package uap.workflow.engine.bpmn.behavior;

import uap.workflow.engine.bpmn.parser.LinkEventDefinition;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
/**
 * @author Daniel Meyer
 */
public class IntermediateThrowLinkEventActivityBehavior extends AbstractBpmnActivityBehavior {
	protected  LinkEventDefinition linkEventDefinition;
	public IntermediateThrowLinkEventActivityBehavior(LinkEventDefinition linkEventDefinition2) {
		this.linkEventDefinition = linkEventDefinition2;
	}
	public void execute(IActivityInstance execution) throws Exception {
		 leaveLink(linkEventDefinition.getTarget().getValue(), execution);	
	}
	private static void leaveLink(String linkHandlerId, IActivityInstance execution) {
		IProcessDefinition processDefinition = ((ActivityInstanceEntity) execution).getProcessDefinition();
		IActivity linkHandler = processDefinition.findActivity(linkHandlerId);
		if (linkHandler == null) {
			throw new WorkflowException(linkHandlerId + " not found in process definition");
		}
		ActivityInstanceEntity linkExecution =(ActivityInstanceEntity) execution;
		linkExecution.setSuperExecution(execution);
		linkExecution.setActivity(linkHandler);
		linkExecution.setEventScope(false);
		linkExecution.performOperation(AtomicOperation.ACTIVITY_START);	
	}
}

