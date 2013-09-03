package uap.workflow.engine.bpmn.behavior;


import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.bpmn.webservice.MessageDefinition;
import uap.workflow.engine.core.IActivityInstance;

public class EventSubProcessMessageStartEventActivityBehavior extends NoneStartEventActivityBehavior {
	protected boolean isInterrupting = true;
    protected MessageDefinition messageDefinition ;
	public EventSubProcessMessageStartEventActivityBehavior(MessageDefinition messageDefinition) {
          this.messageDefinition = messageDefinition;
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


