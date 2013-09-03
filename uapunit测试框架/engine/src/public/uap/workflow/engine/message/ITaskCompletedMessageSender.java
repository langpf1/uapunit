package uap.workflow.engine.message;
import java.util.Map;
public interface ITaskCompletedMessageSender {
	// ���������չ��
	public static final String TaskCompleted = "taskcompleted";
	// ���������
	public static final String TaskCompletedEmail = "taskcompletedemail";
	public static final String TaskCompletedInnerMess = "taskcompletedinner";
	public static final String TaskCompletedOutLook = "taskcompletedoutlook";
	// �������������Ϣ�ӿ�
	public void sendTaskCompletedMessage(Map<String, Object> messageMap);
}
