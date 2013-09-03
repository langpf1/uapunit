package uap.workflow.app.participant;

import java.util.List;

import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.Role;

/** 
   ����������ʱ�Ķ������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��������
   ��Ҫ�������ļ�������IParticipantService�ľ���ʵ�������ĸ�   
 * @author 
 */

public interface IParticipantService{

  /** 
         �õ������ߵ��û�����
   @return �����ߵ��û��б�
   */
  public List<String> getUsers(ParticipantContext participantContext);
 /**
  *�õ���ǰ��������ͬ�����͵��û�����
  *@return �������б� 
  */
  public List<Participant> getAllUsersbyType(Participant currentparticipant);
  /**
   * �õ���ǰ������������еĽ�ɫ
   * @return �������б�
   * */
  public List<Role> getRolesByType(Role currentparticipant);
}