package uap.workflow.app.participant;
/**
 * 参与者类型接口 参与者设计期时的参与者类型接口，具体业务从此接口实现自己的具体限定模式类型类
 * 
 * @author
 */
public interface IParticipantType {
	// /不支持修改
	// /参与者类型代码
	public String getCode();
	public void setCode(String code);
	// /多语的参与者类型名称
	public String getName();
	public void setName(String name);
}