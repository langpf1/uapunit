package uap.workflow.app.core;
public interface IFormOperator {
	/**
	 * ���ݱ���
	 * 
	 * @param frmInfoCtx
	 * @param flwInfoCtx
	 */
	IBusinessKey save(IBusinessKey frmInfoCtx, FlowInfoCtx flwInfoCtx);
	/**
	 * ���ݸ���
	 * 
	 * @param frmInfoCtx
	 * @param flwInfoCtx
	 */
	IBusinessKey update(IBusinessKey frmInfoCtx, FlowInfoCtx flwInfoCtx);
}
