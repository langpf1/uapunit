package uap.workflow.app.participant;
/**
 * ���������ͽӿ� �����������ʱ�Ĳ��������ͽӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ����޶�ģʽ������
 * 
 * @author
 */
public interface IParticipantType {
	// /��֧���޸�
	// /���������ʹ���
	public String getCode();
	public void setCode(String code);
	// /����Ĳ�������������
	public String getName();
	public void setName(String name);
}