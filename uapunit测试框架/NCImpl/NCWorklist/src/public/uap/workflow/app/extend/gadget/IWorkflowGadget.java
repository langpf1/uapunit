package uap.workflow.app.extend.gadget;

import nc.vo.pub.BusinessException;

/**
 * ����������ӿڣ���״̬��
 * <li>��ҵ������Private��ʵ�֣���ע�ᵽpub_workflowgadget����
 * 
 * @author leijun 2008-8
 * @since 5.5
 * @modifier leijun 2009-6 ��������������UI
 * @modifier leijun 2009-12 ����ǰ�ô�������˵��÷���
 */
public interface IWorkflowGadget {
	/**
	 * ����ʱ��ִ�й����������ҵ����
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object doAfterRunned(WfGadgetContext gc) throws BusinessException;

	/**
	 * �����ʱ���������������ҵ������л���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoAfterRunned(WfGadgetContext gc) throws BusinessException;

	/**
	 * �����ʱ��ִ�й����������ǰ�ô���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object doBeforeActive(WfGadgetContext gc) throws BusinessException;

	/**
	 * �����ʱ���������������ǰ�ô�����л���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoBeforeActive(WfGadgetContext gc) throws BusinessException;
}
