package uap.workflow.engine.session;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.StartedProInsCtx;
import uap.workflow.engine.context.UnStartProInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.TaskUtil;
public class WorkflowContext {
	public static final ThreadLocal<WorkflowSession> bpmnContext = new ThreadLocal<WorkflowSession>();
	public static WorkflowSession getCurrentBpmnSession() {
		return bpmnContext.get();
	}
	public static void setBpmnSession(WorkflowSession bpmnSession) {
		bpmnContext.set(bpmnSession);
	}
	public static void removeFormSession() {
		bpmnContext.remove();
	}
	public class WorkflowSession {
		private IFlowRequest request;
		private IFlowResponse response;
		private IProcessInstance processInstance;
		private UserTaskRunTimeCtx cntUserTaskInfo;
		public IBusinessKey getBusinessObject() {
			if (request != null)
				return request.getBusinessObject();
			else
				return null;
		}
		public FlowInfoCtx getFlowInfoCtx() {
			if (request != null)
				return request.getFlowInfoCtx();
			else
				return null;
		}
		public String getProDefPk() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startedFlowInfoCtx = (StartedProInsCtx) flowInfoCtx;
				ITask task = TaskUtil.getTaskByTaskPk(startedFlowInfoCtx.getTaskPk());
				return task.getProcessDefinition().getProDefPk();
			}
			if (flowInfoCtx instanceof UnStartProInsCtx) {
				UnStartProInsCtx unStartProInsCtx = (UnStartProInsCtx) flowInfoCtx;
				return unStartProInsCtx.getProDefPk();
			}
			return null;
		}
		public String getCntUserPk() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			return flowInfoCtx.getUserPk();
		}
		public String getProDefId() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startedFlowInfoCtx = (StartedProInsCtx) flowInfoCtx;
				ITask task = TaskUtil.getTaskByTaskPk(startedFlowInfoCtx.getTaskPk());
				return task.getProcessDefinition().getProDefId();
			}
			if (flowInfoCtx instanceof UnStartProInsCtx) {
				UnStartProInsCtx unStartProInsCtx = (UnStartProInsCtx) flowInfoCtx;
				String proDefPk = unStartProInsCtx.getProDefPk();
				if (proDefPk == null || proDefPk.length() == 0) {
					return unStartProInsCtx.getProDefId();
				} else {
					return ProcessDefinitionUtil.getProDefByProDefPk(proDefPk).getProDefId();
				}
			}
			return null;
		}
		public String getTaskPk() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startedFlowInfoCtx = (StartedProInsCtx) flowInfoCtx;
				return startedFlowInfoCtx.getTaskPk();
			}
			return null;
		}
		public IProcessDefinition getProDef() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startedFlowInfoCtx = (StartedProInsCtx) flowInfoCtx;
				ITask task = TaskUtil.getTaskByTaskPk(startedFlowInfoCtx.getTaskPk());
				return task.getProcessDefinition();
			}
			if (flowInfoCtx instanceof UnStartProInsCtx) {
				UnStartProInsCtx unStartProInsCtx = (UnStartProInsCtx) flowInfoCtx;
				String proDefPk = unStartProInsCtx.getProDefPk();
				return ProcessDefinitionUtil.getProDefByProDefPk(proDefPk);
			}
			return null;
		}
		public ITask getTask() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startedFlowInfoCtx = (StartedProInsCtx) flowInfoCtx;
				return TaskUtil.getTaskByTaskPk(startedFlowInfoCtx.getTaskPk());
			}
			return null;
		}
		public String getBusinessPk() {
			IBusinessKey businessObject = request.getBusinessObject();
			if (businessObject == null) {
				return null;
			}
			return businessObject.getBillId();
		}
		public UserTaskRunTimeCtx[] getUserTaskCtx() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof NextTaskInsCtx) {
				NextTaskInsCtx nextCtx = (NextTaskInsCtx) flowInfoCtx;
				return nextCtx.getNextInfo();
			}
			if (flowInfoCtx instanceof CommitProInsCtx) {
				CommitProInsCtx commitProIns = (CommitProInsCtx) flowInfoCtx;
				return commitProIns.getNextInfo();
			}
			if (flowInfoCtx instanceof NextReceiveTaskCtx) {
				NextReceiveTaskCtx ctx = (NextReceiveTaskCtx) flowInfoCtx;
				return ctx.getNextInfo();
			}
			if (flowInfoCtx instanceof RejectTaskInsCtx) {
				RejectTaskInsCtx ctx = (RejectTaskInsCtx) flowInfoCtx;
				UserTaskRunTimeCtx userTaskRunTimeCtx = ctx.getRejectInfo();
				UserTaskRunTimeCtx[] userTaskRunTimeCtxs = new UserTaskRunTimeCtx[1];
				userTaskRunTimeCtxs[0]=userTaskRunTimeCtx;
				return userTaskRunTimeCtxs;
			}
			return null;
		
		}
		public String getOpinion() {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx ctx = (StartedProInsCtx) flowInfoCtx;
				return ctx.getComment();
			}
			return null;
		}
		public UserTaskRunTimeCtx getCntUserTaskInfo() {
			return cntUserTaskInfo;
		}
		public void setCntUserTaskInfo(UserTaskRunTimeCtx cntUserTaskInfo) {
			this.cntUserTaskInfo = cntUserTaskInfo;
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
		public IProcessInstance getProcessInstance() {
			return processInstance;
		}
		public void setProcessInstance(IProcessInstance processInstance) {
			this.processInstance = processInstance;
		}
		public void reset() {
			request = null;
			response = null;
			processInstance = null;
		}
	}
}
