package uap.workflow.engine.message;
import java.util.Map;
//���������չ�㡣
public interface IProcessEndMessageSender {
	// ���������չ��
	public static final String ProcCompleted = "proccompleted";
	// ���������
	public static final String ProcCompletedEmail = "proccompletedemail";
	public static final String ProcCompletedInnerMess = "proccompletedinner";
	public static final String ProcCompletedOutLook = "proccompletedoutlook";
	// �������������Ϣ�ӿ�
	public void sendProcCompletedMessage(Map<String, Object> messageMap);
}
