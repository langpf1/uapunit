package uap.workflow.engine.message;
import java.util.Map;
//流程完成扩展点。
public interface IProcessEndMessageSender {
	// 完成任务扩展点
	public static final String ProcCompleted = "proccompleted";
	// 完成任务插件
	public static final String ProcCompletedEmail = "proccompletedemail";
	public static final String ProcCompletedInnerMess = "proccompletedinner";
	public static final String ProcCompletedOutLook = "proccompletedoutlook";
	// 发送任务完成信息接口
	public void sendProcCompletedMessage(Map<String, Object> messageMap);
}
