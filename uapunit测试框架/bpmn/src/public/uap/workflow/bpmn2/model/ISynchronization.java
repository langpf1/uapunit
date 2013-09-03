package uap.workflow.bpmn2.model;
/**
 * 通知类型接口
 * 设计期时的通知类型接口，具体业务从此接口实现自己的具体通知类型类
 * 现在支持邮件，短消息，消息中心，以后可以扩展，比如支持即时消息，
 * @author
 */
public interface ISynchronization {
	public void marshal();
	public void unmarshal();
}