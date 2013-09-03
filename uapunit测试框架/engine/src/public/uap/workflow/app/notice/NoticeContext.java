package uap.workflow.app.notice;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;

/** 
   ִ֪ͨ��ʱ����������
 * @author 
 */
public class NoticeContext{
	//����ʵ�壬һ��Ϊ���ݾۺ�VO
	Object billEntity;
	INoticeDefinition[] noticeDefs;
	ITask[] tasks;
	IActivity activity;
	
	public INoticeDefinition[] getNotice() {
		return noticeDefs;
	}

	public void setNotice(INoticeDefinition[] notices) {
		this.noticeDefs = notices;
	}

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