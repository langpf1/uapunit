
package uap.workflow.engine.bpmn.behavior;
import uap.workflow.engine.bpmn.parser.MessageEventDefinition;
/**
 * @author Daniel Meyer
 */
public class IntermediateThrowMessageEventActivityBehavior extends FlowNodeActivityBehavior {
	protected final MessageEventDefinition messageEventDefinition;
	public IntermediateThrowMessageEventActivityBehavior(MessageEventDefinition messageEventDefinition) {
		this.messageEventDefinition=messageEventDefinition;
	}
	//@Override
	/*
	public void execute(IActivitiExecution execution) throws Exception {
		final String activityRef = messageEventDefinition.getActivityId();
		IActivitiExecution scopeExecution = ScopeUtil.findScopeExecution((ExecutionEntity) execution, (ActivityImpl) execution.getActivity());
		List<MessageEventSubscriptionEntity> eventSubscriptions;
		if (activityRef != null) {
			eventSubscriptions = scopeExecution.getMessageEventSubscriptions(activityRef);
		} else {
			eventSubscriptions = scopeExecution.getMessageEventSubscriptions();
		}
		if (eventSubscriptions.isEmpty()) {
			leave(execution);
		} else {
			// TODO: implement async (waitForCompletion=false in bpmn)
			ScopeUtil.throwCompensationEvent(eventSubscriptions, execution, false);
		}
	}
	public void signal(IActivitiExecution execution, String signalName, Object signalData) throws Exception {
		// join compensating executions
		if (execution.getExecutions().isEmpty()) {
			leave(execution);
		}
	}
	*/
}
