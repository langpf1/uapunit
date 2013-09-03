package uap.workflow.engine.filter;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.server.ProcessEngineExecutor;
/**
 * 
 * @author tianchw
 * 
 */
public class BpmnFlowFilter implements IFilter {
	public void doFilter(IFlowRequest request, IFlowResponse response, FilterChain chain) {
		new ProcessEngineExecutor(request, response).execute();
		chain.doFilter(request, response);
	}
}
