package uap.workflow.engine.message;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.plugin.PluginManager;
import uap.workflow.engine.plugin.WfExtension;
/**
 * ������Ϣ����
 * 
 * @author tianchw
 * 
 */
public class TaskMessageSenderMgr {
	// ��Ϣ������
	public static String ReceiverUser = "ReceiverUser";
	// ��������
	public static String ProcessDefinitionName = "ProcessDefinitionName";
	// �����
	public static String ActivityName = "ActivityName";
	// ���ݱ���
	public static String FormTitle = "FormTitle";
	// ����ʼʱ��
	public static String TaskStartDate = "TaskStartDate";
	// ������
	public static String SenderUser = "SenderUser";
	// Ĭ�Ϸ�������
	public static String DefaultSendMsgType = "DefaultSendMsgType";
	// ǰ�˿�����
	public static String FrontControlType = "FrontControlType";
	// HTML��ʽ���ʼ�����
	public static String HtmlContent = "FrmContent";
	// ��������
	public static String AttachFile = "AttachFile";
	// �������ʼ�
	public static final String ReceiveEmails = "ReceiveEmails";
	// ��ǰ����
	public static final String CurrentTask = "CurrentTask";
	// �´���������Ϣ����
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
	 * ������ɷ�����Ϣ
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
	 * ����ʱ��Ϣ
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
	 * ����߰���Ϣ
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
