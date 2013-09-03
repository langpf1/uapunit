package uap.workflow.engine.server;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
public interface IProcessEngine {
	void execute(IFlowRequest reqeust, IFlowResponse response);
}
