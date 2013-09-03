package uap.workflow.pub.app.taskhandling;
import uap.workflow.app.taskhandling.ITaskHandlingType;
/**
 * 消息中心工作任务处理类型
 * 
 * @author
 */
public class MessageCenterType implements ITaskHandlingType {
	String code="MessageCenter";
	String name="消息中心";

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public void setCode(String code) 
	{
		this.code = code;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
}