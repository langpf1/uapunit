package uap.workflow.engine.dftimpl;
import java.util.ArrayList;
import java.util.List;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.common.ExtendAttributeSupport;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
/**
 * 
 * @author tianchw
 * 
 */
public class FlowResponse extends ExtendAttributeSupport implements IFlowResponse {
	private static final long serialVersionUID = 1L;
	private IProcessInstance processInstance = null;
	private ITask currentTask = null;
	private List<ITask> newTasks = new ArrayList<ITask>();
	public ITask getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(ITask currentTask) {
		this.currentTask = currentTask;
	}
	public ITask[] getNewTasks() {
		return newTasks.toArray(new ITask[0]);
	}
	public void addNewTask(ITask task) {
		this.newTasks.add(task);
	}

	public IProcessInstance getProcessInstance() {

		return processInstance;
	}

	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
}
