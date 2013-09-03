package uap.workflow.app.action;

import java.util.HashMap;

import uap.workflow.vo.WorkflownoteVO;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.sm.UserVO;

/**
 * ����ƽ̨��������Ĺ�����ڽӿ�.
 * �����������:
 *		try {
 *			����
 *			У��ts
 *			ҵ����
 *			}
 *		finally {
 *  		����
 *		}
 *	���ǻᷢ��ִ�й�������
 *			�õ�����
 *		����
 *		У��ts
 *		ҵ����
 *		����
 *		�ύ���߻ع�����
 *		 
 *			�������в�������ʱ�������һ�ţ���һ�������Ƚ���������û���ύ����
 *			��һ�����������ɹ������ڵ�һ������û���ύ��ts���Ҳ��ͨ������������
 *			����������ɹ���
 * 
 * @author ���ھ� 2006-5-26  
 * @modifier leijun 2008-2-1 �����ӿڷ���
 */
public interface IplatFormEntry {
	/**
	 * ����ƽ̨���еĵ��ݶ�������������.
	 * <li>���ݼ�����һ���Լ��
	 * <li>����ִ��IPFBusiAction
	 * <li>�ýӿ���Զ��public�ӿڣ�������Ϊ�˱�֤���������һ���ԣ����ṩ�ýӿ�
	 * <li>ƽ̨Ĭ�ϵ��øýӿ�
	 * 
	 * @param actionName �������룬���硰SAVE������APPROVE��
	 * @param billType ��������PK
	 * @param currentDate ��ǰ����
	 * @param worknoteVO ������VO
	 * @param billvo ���ݾۺ�VO
	 * @param userObj �û�����
	 * @param eParam ��������
	 * @return ����������ֵ
	 * @throws BusinessException
	 */
	public Object processAction(String actionName, String billType, WorkflownoteVO worknoteVO,
			AggregatedValueObject billvo, Object userObj, HashMap eParam) throws BusinessException;

	/**
	 * ����ƽ̨���еĵ��ݶ���������������.
	 * <li>���ݼ�����һ���Լ��
	 * <li>����ִ��IPFBusiAction
	 * <li>�ýӿ���Զ��public�ӿڣ�������Ϊ�˱�֤���������һ���ԣ����ṩ�ýӿ�
	 * <li>ƽ̨Ĭ�ϵ��øýӿ�
	 * @param actionName
	 * @param billType
	 * @param currentDate
	 * @param worknoteVO
	 * @param billvos
	 * @param userObjAry
	 * @param eParam
	 * @param retError 
	 * @return
	 * @throws BusinessException
	 */
	public Object processBatch(String actionName, String billType, WorkflownoteVO worknoteVO,
			AggregatedValueObject[] billvos, Object[] userObjAry, HashMap eParam)
			throws BusinessException;
	
	/**
	 * ��������ͬ���������µĶ������
	 * @param actionName ����
	 * @param worknoteVO �����������
	 * @param billTypes ��������
	 * @param billIds ����id����, ÿ��Ԫ����Ҫ�뵥�����������е���Ӧλ�ö�Ӧ
	 * @return
	 * @throws BusinessException
	 */
	public Object processBatch(String actionName, WorkflownoteVO worknoteVO, String[] billTypes, String[] billIds)
			throws BusinessException;

	/**
	 * ���ݵ���ID��ѯ��Ч�������
	 * 
	 * @param billId
	 * @param billType
	 * @return
	 * @throws BusinessException
	 */
	//xry TODO:public UserVO[] queryValidCheckers(String billId, String billType) throws BusinessException;

	/**
	 * ��ѯĳҵ�������£�ĳ���ݶ������������е��ݶ���
	 * 
	 * @param billType
	 * @param busiType
	 * @param pkCorp
	 * @param actionName
	 * @return
	 * @throws BusinessException
	 */
	public PfUtilActionVO[] getActionDriveVOs(String billType, String busiType, String pkCorp,
			String actionName) throws BusinessException;

}
