package uap.workflow.engine.filter;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
/**
 * 
 * @author tianchw
 * 
 */
public class BpmnFilter1 implements IFilter {
	public void doFilter(IFlowRequest request, IFlowResponse response, FilterChain chain) {
		System.err.println("filter1=start");
		chain.doFilter(request, response);
		System.err.println("filter1=end");
	}
}
