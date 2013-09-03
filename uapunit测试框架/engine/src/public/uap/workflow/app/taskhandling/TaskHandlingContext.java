package uap.workflow.app.taskhandling;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;

/** 
   工作任务执行时的上下文类
 * @author 
 */
public class TaskHandlingContext{
	//单据实体，一般为单据聚合VO
	Object billEntity;
	ITask[] tasks;
	IActivity activity;
	
	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

	public ITask[] getTasks() {
		return tasks;
	}

	public void setTasks(ITask[] tasks) {
		this.tasks = tasks;
	}

	public Object getBillEntity() {
		return billEntity;
	}

	public void setBillEntity(Object billEntity) {
		this.billEntity = billEntity;
	}	
}