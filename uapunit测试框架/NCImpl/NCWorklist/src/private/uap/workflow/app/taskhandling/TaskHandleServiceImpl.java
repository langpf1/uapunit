package uap.workflow.app.taskhandling;

import nc.bs.logging.Logger;
import nc.vo.pub.BusinessException;
import uap.workflow.app.taskhandling.ITaskHandlingService;
import uap.workflow.app.taskhandling.TaskHandlingContext;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.vos.TaskInstanceVO;


/** 
 * 任务处理通知实现类
 * @author 
 */
public class TaskHandleServiceImpl implements ITaskHandlingService{

	public void send(TaskHandlingContext taskContext) {
		ITask[] tasks = taskContext.getTasks();
		IActivity activity = taskContext.getActivity();
		TaskInstanceVO taskInstanceVO;
		ITaskHandlingAdapter email = new EMailAdapter();
		ITaskHandlingAdapter sms = new SMSAdapter();
		ITaskHandlingAdapter message = new MessageCenterAdapter();
		TaskInstanceBridge taskInstanceBridge = new TaskInstanceBridge();
		for (int i = 0; i < tasks.length; i++) {
			taskInstanceVO = taskInstanceBridge.convertT2M(tasks[i]);
			email.send(taskInstanceVO);
			sms.send(taskInstanceVO);
			message.send(taskInstanceVO);
		}
	}

	@Override
	public void receive(TaskHandlingContext taskContext) {
		// TODO Auto-generated method stub
		
	}

}