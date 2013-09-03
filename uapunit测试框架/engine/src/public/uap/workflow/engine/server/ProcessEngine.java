package uap.workflow.engine.server;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.filter.FilterChain;
public class ProcessEngine implements IProcessEngine {
	public void execute(IFlowRequest reqeust, IFlowResponse response) {
		new FilterChain().doFilter(reqeust, response);
	}
}
