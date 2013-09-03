package uap.workflow.app.participant;

import java.util.List;

/**
 * 参与者适配器接口
 * 参与者运行时的适配器接口，具体业务从此接口实现自己的具体参与者适配类
 * @author 
 */

public interface IParticipantAdapter{

  /** 
        得到参与者的用户集合
   @return 参与者的用户列表
   */
  public List<String> findUsers(ParticipantContext participantContext) throws ParticipantException;
  
  public void checkValidity(ParticipantContext context) throws ParticipantException;
  
}