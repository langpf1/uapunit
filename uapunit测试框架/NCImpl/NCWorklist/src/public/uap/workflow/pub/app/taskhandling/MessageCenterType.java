package uap.workflow.pub.app.taskhandling;
import uap.workflow.app.taskhandling.ITaskHandlingType;
/**
 * ��Ϣ���Ĺ�������������
 * 
 * @author
 */
public class MessageCenterType implements ITaskHandlingType {
	String code="MessageCenter";
	String name="��Ϣ����";

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