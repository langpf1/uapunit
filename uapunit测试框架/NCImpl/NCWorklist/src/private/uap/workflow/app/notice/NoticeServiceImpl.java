package uap.workflow.app.notice;

import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.vos.TaskInstanceVO;


/** 
   通知实现类
 * @author 
 */
public class NoticeServiceImpl implements INoticeService{

	@Override
	public void sendNotice(NoticeContext noticeContext) {
		ITask[] tasks = noticeContext.getTasks();
		IActivity activity = noticeContext.getActivity();
		TaskInstanceVO taskInstanceVO;
		INoticeAdapter email = new EMailNoticeAdapter();
		INoticeAdapter sms = new SMSNoticeAdapter();
		INoticeAdapter message = new MessageCenterNoticeAdapter();
		TaskInstanceBridge taskInstanceBridge = new TaskInstanceBridge();
		for (int i = 0; i < tasks.length; i++) {
			taskInstanceVO = taskInstanceBridge.convertT2M(tasks[i]);
			email.sendNotice(taskInstanceVO, null, null);
			sms.sendNotice(taskInstanceVO, null, null);
			message.sendNotice(taskInstanceVO, null, null);
		}
	}

}