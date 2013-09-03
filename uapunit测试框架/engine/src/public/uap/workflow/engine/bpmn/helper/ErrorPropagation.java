/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.bpmn.helper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import uap.workflow.engine.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.bpmn.parser.ErrorEventDefinition;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.delegate.BpmnError;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
/**
 * This class is responsible for finding and executing error handlers for BPMN
 * Errors.
 * 
 * Possible error handlers include Error Intermediate Events and Error Event
 * Sub-Processes.
 * 
 * @author Falko Menge
 * @author Daniel Meyer
 */
public class ErrorPropagation {
	private static final Logger LOG = Logger.getLogger(ErrorPropagation.class.getName());
	public static void propagateError(BpmnError error, IActivityInstance execution) throws Exception {
		propagateError(error.getErrorCode(), execution);
	}
	public static void propagateError(String errorCode, IActivityInstance execution) throws Exception {
		// find local error handler
		String eventHandlerId = findLocalErrorEventHandler(execution, errorCode);
		// TODO: merge two approaches (super process / regular process approach)
		if (eventHandlerId != null) {
			executeCatch(eventHandlerId, execution);
		} else {
			IActivityInstance superExecution = getSuperExecution(execution);
			if (superExecution != null) {
				executeCatchInSuperProcess(errorCode, superExecution);
			} else {
				LOG.info(execution.getActivity().getId() + " throws error event with errorCode '" + errorCode + "', but no catching boundary event was defined. "
						+ "Execution will simply be ended (none end event semantics).");
				execution.end();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private static String findLocalErrorEventHandler(IActivityInstance execution, String errorCode) {
		IScope scope = execution.getActivity();
		while (scope != null) {
			List<ErrorEventDefinition> definitions = (List<ErrorEventDefinition>) scope.getProperty(BpmnParse.PROPERTYNAME_ERROR_EVENT_DEFINITIONS);
			if (definitions != null) {
				// definitions are sorted by precedence, ie. event subprocesses
				// first.
				for (ErrorEventDefinition errorEventDefinition : definitions) {
					if (errorEventDefinition.catches(errorCode)) {
						return scope.findActivity(errorEventDefinition.getHandlerActivityId()).getId();
					}
				}
			}
			// search for error handlers in parent scopes
			if (scope instanceof IActivity) {
				scope = ((IActivity) scope).getParent();
			} else {
				scope = null;
			}
		}
		return null;
	}
	private static void executeCatchInSuperProcess(String errorCode, IActivityInstance superExecution) {
		String errorHandlerId = findLocalErrorEventHandler(superExecution, errorCode);
		if (errorHandlerId != null) {
			executeCatch(errorHandlerId, superExecution);
		} else { // no matching catch found, going one level up in process
					// hierarchy
			IActivityInstance superSuperExecution = getSuperExecution(superExecution);
			if (superSuperExecution != null) {
				executeCatchInSuperProcess(errorCode, superSuperExecution);
			} else {
				throw new BpmnError(errorCode, "No catching boundary event found for error with errorCode '" + errorCode + "', neither in same process nor in parent process");
			}
		}
	}
	private static IActivityInstance getSuperExecution(IActivityInstance execution) {
		return null;
	}
	private static void executeCatch(String errorHandlerId, IActivityInstance execution) {
		IProcessDefinition processDefinition = ((ActivityInstanceEntity) execution).getProcessDefinition();
		IActivity errorHandler = processDefinition.findActivity(errorHandlerId);
		if (errorHandler == null) {
			throw new WorkflowException(errorHandlerId + " not found in process definition");
		}
		boolean matchingParentFound = false;
		IActivityInstance leavingExecution = execution;
		ActivityImpl currentActivity = (ActivityImpl) execution.getActivity();
		ScopeImpl catchingScope = (ScopeImpl) errorHandler.getParent();
		if (catchingScope instanceof ActivityImpl) {
			ActivityImpl catchingScopeActivity = (ActivityImpl) catchingScope;
			if (!catchingScopeActivity.isScope()) { // event subprocesses
				catchingScope = (ScopeImpl) catchingScopeActivity.getParent();
			}
		}
		if (catchingScope instanceof IProcessDefinition) {
			// executeEventHandler(errorHandler, ((ExecutionEntity)
			// execution).getProcessInstance());
		} else {
			if (currentActivity.getId().equals(catchingScope.getId())) {
				matchingParentFound = true;
			} else {
				currentActivity = (ActivityImpl) currentActivity.getParent();
				// Traverse parents until one is found that is a scope
				// and matches the activity the boundary event is defined on
				while (!matchingParentFound && leavingExecution != null && currentActivity != null) {
					if (!leavingExecution.isConcurrent() && currentActivity.getId().equals(catchingScope.getId())) {
						matchingParentFound = true;
					} else if (leavingExecution.isConcurrent()) {
						leavingExecution = leavingExecution.getParent();
					} else {
						currentActivity = (ActivityImpl) currentActivity.getParentActivity();
						leavingExecution = leavingExecution.getParent();
					}
				}
				// Follow parents up until matching scope can't be found anymore
				// (needed to support for multi-instance)
				while (leavingExecution != null && leavingExecution.getParent() != null && leavingExecution.getParent().getActivity() != null
						&& leavingExecution.getParent().getActivity().getId().equals(catchingScope.getId())) {
					leavingExecution = leavingExecution.getParent();
				}
			}
			if (matchingParentFound && leavingExecution != null) {
				executeEventHandler(errorHandler, leavingExecution);
			} else {
				throw new WorkflowException("No matching parent execution for activity " + errorHandlerId + " found");
			}
		}
	}
	private static void executeEventHandler(IActivity borderEventActivity, IActivityInstance leavingExecution) {
		if (borderEventActivity.getActivityBehavior() instanceof EventSubProcessStartEventActivityBehavior) {
			IActivityInstance execution = leavingExecution;
			NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
			List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(borderEventActivity, null);
			if (userTaskPrepCtx.size() > 0) {
				List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
				for (int i = 0; i < userTaskPrepCtx.size(); i++) {
					UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
					UserTaskPrepCtx oneCtx = userTaskPrepCtx.get(i);
					runTimeCtx.setActivityId(oneCtx.getActivityId());
					runTimeCtx.setUserPks(oneCtx.getUserPks());
					nextInfo.add(runTimeCtx);
				}
				nextTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
				nextTaskCtx.setUserPk("timer-transi");
			}
			WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
			WorkflowContext.setBpmnSession(bpmnSession);
			bpmnSession.setRequest(BizProcessServer.createFlowRequest(null, nextTaskCtx));
			execution.setActivity((ActivityImpl) borderEventActivity.getParent());
			execution.performOperation(AtomicOperation.ACTIVITY_START);
		} else {
			leavingExecution.startSubProcess(borderEventActivity);
		}
	}
}
