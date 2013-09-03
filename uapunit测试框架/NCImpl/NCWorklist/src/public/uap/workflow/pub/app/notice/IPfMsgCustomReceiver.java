package uap.workflow.pub.app.notice;

import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.sm.UserVO;
import uap.workflow.app.notice.ReceiverVO;

/**
 * 自定义消息接收者 接口
 * <li>必须由public类来实现
 * 
 * @author leijun 2007-4-24
 */
public interface IPfMsgCustomReceiver {

	/**
	 * 创建待选接收者
	 * @return
	 */
	public ReceiverVO[] createReceivers();

	/**
	 * 把待选接收者 解析为用户
	 * @param receiverVO 待选接收者
	 * @param paravo 工作流参数VO，从中可以获取单据ID和单据类型
	 * @return
	 */
	public UserVO[] queryUsers(ReceiverVO receiverVO, PfParameterVO paravo);
}
