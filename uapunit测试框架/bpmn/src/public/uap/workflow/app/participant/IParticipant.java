package uap.workflow.app.participant;

import java.util.Map;

/**
 * 参与者接口 
 * 参与者设计期时的接口
 * @author 
 */

public interface IParticipant{
  ///标识符
  public String getID();
  public void setID(String ID);

  ///实际参与者的标识符,如，参与者是角色，就记录具体角色的ID
  public String getParticipantID();
  public void setParticipantID(String participantId);

  ///参与者的代码
  public String getCode();
  public void setCode(String code);

  ///参与者的名称
  public String getName();
  public void setName(String name);
  
  ///参与者的类型
  public IParticipantType getParticipantType();
  public void setParticipantType(IParticipantType participantType);
  
  ///参与者的限定模式类型
  public IParticipantFilterType getParticipantFilterType();
  public void setParticipantFilterType(IParticipantFilterType participantFilterType);

  ///取参与者的所有扩展属性
  public Map<String,Object> getProperties();

  ///取参与者的扩展属性
  public Object getProperty(String key);

  ///设置参与者的扩展属性
  public void setProperty(String key, Object value);
  
  public IParticipant clone();
}