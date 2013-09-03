package uap.workflow.app.core;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
/**
 * 
 * @author tianchw
 * 
 */
public interface IFlowResponse extends IDynamicAttribute {
	IProcessInstance getProcessInstance();
	void setProcessInstance(IProcessInstance processInstance);
	ITask getCurrentTask();
	ITask[] getNewTasks();
}
