package uap.workflow.engine.command;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;

/*��û��ʹ�ã��߼������⣬û����ɵ�ǰ����������ƶ���*/
public class ReceiveTaskCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		if (flowInfoCtx instanceof NextReceiveTaskCtx) {
			NextReceiveTaskCtx ctx = (NextReceiveTaskCtx) flowInfoCtx;
			String executionId = ctx.getExecutionId();
			WfmServiceFacility.getRunTimeServie().signal(executionId);
		}
		return null;
	}
}
