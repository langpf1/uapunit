package uap.workflow.engine.message;

import java.util.Map;


import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.logger.WorkflowLogger;

import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class TaskCreatedMsgCenterSender implements ITaskCreatedMessageSender {

	@Override
	public void sendTaskNewCreatedMessage(Map<String, Object> messageMap) {
		NCMessage ncmsg = new NCMessage();
		MessageVO messageVO = new MessageVO();
		messageVO.setMsgsourcetype("worklist");
		messageVO.setReceiver((String) messageMap.get(TaskMessageSenderMgr.ReceiverUser));
		messageVO.setSubject((String) messageMap.get(TaskMessageSenderMgr.FormTitle));
		messageVO.setSender((String) messageMap.get(TaskMessageSenderMgr.SenderUser));
		messageVO.setIshandled(UFBoolean.FALSE);
		TaskEntity taskEntity = (TaskEntity) messageMap.get(TaskMessageSenderMgr.CurrentTask);
		messageVO.setPk_detail(taskEntity.getTaskPk());
		messageVO.setSendtime(new UFDateTime());
		ncmsg.setMessage(messageVO);
		try {
			MessageCenter.sendMessage(new NCMessage[] { ncmsg });
		} catch (Exception e) {
			WorkflowLogger.error(e.getMessage(), e);
		}

	}

}
