package uap.workflow.app.notice;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;

/** 
   通知执行时的上下文类
 * @author 
 */
public class NoticeContext{
	//单据实体，一般为单据聚合VO
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