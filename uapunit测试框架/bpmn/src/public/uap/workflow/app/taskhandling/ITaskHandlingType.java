package uap.workflow.app.taskhandling;
/**
 * �������������ͽӿ�
 * �����ʱ�Ĺ������������ͽӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��幤��������������
 * 
 * @author
 */
public interface ITaskHandlingType {
	// /��֧���޸�
	// /�������������ʹ���
	public String getCode();
	public void setCode(String code);
	// /����Ĺ�����������������
	public String getName();
	public void setName(String name);
}