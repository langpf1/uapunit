package uap.workflow.engine.server;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.app.core.IFormOperator;
import uap.workflow.engine.command.CommandService;
import uap.workflow.engine.command.ICommand;
import uap.workflow.engine.dftimpl.DftFormOperator;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.ClassUtil;
public class ProcessEngineExecutor {
	private IFlowRequest request;
	private IFlowResponse response;
	public ProcessEngineExecutor(IFlowRequest request, IFlowResponse response) {
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
	@SuppressWarnings("unchecked")
	public void execute() {
		/**
		 * 提取当前的上下文信息
		 */
		WorkflowContext.WorkflowSession currSession = WorkflowContext.getCurrentBpmnSession();
		/**
		 * 设置流程的当前上下文
		 */
		setBpmnCtx();
		/**
		 * 保存单据信息
		 */
		saveFormInfoCtx();
		/**
		 * 调用流程引擎的命令链
		 */
		new CommandService().execute((ICommand<Void>) ClassUtil.loadClass(new CommandFetch(request, response).getCommandClazz()));
		/**
		 * 更新单据信息
		 */
		updateFormInoCtx();
		/**
		 * 重置流程的当前上下文
		 */
		resetBpmnCtx();
		/**
		 * 还原上下文信息
		 */
		WorkflowContext.setBpmnSession(currSession);
	}
	/**
	 * 保存单据信息
	 */
	protected void saveFormInfoCtx() {
		FlowInfoCtx flowInoCtx = request.getFlowInfoCtx();
		IBusinessKey formInoCtx = request.getBusinessObject();
		// ProcessDefinition proDef =
		// WorkflowContext.getCurrentBpmnSession().getProDef();
		String serverClazz = DftFormOperator.class.getName();
		if (serverClazz == null || serverClazz.trim().length() == 0) {
			serverClazz = DftFormOperator.class.getName();
		}
		IFormOperator formOper = (IFormOperator) ClassUtil.loadClass(serverClazz);
		formInoCtx = formOper.save(formInoCtx, flowInoCtx);
		//request.setFormInfoCtx(formInoCtx);
		//WorkflowContext.getCurrentBpmnSession().getRequest().setFormInfoCtx(formInoCtx);
	}
	/**
	 * 更新单据信息
	 */
	protected void updateFormInoCtx() {
		FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
		IBusinessKey formInfoCtx = request.getBusinessObject();
		// ProcessDefinition proDef = null;
		String serverClazz = null;// lwTypeVo.getServerclass();
		if (serverClazz == null || serverClazz.trim().length() == 0) {
			serverClazz = DftFormOperator.class.getName();
		}
		IFormOperator formOper = (IFormOperator) ClassUtil.loadClass(serverClazz);
		formOper.update(formInfoCtx, flowInfoCtx);
	}
	/**
	 * 设置流程引擎的上下文信息
	 */
	protected void setBpmnCtx() {
		WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
		WorkflowContext.setBpmnSession(bpmnSession);
		FlowInfoCtx flowInfoCtx = (FlowInfoCtx) request.getFlowInfoCtx();
		flowInfoCtx.check();
		IBusinessKey formInfoCtx = (IBusinessKey) request.getBusinessObject();
		bpmnSession.setRequest(request);
		bpmnSession.setResponse(response);
		bpmnSession.getRequest().setFlowInfoCtx(flowInfoCtx);
		bpmnSession.getRequest().setBusinessObject(formInfoCtx);
	}
	/**
	 * 把上下文信息置空
	 */
	protected void resetBpmnCtx() {
		WorkflowContext.getCurrentBpmnSession().reset();
	}
}
