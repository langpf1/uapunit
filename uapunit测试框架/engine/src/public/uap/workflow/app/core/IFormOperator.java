package uap.workflow.app.core;
public interface IFormOperator {
	/**
	 * 单据保存
	 * 
	 * @param frmInfoCtx
	 * @param flwInfoCtx
	 */
	IBusinessKey save(IBusinessKey frmInfoCtx, FlowInfoCtx flwInfoCtx);
	/**
	 * 单据更新
	 * 
	 * @param frmInfoCtx
	 * @param flwInfoCtx
	 */
	IBusinessKey update(IBusinessKey frmInfoCtx, FlowInfoCtx flwInfoCtx);
}
