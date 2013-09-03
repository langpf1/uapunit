package uap.workflow.app.extend.action;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * �������������ҵ����⴦��
 * <li>�������������ʵ�֣�ע����bd_billtype.checkclassname�ֶ�
 * 
 * @author leijun 2010-2
 */
public interface IPfAfterAction {
	/**
	 * ƽ̨����������ɺ�ҵ����Ķ��⴦�� 
	 * @param reloadVO
	 * @param objAfterAction
	 * @param hmPfExParams ��չ����
	 * @return
	 * @throws BusinessException
	 */
	Object afterAction(AggregatedValueObject reloadVO, Object objAfterAction, HashMap hmPfExParams)
			throws BusinessException;

	/**
	 * ƽ̨������������ɺ�ҵ����Ķ��⴦��
	 * @param reloadVOs
	 * @param objsAfterAction
	 * @param hmPfExParams ��չ����
	 * @return
	 * @throws BusinessException
	 */
	Object[] afterBatch(AggregatedValueObject[] reloadVOs, Object[] objsAfterAction, HashMap hmPfExParams) throws BusinessException;
}
