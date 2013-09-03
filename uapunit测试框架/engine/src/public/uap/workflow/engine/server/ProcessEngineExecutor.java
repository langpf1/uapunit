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
		 * ��ȡ��ǰ����������Ϣ
		 */
		WorkflowContext.WorkflowSession currSession = WorkflowContext.getCurrentBpmnSession();
		/**
		 * �������̵ĵ�ǰ������
		 */
		setBpmnCtx();
		/**
		 * ���浥����Ϣ
		 */
		saveFormInfoCtx();
		/**
		 * �������������������
		 */
		new CommandService().execute((ICommand<Void>) ClassUtil.loadClass(new CommandFetch(request, response).getCommandClazz()));
		/**
		 * ���µ�����Ϣ
		 */
		updateFormInoCtx();
		/**
		 * �������̵ĵ�ǰ������
		 */
		resetBpmnCtx();
		/**
		 * ��ԭ��������Ϣ
		 */
		WorkflowContext.setBpmnSession(currSession);
	}
	/**
	 * ���浥����Ϣ
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
	 * ���µ�����Ϣ
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
	 * ���������������������Ϣ
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
	 * ����������Ϣ�ÿ�
	 */
	protected void resetBpmnCtx() {
		WorkflowContext.getCurrentBpmnSession().reset();
	}
}
