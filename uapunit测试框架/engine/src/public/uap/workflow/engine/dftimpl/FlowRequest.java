package uap.workflow.engine.dftimpl;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.engine.common.ExtendAttributeSupport;
/**
 * 
 * @author tianchw
 * 
 */
public class FlowRequest extends ExtendAttributeSupport implements IFlowRequest {
	private static final long serialVersionUID = 1L;
	protected FlowInfoCtx flowInfoCtx;
	protected IBusinessKey formInfoCtx;
	public FlowInfoCtx getFlowInfoCtx() {
		return flowInfoCtx;
	}
	public IBusinessKey getBusinessObject() {
		return formInfoCtx;
	}
	public void setFlowInfoCtx(FlowInfoCtx flowInfoCtx) {
		this.flowInfoCtx = flowInfoCtx;
	}
	public void setBusinessObject(IBusinessKey formInfoCtx) {
		this.formInfoCtx = formInfoCtx;
	}
}
