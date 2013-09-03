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
		if (flowInfoCtx instanceof UnStartProInsCtx) {// ��������
			commandClazz = StartProInsCmd.class.getName();
		} else if (flowInfoCtx instanceof NextTaskInsCtx) {// �˹������ƶ�
			commandClazz = NextTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof RejectTaskInsCtx) {// ��������
			commandClazz = RejectTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof DelegateTaskInsCtx) {// ��������
			commandClazz = DelegateTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof TakeBackTaskInsCtx) {// ����ȡ�� --ȡ���ύ
			commandClazz = TakeBackTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof CallBackTaskInsCtx) {// �����ջ�--����������̻���
			commandClazz = CallBackTaskInsCmd.class.getName();
		} else if (flowInfoCtx instanceof CreateAfterAddSignCtx) {// ���ǩ
			commandClazz = CreateAfterAddSignCmd.class.getName();
		} else if (flowInfoCtx instanceof CreateBeforeAddSignCtx) {// ǰ��ǩ
			commandClazz = CreateBeforeAddSignCmd.class.getName();
		} else if (flowInfoCtx instanceof StopBeforeAddSignTaskInsCtx) {// ֹͣǰ��ǩ
			commandClazz = StopBeforeAddSignTaskCmd.class.getName();
		} else if (flowInfoCtx instanceof ReStartBeforeAddSignTaskInsCtx) {// ����ǰ��ǩ
			commandClazz = RestartAddSignTaskCmd.class.getName();
		} else if (flowInfoCtx instanceof NextReceiveTaskCtx) {// ���������ƶ�
			commandClazz = ReceiveTaskCmd.class.getName();
		}else if (flowInfoCtx instanceof SubmitStartFormCtx) {
			commandClazz = SubmitStartFormCmd.class.getName();
		} else if (flowInfoCtx instanceof SubmitTaskFormCtx) {
			commandClazz = SubmitTaskFormCmd.class.getName();
		}
		return commandClazz;
	}
}
