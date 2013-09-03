package uap.workflow.engine.bpmn.behavior;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.query.Condition;
/**
 * Implementation of the Complex Gateway as defined in the BPMN specification.
 * @author Tijs Rademakers
 * @author Tom Van Buskirk
 * @author Joram Barrez
 */
public class ComplexGatewayActivityBehavior extends GatewayActivityBehavior{
	private static Logger log = Logger.getLogger(ComplexGatewayActivityBehavior.class.getName());
	 protected  Condition expressionCondition=null;
	
	private boolean ComplexgatewayActivationcondition=false;
  
	public boolean getpropertynameComplexgatewayActivationcondition() {
		return this.ComplexgatewayActivationcondition;
	}
	public void setConditionExpression(boolean ComplexgatewayActivationcondition) {
		this.ComplexgatewayActivationcondition = ComplexgatewayActivationcondition;
	}
	public Condition getConditionExpression() {
		return expressionCondition;
	}
	public void setConditionExpression(Condition conditionExpression) {
		this.expressionCondition = conditionExpression;
	}
	@Override
	  protected void leave(IActivityInstance execution) {
		//execution.setActive(false);
		IActivity activity = execution.getActivity();
		int nbrOfExecutionsJoined = 0;
		IProcessInstance iprocessInstance = execution.getProcessInstance();
		List<IActivityInstance> executionList = execution.findIncomeCompletedExecutions(iprocessInstance,activity);
		nbrOfExecutionsJoined=executionList.size();
		Iterator<ITransition> transitionIterator = execution.getActivity().getOutgoingTransitions().iterator();
		ITransition outgoingSeqFlow = null;
		
		String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
		int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();		
		execution.setVariableLocal("nbrOfExecutionsToJoin",nbrOfExecutionsToJoin);
		execution.setVariableLocal("nbrOfExecutionsJoined",nbrOfExecutionsJoined);
		boolean canLeave = expressionCondition.evaluate(execution);
		if (canLeave) {
			while (outgoingSeqFlow == null && transitionIterator.hasNext()) {
				ITransition seqFlow = transitionIterator.next();
				Condition condition = (Condition) seqFlow
						.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
				if ((condition == null && (defaultSequenceFlow == null || !defaultSequenceFlow
						.equals(seqFlow.getId()))) || (condition != null)) {
					if (log.isLoggable(Level.FINE)) {
						log.fine("Sequence flow '" + seqFlow.getId() + " '"
								+ "selected as outgoing sequence flow.");
					}
					outgoingSeqFlow = seqFlow;
				}
			}

		}

		if (outgoingSeqFlow != null) {
			execution.take(outgoingSeqFlow);
		} else {
			if (defaultSequenceFlow != null) {
				ITransition defaultTransition = execution.getActivity()
						.findOutgoingTransition(defaultSequenceFlow);
				if (defaultTransition != null) {
					execution.take(defaultTransition);
				} else {
					throw new WorkflowException("Default sequence flow '"
							+ defaultSequenceFlow + "' not found");
				}
			} else {
				//没有满足复杂网关的条件，直接是空。
			}
		}
	}

}
