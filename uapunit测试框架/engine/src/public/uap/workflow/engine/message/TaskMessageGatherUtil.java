package uap.workflow.engine.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.plugin.WfExtension;
import uap.workflow.engine.plugin.PluginManager;

public class TaskMessageGatherUtil {
	/**
	 * �ռ�������Ϣ
	 * 
	 * @param task
	 * @return
	 */
	public static Map<String, Object> getMessageMap(ITask task) {
		if (task == null) {
			return null;
		}
		Map<String, Object> messageMap = new HashMap<String, Object>();
		messageMap.put(TaskMessageSenderMgr.CurrentTask, task);
		messageMap.put(TaskMessageSenderMgr.ReceiverUser, ((TaskEntity) task).getOwner());
		messageMap.put(TaskMessageSenderMgr.SenderUser, ((TaskEntity) task).getCreater());
		messageMap.put(TaskMessageSenderMgr.DefaultSendMsgType, task.getExecution().getActivity().getMsgType());
		String title = ((TaskEntity) task).getTitle();
		messageMap.put(TaskMessageSenderMgr.FormTitle, StringUtils.isEmpty(title) ? "Untitle" : title);
		return messageMap;
	}

	/**
	 * �õ��½�������Ϣ��������
	 * 
	 * @return
	 */
	public static WfExtension[] getTaskCreatedSenderType() {
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskCreatedMessageSender.TaskNewCreated);
		return (WfExtension[]) exs.toArray(new WfExtension[0]);
	}

	/**
	 * " �õ���ʱ�������Ϣ��������
	 * 
	 * @return
	 */
	public static WfExtension[] getTaskOverTimeSenderType() {
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskOverTimeMessageSender.TaskOvertime);
		return (WfExtension[]) exs.toArray(new WfExtension[0]);
	}

	/**
	 * �õ�����������Ϣ��������
	 * 
	 * @return
	 */
	public static WfExtension[] getTaskCompletedSenderType() {
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskCompletedMessageSender.TaskCompleted);
		return (WfExtension[]) exs.toArray(new WfExtension[0]);
	}

	/**
	 * �õ��߰����Ϣ��������
	 * 
	 * @return
	 */
	public static WfExtension[] getTaskUregeSenderType() {
		List<WfExtension> exs = PluginManager.newIns().getExtensions(ITaskUregeMessageSender.TaskUrege);
		return (WfExtension[]) exs.toArray(new WfExtension[0]);
	}
}
