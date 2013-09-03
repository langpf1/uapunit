package uap.workflow.app.participant;

import java.util.List;

/** 
 * 参与者限定模式接口
 * 参与者运行时的限定模式接口，具体业务从此接口实现自己的具体参与者限定模式类
 * @author 
 */

public interface IParticipantFilter{

  /** 
        经过限定模式过滤后，得到参与者的用户集合
   @return 参与者的用户列表
   */
  public List<String> filterUsers(ParticipantFilterContext context);
}