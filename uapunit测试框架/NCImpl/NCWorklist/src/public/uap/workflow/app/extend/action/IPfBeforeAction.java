package uap.workflow.app.extend.action;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * ��������ǰ��ҵ����⴦��
 * <li>�������������ʵ�֣�ע����bd_billtype.checkclassname�ֶ�
 * <li>��Ҫ�����ڴ�����VOʱ����ҵ�����ṩ��ȫVO�Ļ���
 * 
 * @author guowl 2010-5
 */
public interface IPfBeforeAction {
	/**
	 * ƽ̨��������ǰ��ҵ����Ķ��⴦�� 
	 * @param hmPfExParams 
	 * @param userObj 
	 * @param reloadVO
	 * @param objAfterAction
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject beforeAction(Object billVO, Object userObj, HashMap hmPfExParams) throws BusinessException;

	/**
	 * ƽ̨�������������ǰ��ҵ����Ķ��⴦��
	 * @param hmPfExParams 
	 * @param userObjAry 
	 * @param reloadVOs
	 * @param objsAfterAction
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject[] beforeBatch(Object[] billVOs, Object[] userObjAry, HashMap hmPfExParams) throws BusinessException;

	/**
	 * ���ؿ�¡VO�������������������ֻ��һ��Ԫ�ص����顣
	 * @return
	 */
	AggregatedValueObject[] getCloneVO();
}
