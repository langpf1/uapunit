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
package uap.workflow.engine.handler;
import java.util.List;

import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.bpmn.parser.BpmnParseListener;
import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.process.TransitionImpl;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.variable.VariableDeclaration;
import uap.workflow.engine.xml.Element;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Falko Menge
 */
public class HistoryParseListener implements BpmnParseListener {
	protected static final StartEventEndHandler START_EVENT_END_HANDLER = new StartEventEndHandler();
	protected static final ActivityInstanceEndHandler ACTIVITI_INSTANCE_END_LISTENER = new ActivityInstanceEndHandler();
	protected static final ActivityInstanceStartHandler ACTIVITY_INSTANCE_START_LISTENER = new ActivityInstanceStartHandler();
	protected static final UserTaskAssignmentHandler USER_TASK_ASSIGNMENT_HANDLER = new UserTaskAssignmentHandler();
	// The history level set in the Activiti configuration
	protected int historyLevel;
	public HistoryParseListener(int historyLevel) {
		this.historyLevel = historyLevel;
	}
	public void parseProcess(Element processElement, IProcessDefinition processDefinition) {
		if (activityHistoryEnabled(processDefinition, historyLevel)) {
			processDefinition.addExecutionListener(IInstanceListener.EVENTNAME_END, new ProcessInstanceEndHandler());
		}
	}
	public void parseExclusiveGateway(Element exclusiveGwElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseInclusiveGateway(Element inclusiveGwElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseCallActivity(Element callActivityElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseManualTask(Element manualTaskElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseReceiveTask(Element receiveTaskElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseScriptTask(Element scriptTaskElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseTask(Element taskElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
	}
	public void parseUserTask(Element userTaskElement, IScope scope, IActivity activity) {
		addActivityHandlers(activity);
		if (activityHistoryEnabled(scope, historyLevel)) {
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
			taskDefinition.addTaskListener(ITaskListener.EVENTNAME_DELEGATE_AFTER, USER_TASK_ASSIGNMENT_HANDLER);
		}
	}
	public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
		addActivityHandlers(activity);
	}
	public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {
		addActivityHandlers(activity);
	}
	public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {
		addActivityHandlers(activity);
	}
	public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl activity) {
		if (fullHistoryEnabled(historyLevel)) {
			activity.addExecutionListener(IInstanceListener.EVENTNAME_END, START_EVENT_END_HANDLER);
		}
	}
	public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {
		addActivityHandlers(activity);
	}
	public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
	}
	public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {
	}
	public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl timerActivity) {
	}
	public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
	}
	public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity) {
	}
	public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, ActivityImpl activity) {
	}
	public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
	}
	public void parseRootElement(Element rootElement, List<IProcessDefinition> processDefinitions) {
	}
	public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl signalActivity) {
	}
	public void parseEventBasedGateway(Element eventBasedGwElement, ScopeImpl scope, ActivityImpl activity) {
	}
	public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
		// Remove any history parse listeners already attached: the Multi
		// instance behavior will
		// call them for every instance that will be created
	}
	// helper methods
	// ///////////////////////////////////////////////////////////
	protected void addActivityHandlers(IActivity activity) {
		if (activityHistoryEnabled(activity, historyLevel)) {
			activity.addExecutionListener(IInstanceListener.EVENTNAME_START, ACTIVITY_INSTANCE_START_LISTENER);
			activity.addExecutionListener(IInstanceListener.EVENTNAME_END, ACTIVITI_INSTANCE_END_LISTENER);
		}
	}
	public static boolean fullHistoryEnabled(int historyLevel) {
		return historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_FULL;
	}
	public static boolean auditHistoryEnabled(IScope scopeElement, int historyLevel) {
		return historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT;
	}
	public static boolean activityHistoryEnabled(IScope scopeElement, int historyLevel) {
		return historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY;
	}
	public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, IActivity signalActivity) {
	}
	public void parseTransaction(Element transactionElement, IScope scope, IActivity activity) {
	}
	public void parseCompensateEventDefinition(Element compensateEventDefinition, IActivity compensationActivity) {
	}
	public void parseIntermediateThrowEvent(Element intermediateEventElement, IScope scope, IActivity activity) {
	}
	@Override
	public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, IActivity activity, IActivity nestedErrorEventActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, IActivity signalActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, IActivity timerActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseBusinessRuleTask(Element businessRuleTaskElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseEndEvent(Element endEventElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseEventBasedGateway(Element eventBasedGwElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, IActivity timerActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseParallelGateway(Element parallelGwElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseSendTask(Element sendTaskElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseSequenceFlow(Element sequenceFlowElement, IScope scopeElement, ITransition transition) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseServiceTask(Element serviceTaskElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseStartEvent(Element startEventElement, IScope scope, IActivity startEventActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseSubProcess(Element subProcessElement, IScope scope, IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parseComplexGateway(Element complexGwElement, IScope scope,
			IActivity activity) {
		// TODO Auto-generated method stub
		
	}
	
   public void parseIntermediateCompensateEventDefinition(Element compensateEventDefinition, IActivity compensateActivity){
    	
    }
   public void parseIntermediateLinkCatchEventDefinition(Element linkEventDefinition, IActivity linkActivity){
   	
   }

}
