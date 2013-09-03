package uap.workflow.engine.message;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.plugin.PluginManager;
import uap.workflow.engine.plugin.WfExtension;
/**
 * 任务消息发送
 * 
 * @author tianchw
 * 
 */
public class TaskMessageSenderMgr {
	// 消息接收人
	public static String ReceiverUser = "ReceiverUser";
	// 流程名称
	public static String ProcessDefinitionName = "ProcessDefinitionName";
	// 活动名称
	public static String ActivityName = "ActivityName";
	// 单据标题
	public static String FormTitle = "FormTitle";
	// 任务开始时间
	public static String TaskStartDate = "TaskStartDate";
	// 发送人
	public static String SenderUser = "SenderUser";
	// 默认发送类型
	public static String DefaultSendMsgType = "DefaultSendMsgType";
	// 前端控制项
	public static String FrontControlType = "FrontControlType";
	// HTML格式的邮件内容
	public static String HtmlContent = "FrmContent";
	// 附件名称
	public static String AttachFile = "AttachFile";
	// 接受者邮件
	public static final String ReceiveEmails = "ReceiveEmails";
	// 当前任务
	public static final String CurrentTask = "CurrentTask";
	// 新创建任务信息发送
	public static void sendTaskCreatedMessage(Map<String, Object> messageMap) {
		if (messageMap == null) {
			return;
		}
		MsgType[] defaultSendedType = (MsgType[]) messageMap.get(TaskMessageSenderMgr.DefaultSendMsgType);
		if (defaultSendedType == null) {
			return;
		}
		List<MsgType> senderTypeList = Arrays.asList(defaultSendedType);
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskCreatedMessageSender.TaskNewCreated);
		if (exs == null || exs.size() == 0) {
			return;
		}
		for (WfExtension ex : exs) {
			String exId = ex.getId();
			if (!senderTypeList.contains(MsgType.getMsgType(exId))) {
				continue;
			}
			MsgType[] msgTypes = (MsgType[]) messageMap.get(TaskMessageSenderMgr.FrontControlType);
			if (msgTypes == null || msgTypes.length == 0) {
				continue;
			}
			List<MsgType> msgType = Arrays.asList(msgTypes);
			if (msgType != null && !msgType.contains(MsgType.getMsgType(exId))) {
				continue;
			}
			ITaskCreatedMessageSender taskCreatedService = null;
			try {
				taskCreatedService = (ITaskCreatedMessageSender) ex.newInstance();
				taskCreatedService.sendTaskNewCreatedMessage(messageMap);
			} catch (Exception e) {
				throw new WorkflowRuntimeException(e);
			}
		}
	}
	/**
	 * 任务完成发送消息
	 * 
	 * @param messageMap
	 */
	public static void sendTaskCompletedMessage(Map<String, Object> messageMap) {
		if (messageMap == null) {
			return;
		}
		String[] defaultSendedType = (String[]) messageMap.get(TaskMessageSenderMgr.DefaultSendMsgType);
		List<String> senderTypeList = Arrays.asList(defaultSendedType);
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskCompletedMessageSender.TaskCompleted);
		if (CollectionUtils.isNotEmpty(exs)) {
			for (WfExtension ex : exs) {
				String exId = ex.getId();
				if (senderTypeList.contains(exId)) {
					ITaskCompletedMessageSender taskCompletedService = null;
					try {
						taskCompletedService = (ITaskCompletedMessageSender) ex.newInstance();
					} catch (Exception e) {}
					if (taskCompletedService != null) {
						taskCompletedService.sendTaskCompletedMessage(messageMap);
					}
				}
			}
		}
	}
	/**
	 * 任务超时消息
	 * 
	 * @param messageMap
	 */
	public static void sendOverTiemMessage(Map<String, Object> messageMap) {
		if (messageMap == null) {
			return;
		}
		String[] defaultSendedType = (String[]) messageMap.get(TaskMessageSenderMgr.DefaultSendMsgType);
		List<String> senderTypeList = Arrays.asList(defaultSendedType);
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskOverTimeMessageSender.TaskOvertime);
		if (CollectionUtils.isNotEmpty(exs)) {
			for (WfExtension ex : exs) {
				String exId = ex.getId();
				if (senderTypeList.contains(exId)) {
					ITaskOverTimeMessageSender taskOverTimeService = null;
					try {
						taskOverTimeService = (ITaskOverTimeMessageSender) ex.newInstance();
					} catch (Exception e) {}
					taskOverTimeService.sendTaskOverTimeMessage(messageMap);
				}
			}
		}
	}
	/**
	 * 任务催办消息
	 * 
	 * @param messageMap
	 */
	public static void sendUergeMessage(Map<String, Object> messageMap) {
		if (messageMap == null) {
			return;
		}
		String[] defaultSendedType = (String[]) messageMap.get(TaskMessageSenderMgr.DefaultSendMsgType);
		List<String> senderTypeList = Arrays.asList(defaultSendedType);
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskUregeMessageSender.TaskUrege);
		if (CollectionUtils.isNotEmpty(exs)) {
			for (WfExtension ex : exs) {
				String exId = ex.getId();
				if (senderTypeList.contains(exId)) {
					ITaskUregeMessageSender taskOverTimeService = null;
					try {
						taskOverTimeService = (ITaskUregeMessageSender) ex.newInstance();
					} catch (Exception e) {}
					taskOverTimeService.sendTaskUregeMessage(messageMap);
				}
			}
		}
	}
}
