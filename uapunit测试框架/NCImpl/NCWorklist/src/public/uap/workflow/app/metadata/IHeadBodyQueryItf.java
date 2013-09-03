package uap.workflow.app.metadata;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ����ƽ̨ҵ��ӿ�-��ѯ��������VO����
 * <li>�ɵ��ݵ�Ԫ���ݶ���ʵ��
 * 
 * @author leijun 2008-7
 * @since 5.5
 * @modifier leijun 2010-3 ��������queryAggData
 */
public interface IHeadBodyQueryItf {
	/**
	 * ��ѯ�ӱ�VO����
	 * <li>�������ڶ��ӱ�
	 * 
	 * @param billType ��������
	 * @param headPK �����PK
	 * @param whereString �ӱ�Ĳ�ѯ���� 
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject[] queryAllBodyData(String billType, String headPK,
			String whereString) throws BusinessException;

	/**
	 * ��ѯĳ�����ӱ��VO����
	 * <li>�����ڶ��ӱ�
	 *  
	 * @param billType
	 * @param headPK
	 * @param tableCode2Where �ӱ����-�ӱ�where����
	 * @return
	 * @throws BusinessException
	 */
	HashMap<String, CircularlyAccessibleValueObject[]> queryBodyDataByCode(String billType,
			String headPK, HashMap<String, String> tableCode2Where) throws BusinessException;

	/**
	 * ����Where������ѯ����VO����
	 * 
	 * @param billType ��������
	 * @param whereString �����������
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject[] queryAllHeadData(String billType, String whereString)
			throws BusinessException;

	/**
	 * ���ݵ���ID��ѯ����VO
	 * @param billType ��������
	 * @param billId ��������PK
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject queryHeadData(String billType, String billId)
			throws BusinessException;

	/**
	 * ��ѯ���������ݾۺ�VO
	 * <li>���԰������ӱ�
	 * 
	 * @param billType ��������
	 * @param billId ��������PK
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject queryAggData(String billType, String billId) throws BusinessException;
}
