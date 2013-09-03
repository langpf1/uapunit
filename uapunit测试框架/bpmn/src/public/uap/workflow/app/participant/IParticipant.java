package uap.workflow.app.participant;

import java.util.Map;

/**
 * �����߽ӿ� 
 * �����������ʱ�Ľӿ�
 * @author 
 */

public interface IParticipant{
  ///��ʶ��
  public String getID();
  public void setID(String ID);

  ///ʵ�ʲ����ߵı�ʶ��,�磬�������ǽ�ɫ���ͼ�¼�����ɫ��ID
  public String getParticipantID();
  public void setParticipantID(String participantId);

  ///�����ߵĴ���
  public String getCode();
  public void setCode(String code);

  ///�����ߵ�����
  public String getName();
  public void setName(String name);
  
  ///�����ߵ�����
  public IParticipantType getParticipantType();
  public void setParticipantType(IParticipantType participantType);
  
  ///�����ߵ��޶�ģʽ����
  public IParticipantFilterType getParticipantFilterType();
  public void setParticipantFilterType(IParticipantFilterType participantFilterType);

  ///ȡ�����ߵ�������չ����
  public Map<String,Object> getProperties();

  ///ȡ�����ߵ���չ����
  public Object getProperty(String key);

  ///���ò����ߵ���չ����
  public void setProperty(String key, Object value);
  
  public IParticipant clone();
}