package uap.workflow.pub.app.mobile;

import uap.workflow.pub.app.mobile.vo.ReceivedSmsVO;


/**
 * 短消息服务接口
 * <li>具体实现类必须注册在ierp/bin/mobileplugin.xml文件中
 * 
 * @author leijun 2003-10-15
 * @modifier leijun 2007-3-22 增加批量发送的方法
 * @modifier ewei 2007-9-24 增加向不同的号码组发不同的消息的方法
 */
public abstract class ShortMessageService {

	public ShortMessageService() {
		super();
	}

	/**
	 * 初始化环境变量或短信发送设备等
	 */
	public abstract boolean initialize();

	/**
	 * 发送短信
	 * @param targetPhone 目的手机号
	 * @param msg 消息内容
	 */
	public abstract Object sendMessage(String targetPhone, String msg);

	/**
	 * 发送短信，带会话ID
	 * @param targetPhone 目的手机号
	 * @param msg 消息内容
	 * @param sid 会话ID，与手机号对应
	 */
	public abstract Object sendMessage(String targetPhone, String msg, String sid);

	/**
	 * 批量发送短信
	 * @param targetPhones 目的手机号数组
	 * @param msg 消息内容
	 */
	public abstract Object sendMessages(String[] targetPhones, String msg);

	/**
	 * 批量发送短信，带会话ID数组
	 * @param targetPhones 目的手机号数组
	 * @param msg 消息内容
	 * @param sids 会话ID数组，与手机号对应
	 */
	public abstract Object sendMessages(String[] targetPhones, String msg, String[] sids);

	/**
	 * 向不同的号码组发不同的消息
	 * @param targetPhones
	 * @param msg
	 */
	public abstract Object sendMessages(String[][] targetPhones, String[] msg);

	/**
	 * 批量接收短信
	 * @return
	 */
	public abstract ReceivedSmsVO[] receiveMessages();
}
