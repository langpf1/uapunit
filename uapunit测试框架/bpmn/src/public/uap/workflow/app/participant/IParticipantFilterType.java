package uap.workflow.app.participant;
/**
 * �������޶�ģʽ���ͽӿ� �����������ʱ���޶�ģʽ���ͽӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ����޶�ģʽ������
 * 
 * @author
 */
public interface IParticipantFilterType {
	// /��֧���޸�
	// /�������޶�ģʽ���ʹ���
	public String getCode();
	public void setCode(String code);
	
	// /������޶�ģʽ��������
	public String getName();
	public void setName(String name);
}