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
package uap.workflow.engine.bpmn.behavior;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import nc.vo.pub.AggregatedValueObject;

import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.bpmn.helper.ErrorPropagation;
import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.delegate.BpmnError;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.invocation.ExecutionListenerInvocation;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.behavior.CompositeActivityBehavior;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.ClassUtil;
/**
 * Implementation of the multi-instance functionality as described in the BPMN
 * 2.0 spec.
 * 
 * Multi instance functionality is implemented as an {@link ActivityBehavior}
 * that wraps the original {@link ActivityBehavior} of the activity.
 * 
 * Only subclasses of {@link AbstractBpmnActivityBehavior} can have
 * multi-instance behavior. As such, special logic is contained in the
 * {@link AbstractBpmnActivityBehavior} to delegate to the
 * {@link MultiInstanceActivityBehavior} if needed.
 * 
 * @author Joram Barrez
 * @author Falko Menge
 */
public abstract class MultiInstanceActivityBehavior extends FlowNodeActivityBehavior implements CompositeActivityBehavior {
	protected static final Logger LOGGER = Logger.getLogger(MultiInstanceActivityBehavior.class.getName());
	// Variable names for outer instance(as described in spec)
	protected final String NUMBER_OF_INSTANCES = "nrOfInstances";
	//protected final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
	protected final String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";
	// Variable names for inner instances (as described in the spec)
	protected final String LOOP_COUNTER = "loopCounter";
	// Instance members
	protected IActivity activity;
	protected AbstractBpmnActivityBehavior innerActivityBehavior;
	protected Expression loopCardinalityExpression;
	protected Expression completionConditionExpression;
	protected Expression collectionExpression;
	protected String collectionVariable;
	protected String collectionElementVariable;
	protected static Stack<IBusinessKey> stack = new Stack<IBusinessKey>();
	/**
	 * @param innerActivityBehavior
	 *            The original {@link ActivityBehavior} of the activity that
	 *            will be wrapped inside this behavior.
	 * @param isSequential
	 *            Indicates whether the multi instance behavior must be
	 *            sequential or parallel
	 */
	public MultiInstanceActivityBehavior(IActivity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
		this.activity = activity;
		setInnerActivityBehavior(innerActivityBehavior);
	}
	public void execute(IActivityInstance execution) throws Exception {
		if (getLoopVariable(execution, LOOP_COUNTER) == null) {
			try {
				createInstances(execution);
			} catch (BpmnError error) {
				ErrorPropagation.propagateError(error, execution);
			}
		} else {
			innerActivityBehavior.execute(execution);
		}
	}
	protected abstract void createInstances(IActivityInstance execution) throws Exception;
	// Intercepts signals, and delegates it to the wrapped {@link
	// ActivityBehavior}.
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		innerActivityBehavior.signal(execution, signalName, signalData);
	}
	// required for supporting embedded subprocesses
	public void lastExecutionEnded(IActivityInstance execution) {
		ScopeUtil.createEventScopeExecution((ActivityInstanceEntity) execution);
		//恢复上下文环境，zhailzh （没有找到切换上下文的地方，故暂改，2013.1.25）
		IBusinessKey bizObjRef =  WorkflowContext.getCurrentBpmnSession().getBusinessObject();
		IBusinessKey bizObjRefTemp = new BizObjectImpl();
		if(!stack.empty()){
			bizObjRefTemp = stack.pop();	
		}else{
			throw new WorkflowRuntimeException("couldn't execute activity <" + activity.getProperty("type") + " id=\"" + activity.getId() + "\" ...>: stack is null");
		}
		bizObjRef.setBillId(bizObjRefTemp.getBillId());
		bizObjRef.setBillType(bizObjRefTemp.getBillType());
		execution.getProcessInstance().setPk_form_ins(bizObjRef.getBillId());
		execution.getProcessInstance().setPk_form_ins_version(bizObjRef.getBillId());
		((ProcessInstanceEntity)(execution.getProcessInstance())).asyn();
		//
		leave(execution);
	}
	// required for supporting external subprocesses
	public void completing(IActivityInstance execution, IActivityInstance subProcessInstance) throws Exception {}
	// required for supporting external subprocesses
	public void completed(IActivityInstance execution) throws Exception {
		leave(execution);
	}
	// Helpers
	// //////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	protected int resolveNrOfInstances(IActivityInstance execution) {
		int nrOfInstances = -1;
		if (loopCardinalityExpression != null) {
			nrOfInstances = resolveLoopCardinality(execution);
		} else if (collectionExpression != null) {
			Object obj = collectionExpression.getValue(execution);
			if (obj instanceof Collection) {
				nrOfInstances = ((Collection<Object>) obj).size();
			}else if(obj instanceof Object[]){
				nrOfInstances = ((Object[]) obj).length;
			}else
				throw new WorkflowException(collectionExpression.getExpressionText() + "' didn't resolve to a Collection");
			
		} else if (collectionVariable != null) {
			Object obj = execution.getVariable(collectionVariable);
			obj=((ICollectionService)ClassUtil.loadClass("nc.uap.engine.test.TestCollectionService")).getCollction();
			if (!(obj instanceof Collection)) {
				throw new WorkflowException("Variable " + collectionVariable + "' is not a Collection");
			}
			nrOfInstances = ((Collection<Object>) obj).size();
		} else {
			throw new WorkflowException("Couldn't resolve collection expression nor variable reference");
		}
		return nrOfInstances;
	}
	protected void executeOriginalBehavior(IActivityInstance execution, int loopCounter) throws Exception {
		if (usesCollection() && collectionElementVariable != null) {
			Collection<?> collection = null;
			if (collectionExpression != null) {
				Object obj = collectionExpression.getValue(execution);
				if (obj instanceof Collection){
					collection = (Collection<?>) obj;
					Object value = null;
					int index = 0;
					Iterator<?> it = collection.iterator();
					while (index <= loopCounter) {
						value = it.next();
						index++;
					}
					//修改FormInfoCtx上的子对象
					//execution.getFromInfoCtx().setBillVos(new Object[]{value});
					setLoopVariable(execution, collectionElementVariable, value);
					// If loopcounter == 1, then historic activity instance already created,
					// no need to
					// pass through executeActivity again since it will create a new
					// historic activity
					if (loopCounter == 0) {
						innerActivityBehavior.execute(execution);
					} else {
						execution.startSubProcess(activity);
					}
				}else if(obj instanceof Object[]){
					Object[] object = (Object[])obj;
					//for(Object value : object){
					//	setLoopVariable(execution, collectionElementVariable, value);
					//}
					Object value = object[loopCounter];
					//修改FormInfoCtx上的子对象
					//execution.getFromInfoCtx().setBizObjects(new Object[]{value});
					setLoopVariable(execution, collectionElementVariable, value);
					if (loopCounter == 0) {
						//zhailzh 2013.1.25 没有找到切换上下文的地方，暂改。
						//多实例切换上下文
						IBusinessKey bizObjRef =  WorkflowContext.getCurrentBpmnSession().getBusinessObject();
						IBusinessKey bizObjRefSave = new BizObjectImpl();
						bizObjRefSave.setBillId(bizObjRef.getBillId());
						bizObjRefSave.setBillType(bizObjRef.getBillType());
						stack.push(bizObjRefSave);
						if(value instanceof AggregatedValueObject){
							AggregatedValueObject bizObject = (AggregatedValueObject)value;
							bizObjRef.setBillId(bizObject.getParentVO().getPrimaryKey());
							bizObjRef.setBillType(bizObject.getParentVO().getClass().getName());
							execution.getProcessInstance().setPk_form_ins(bizObject.getParentVO().getPrimaryKey());
							execution.getProcessInstance().setPk_form_ins_version(bizObject.getParentVO().getPrimaryKey());
							((ProcessInstanceEntity)(execution.getProcessInstance())).asyn();
						}
						//
						innerActivityBehavior.execute(execution);
					} else {
						execution.startSubProcess(activity);
					}					
				}
				
			} else if (collectionVariable != null) {
				collection = ((ICollectionService)ClassUtil.loadClass("nc.uap.engine.test.TestCollectionService")).getCollction();
			}
		}
	}
	protected boolean usesCollection() {
		return collectionExpression != null || collectionVariable != null;
	}
	protected boolean isExtraScopeNeeded() {
		// special care is needed when the behavior is an embedded subprocess
		// (not very clean, but it works)
		return innerActivityBehavior instanceof uap.workflow.engine.bpmn.behavior.SubProcessActivityBehavior;
	}
	protected int resolveLoopCardinality(IActivityInstance execution) {
		// Using Number since expr can evaluate to eg. Long (which is also the
		// default for Juel)
		Object value = loopCardinalityExpression.getValue(execution);
		if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			return Integer.valueOf((String) value);
		} else {
			throw new WorkflowException("Could not resolve loopCardinality expression '" + loopCardinalityExpression.getExpressionText() + "': not a number nor number String");
		}
	}
	protected boolean completionConditionSatisfied(IActivityInstance execution) {
		if (completionConditionExpression != null) {
			Object value = completionConditionExpression.getValue(execution);
			if (!(value instanceof Boolean)) {
				throw new WorkflowException("completionCondition '" + completionConditionExpression.getExpressionText() + "' does not evaluate to a boolean value");
			}
			Boolean booleanValue = (Boolean) value;
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Completion condition of multi-instance satisfied: " + booleanValue);
			}
			return booleanValue;
		}
		return false;
	}
	protected void setLoopVariable(IActivityInstance execution, String variableName, Object value) {
		execution.setVariableLocal(variableName, value);
	}
	protected Integer getLoopVariable(IActivityInstance execution, String variableName) {
		Object value = execution.getVariableLocal(variableName);
		IActivityInstance parent = execution.getParent();
		while (value == null && parent != null) {
			value = parent.getVariableLocal(variableName);
			parent = parent.getParent();
		}
		if(value == null)
			return null;
		return Integer.valueOf(value.toString());
	}
	/**
	 * Since no transitions are followed when leaving the inner activity, it is
	 * needed to call the end listeners yourself.
	 */
	protected void callActivityEndListeners(IActivityInstance execution) {
		List<IInstanceListener> listeners = activity.getExecutionListeners(IInstanceListener.EVENTNAME_END);
		for (IInstanceListener executionListener : listeners) {
			try {
				Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ExecutionListenerInvocation(executionListener, execution));
			} catch (Exception e) {
				throw new WorkflowException("Couldn't execute end listener", e);
			}
		}
	}
	protected void logLoopDetails(IActivityInstance execution, String custom, int loopCounter, int nrOfCompletedInstances, int nrOfActiveInstances, int nrOfInstances) {
		if (LOGGER.isLoggable(Level.FINE)) {
			StringBuilder strb = new StringBuilder();
			strb.append("Multi-instance '" + execution.getActivity() + "' " + custom + ". ");
			strb.append("Details: loopCounter=" + loopCounter + ", ");
			strb.append("nrOrCompletedInstances=" + nrOfCompletedInstances + ", ");
			strb.append("nrOfActiveInstances=" + nrOfActiveInstances + ", ");
			strb.append("nrOfInstances=" + nrOfInstances);
			LOGGER.fine(strb.toString());
		}
	}
	// Getters and Setters
	// ///////////////////////////////////////////////////////////
	public Expression getLoopCardinalityExpression() {
		return loopCardinalityExpression;
	}
	public void setLoopCardinalityExpression(Expression loopCardinalityExpression) {
		this.loopCardinalityExpression = loopCardinalityExpression;
	}
	public Expression getCompletionConditionExpression() {
		return completionConditionExpression;
	}
	public void setCompletionConditionExpression(Expression completionConditionExpression) {
		this.completionConditionExpression = completionConditionExpression;
	}
	public Expression getCollectionExpression() {
		return collectionExpression;
	}
	public void setCollectionExpression(Expression collectionExpression) {
		this.collectionExpression = collectionExpression;
	}
	public String getCollectionVariable() {
		return collectionVariable;
	}
	public void setCollectionVariable(String collectionVariable) {
		this.collectionVariable = collectionVariable;
	}
	public String getCollectionElementVariable() {
		return collectionElementVariable;
	}
	public void setCollectionElementVariable(String collectionElementVariable) {
		this.collectionElementVariable = collectionElementVariable;
	}
	public void setInnerActivityBehavior(AbstractBpmnActivityBehavior innerActivityBehavior) {
		this.innerActivityBehavior = innerActivityBehavior;
		this.innerActivityBehavior.setMultiInstanceActivityBehavior(this);
	}
}
