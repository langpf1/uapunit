package uap.workflow.app.extend.bizstate;

import nc.vo.pub.BusinessException;

/**
 * 审批状态回调接口
 * <li>注册在bd_billtype.checkclassname审批流检查类
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public interface ICheckStatusCallback {
	/**
	 * 单据状态回写
	 * @param cscc
	 * @throws BusinessException
	 */
	public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException;
 
}
