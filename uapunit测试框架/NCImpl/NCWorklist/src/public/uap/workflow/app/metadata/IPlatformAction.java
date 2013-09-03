package uap.workflow.app.metadata;

import nc.uap.pf.metadata.ActionParams;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;

/**
 * 平台动作处理接口
 * @author leijun 2008-3
 * @modify guowl 2010-12
 * @since 6.0
 */
public interface IPlatformAction {
	/**
	 * 动作处理
	 * @param ap
	 * @return 动作处理返回值
	 * @throws BusinessException
	 * @since 5.5
	 */
	public Object runAction(ActionParams ap) throws BusinessException;

	/**
	 * 动作批处理
	 * @param ap
	 * @return 动作批处理返回值
	 * @throws BusinessException
	 * @since 5.5
	 */
	public Object runBatch(ActionParams ap) throws BusinessException;

	/**
	 * @param ap
	 * @return
	 * @throws BusinessException
	 * @since 5.5
	 */
	public WorkflownoteVO getWorkitemOnSave(ActionParams ap) throws BusinessException;

	/**
	 * @param ap
	 * @return
	 * @throws BusinessException
	 * @since 5.5
	 */
	public WorkflownoteVO getWorkitemOnApprove(ActionParams ap) throws BusinessException;
}
