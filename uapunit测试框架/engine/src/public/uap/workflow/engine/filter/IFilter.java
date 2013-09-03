package uap.workflow.engine.filter;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
/**
 * 
 * @author tianchw
 *
 */
public interface IFilter {
	public void doFilter(IFlowRequest request, IFlowResponse response, FilterChain chain);
}