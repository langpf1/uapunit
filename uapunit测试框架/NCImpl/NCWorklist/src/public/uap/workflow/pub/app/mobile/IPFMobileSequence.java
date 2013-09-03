package uap.workflow.pub.app.mobile;

import nc.vo.pub.BusinessException;

/**
 * 移动应用的ID产生器
 * <li>必须做成单点组件
 * <li>适配UFMobile SDK 2.0
 * 
 * @author leijun 2006-9-16
 */
public interface IPFMobileSequence {

	public long[] nextLongValue(String mobile, int iBatchSize) throws BusinessException;

	public String[] nextStringValue(String mobile, int iBatchSize, int iPaddingLength)
			throws BusinessException;
}
