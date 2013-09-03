package uap.workflow.pub.app.taskhandling;
import uap.workflow.app.taskhandling.ITaskHandlingType;
/**
 * 短消息工作任务处理类型
 * 
 * @author
 */
public class SMSType implements ITaskHandlingType {
	String code="SMS";
	String name="短消息";

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