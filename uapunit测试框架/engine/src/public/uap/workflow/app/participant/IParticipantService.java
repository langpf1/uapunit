package uap.workflow.app.participant;

import java.util.List;

import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.Role;

/** 
   参与者运行时的对外服务接口，具体业务从此接口实现自己的具体服务类
   需要在配置文件中配置IParticipantService的具体实现类是哪个   
 * @author 
 */

public interface IParticipantService{

  /** 
         得到参与者的用户集合
   @return 参与者的用户列表
   */
  public List<String> getUsers(ParticipantContext participantContext);
 /**
  *得到当前集团下面同种类型的用户集合
  *@return 参与者列表 
  */
  public List<Participant> getAllUsersbyType(Participant currentparticipant);
  /**
   * 得到当前集团下面的所有的角色
   * @return 参与者列表
   * */
  public List<Role> getRolesByType(Role currentparticipant);
}