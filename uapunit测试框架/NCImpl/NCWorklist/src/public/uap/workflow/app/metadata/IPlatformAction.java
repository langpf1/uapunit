package uap.workflow.app.metadata;

import nc.uap.pf.metadata.ActionParams;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;

/**
 * ƽ̨��������ӿ�
 * @author leijun 2008-3
 * @modify guowl 2010-12
 * @since 6.0
 */
public interface IPlatformAction {
	/**
	 * ��������
	 * @param ap
	 * @return ����������ֵ
	 * @throws BusinessException
	 * @since 5.5
	 */
	public Object runAction(ActionParams ap) throws BusinessException;

	/**
	 * ����������
	 * @param ap
	 * @return ������������ֵ
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
