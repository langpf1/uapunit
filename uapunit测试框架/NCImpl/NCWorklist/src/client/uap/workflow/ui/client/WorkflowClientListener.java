package uap.workflow.ui.client;

public interface WorkflowClientListener {

	Object beforeSubmit(WorkflowClientEvent event);
	
	Object afterSubmit(WorkflowClientEvent event);
	
	Object beforeSignal(WorkflowClientEvent event);
	
	Object afterSignal(WorkflowClientEvent event);
	
	Object beforeReject(WorkflowClientEvent event);
	
	Object afterReject(WorkflowClientEvent event);
}
