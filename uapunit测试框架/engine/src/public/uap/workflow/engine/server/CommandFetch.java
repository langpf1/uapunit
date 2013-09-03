package uap.workflow.engine.server;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.command.CreateAfterAddSignCmd;
import uap.workflow.engine.command.CreateBeforeAddSignCmd;
import uap.workflow.engine.command.CallBackTaskInsCmd;
import uap.workflow.engine.command.DelegateTaskInsCmd;
import uap.workflow.engine.command.NextTaskInsCmd;
import uap.workflow.engine.command.ReceiveTaskCmd;
import uap.workflow.engine.command.RejectTaskInsCmd;
import uap.workflow.engine.command.RestartAddSignTaskCmd;
import uap.workflow.engine.command.StartProInsCmd;
import uap.workflow.engine.command.StopBeforeAddSignTaskCmd;
import uap.workflow.engine.command.SubmitStartFormCmd;
import uap.workflow.engine.command.SubmitTaskFormCmd;
import uap.workflow.engine.command.TakeBackTaskInsCmd;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.CallBackTaskInsCtx;
import uap.workflow.engine.context.DelegateTaskInsCtx;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.ReStartBeforeAddSignTaskInsCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.StopBeforeAddSignTaskInsCtx;
import uap.workflow.engine.context.SubmitStartFormCtx;
import uap.workflow.engine.context.SubmitTaskFormCtx;
import uap.workflow.engine.context.TakeBackTaskInsCtx;
import uap.workflow.engine.context.UnStartProInsCtx;
public class CommandFetch {
	private IFlowRequest request;
	private IFlowResponse response;
	public CommandFetch(IFlowRequest request, IFlowResponse response) {
		super();
		this.request = request;
		this.response = response;
	}
	public IFlowRequest getRequest() {
		return request;
	}
	public void setRequest(IFlowRequest request) {
		this.request = request;
	}
	public IFlowResponse getResponse() {
		return response;
	}
	public void setResponse(IFlowResponse response) {
		this.response = response;
	}
	public String getCommandClazz() {
		String commandClazz = null;
		FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
		if (flowInfoCtx instanceof UnStartProInsCtx) {// 流程启动
			commandClazz = StartProInsCmd.class.getName();
		} else if (flowInfoCtx instanceof NextTaskInsCtx) {// 人工任务推动
			commandClazz = NextTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof RejectTaskInsCtx) {// 驳回任务
			commandClazz = RejectTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof DelegateTaskInsCtx) {// 改派任务
			commandClazz = DelegateTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof TakeBackTaskInsCtx) {// 任务取回 --取消提交
			commandClazz = TakeBackTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof CallBackTaskInsCtx) {// 任务收回--弃审或者流程回退
			commandClazz = CallBackTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof CreateAfterAddSignCtx) {// 后加签
			commandClazz = CreateAfterAddSignCmd.class.getName();
		} else if (flowInfoCtx instanceof CreateBeforeAddSignCtx) {// 前加签
			commandClazz = CreateBeforeAddSignCmd.class.getName();
		} else if (flowInfoCtx instanceof StopBeforeAddSignTaskInsCtx) {// 停止前加签
			commandClazz = StopBeforeAddSignTaskCmd.class.getName();
		} else if (flowInfoCtx instanceof ReStartBeforeAddSignTaskInsCtx) {// 重启前加签
			commandClazz = RestartAddSignTaskCmd.class.getName();
		} else if (flowInfoCtx instanceof NextReceiveTaskCtx) {// 接收任务推动
			commandClazz = ReceiveTaskCmd.class.getName();
		}else if (flowInfoCtx instanceof SubmitStartFormCtx) {
			commandClazz = SubmitStartFormCmd.class.getName();
		} else if (flowInfoCtx instanceof SubmitTaskFormCtx) {
			commandClazz = SubmitTaskFormCmd.class.getName();
		}
		return commandClazz;
	}
}
