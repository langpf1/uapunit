package uap.workflow.app.taskhandling;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;

/** 
   ��������ִ��ʱ����������
 * @author 
 */
public class TaskHandlingContext{
	//����ʵ�壬һ��Ϊ���ݾۺ�VO
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