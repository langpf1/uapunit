package uap.workflow.app.extend.action;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * 用于业务流程跳转时，单据的状态回写
 * <li>注册在bd_billtype2中
 * */
public interface IBusiBillStatusCallBack {
	
	/**
	 * 单据状态回写
	 * @param busitypepk 将要跳转的业务流程PK
	 * @return UFDateTime
	 * */
	public UFDateTime callCheckStatus(JumpStatusCallbackContext context) throws BusinessException;
	
}
