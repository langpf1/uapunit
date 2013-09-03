package uap.workflow.app.participant;

import java.util.List;

/**
 * �������������ӿ�
 * ����������ʱ���������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��������������
 * @author 
 */

public interface IParticipantAdapter{

  /** 
        �õ������ߵ��û�����
   @return �����ߵ��û��б�
   */
  public List<String> findUsers(ParticipantContext participantContext) throws ParticipantException;
  
  public void checkValidity(ParticipantContext context) throws ParticipantException;
  
}