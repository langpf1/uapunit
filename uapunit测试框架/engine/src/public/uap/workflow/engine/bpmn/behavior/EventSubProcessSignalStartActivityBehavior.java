package uap.workflow.engine.bpmn.behavior;

import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.bpmn.parser.SignalEventDefinition;
import uap.workflow.engine.core.IActivityInstance;

public class EventSubProcessSignalStartActivityBehavior extends NoneStartEventActivityBehavior{
	protected boolean isInterrupting = true;
	protected  SignalEventDefinition signalStartEventDefinition;
	public EventSubProcessSignalStartActivityBehavior(SignalEventDefinition signalDefinition) {
		this.signalStartEventDefinition=signalDefinition;
	}
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		IActivityInstance outgoingExecution = execution;
		if (isInterrupting) {
			ScopeUtil.destroyScope(execution, "interrupting event subprocess started");
		} else {
			outgoingExecution = execution.createExecution(execution.getActivity());
			outgoingExecution.setActive(true);
			outgoingExecution.setScope(false);
			outgoingExecution.setConcurrent(true);
		}
		super.execute(outgoingExecution);
	}
}