package uap.workflow.engine.extend.gadget;

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
	Object doAfterRunned(IGadgetContext gc);

	/**
	 * �����ʱ���������������ҵ������л���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoAfterRunned(IGadgetContext gc);

	/**
	 * �����ʱ��ִ�й����������ǰ�ô���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object doBeforeActive(IGadgetContext gc);

	/**
	 * �����ʱ���������������ǰ�ô�����л���
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoBeforeActive(IGadgetContext gc);
}
