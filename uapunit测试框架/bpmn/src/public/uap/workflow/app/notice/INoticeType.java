package uap.workflow.app.notice;
/**
 * ֪ͨ���ͽӿ�
 * �����ʱ��֪ͨ���ͽӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ���֪ͨ������
 * ����֧���ʼ�������Ϣ����Ϣ���ģ��Ժ������չ������֧�ּ�ʱ��Ϣ��
 * @author
 */
public interface INoticeType {
	//��֧���޸�
	//֪ͨ���ʹ���
	public String getCode();
	public void setCode(String code);
	
	//�����֪ͨ��������
	public String getName();
	public void setName(String name);
}