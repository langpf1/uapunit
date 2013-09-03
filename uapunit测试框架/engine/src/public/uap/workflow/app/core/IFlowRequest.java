package uap.workflow.app.core;
/**
 * 
 * @author tianchw
 * 
 */
public interface IFlowRequest extends IDynamicAttribute {
	IBusinessKey getBusinessObject();
	FlowInfoCtx getFlowInfoCtx();
	void setBusinessObject(IBusinessKey businessObject);
	void setFlowInfoCtx(FlowInfoCtx flwInfoCtx);
}
