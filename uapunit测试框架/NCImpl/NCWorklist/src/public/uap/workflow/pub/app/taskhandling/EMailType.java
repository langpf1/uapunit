package uap.workflow.pub.app.taskhandling;
import uap.workflow.app.taskhandling.ITaskHandlingType;
/**
 * �ʼ���������������
 * 
 * @author
 */
public class EMailType implements ITaskHandlingType {
	String code="EMail";
	String name="�ʼ�";
	
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