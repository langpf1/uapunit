package uap.workflow.app.notice;
/**
 * 通知类型接口
 * 设计期时的通知类型接口，具体业务从此接口实现自己的具体通知类型类
 * 现在支持邮件，短消息，消息中心，以后可以扩展，比如支持即时消息，
 * @author
 */
public interface INoticeType {
	//不支持修改
	//通知类型代码
	public String getCode();
	public void setCode(String code);
	
	//多语的通知类型名称
	public String getName();
	public void setName(String name);
}