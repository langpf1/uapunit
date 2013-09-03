package uap.workflow.pub.app.notice;

import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.sm.UserVO;
import uap.workflow.app.notice.ReceiverVO;

/**
 * �Զ�����Ϣ������ �ӿ�
 * <li>������public����ʵ��
 * 
 * @author leijun 2007-4-24
 */
public interface IPfMsgCustomReceiver {

	/**
	 * ������ѡ������
	 * @return
	 */
	public ReceiverVO[] createReceivers();

	/**
	 * �Ѵ�ѡ������ ����Ϊ�û�
	 * @param receiverVO ��ѡ������
	 * @param paravo ����������VO�����п��Ի�ȡ����ID�͵�������
	 * @return
	 */
	public UserVO[] queryUsers(ReceiverVO receiverVO, PfParameterVO paravo);
}
