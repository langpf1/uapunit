package uap.workflow.app.extend.bizstate;

import nc.vo.pub.BusinessException;

/**
 * ����״̬�ص��ӿ�
 * <li>ע����bd_billtype.checkclassname�����������
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public interface ICheckStatusCallback {
	/**
	 * ����״̬��д
	 * @param cscc
	 * @throws BusinessException
	 */
	public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException;
 
}
