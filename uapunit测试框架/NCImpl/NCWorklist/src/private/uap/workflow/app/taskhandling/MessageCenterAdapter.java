package uap.workflow.app.taskhandling;

import nc.vo.pub.BusinessException;
import uap.workflow.app.taskhandling.ITaskHandlingAdapter;
import uap.workflow.app.taskhandling.TaskHandlingContext;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.PfMessageUtil;
import nc.bs.logging.Logger;


/** 
   消息中心工作任务处理实现类
 * @author 
 */
public class MessageCenterAdapter implements ITaskHandlingAdapter{

	public void send(TaskInstanceVO taskInstanceVO) {
		TaskInstanceVO[] taskInstanceVOs = new TaskInstanceVO[1];
		taskInstanceVOs[0] = taskInstanceVO;
		try {
			PfMessageUtil.sendMessageOfWorknoteBatch(taskInstanceVOs);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			// 异常吞掉？
		}
	}

	public void receive(TaskInstanceVO taskInstanceVO) {
		// TODO Auto-generated method stub
		
	}

}