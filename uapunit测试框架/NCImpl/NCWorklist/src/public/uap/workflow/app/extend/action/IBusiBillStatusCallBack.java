package uap.workflow.app.extend.action;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * ����ҵ��������תʱ�����ݵ�״̬��д
 * <li>ע����bd_billtype2��
 * */
public interface IBusiBillStatusCallBack {
	
	/**
	 * ����״̬��д
	 * @param busitypepk ��Ҫ��ת��ҵ������PK
	 * @return UFDateTime
	 * */
	public UFDateTime callCheckStatus(JumpStatusCallbackContext context) throws BusinessException;
	
}
