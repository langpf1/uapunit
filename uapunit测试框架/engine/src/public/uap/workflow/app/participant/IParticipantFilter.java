package uap.workflow.app.participant;

import java.util.List;

/** 
 * �������޶�ģʽ�ӿ�
 * ����������ʱ���޶�ģʽ�ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ���������޶�ģʽ��
 * @author 
 */

public interface IParticipantFilter{

  /** 
        �����޶�ģʽ���˺󣬵õ������ߵ��û�����
   @return �����ߵ��û��б�
   */
  public List<String> filterUsers(ParticipantFilterContext context);
}