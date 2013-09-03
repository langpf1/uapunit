package uap.workflow.engine.command;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;

/*暂没有使用，逻辑有问题，没有完成当前任务就往下推动了*/
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
