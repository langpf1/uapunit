package uap.workflow.engine.filter;
import java.util.List;

import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.server.BizProcessServer;
/**
 * 
 * @author tianchw
 *
 */
public class FilterChain {
	private List<IFilter> filters = BizProcessServer.getInstance().getFilters();
	private int index = 0;
	public void addFilter(IFilter f) {
		filters.add(f);
	}
	public void doFilter(IFlowRequest request, IFlowResponse response) {
		if (index == filters.size()) {
			return;
		}
		IFilter filter = filters.get(index);
		index++;
		filter.doFilter(request, response, this);
	}
}
