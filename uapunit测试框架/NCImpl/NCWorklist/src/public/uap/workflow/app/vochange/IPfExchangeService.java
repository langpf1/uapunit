package uap.workflow.app.vochange;

import nc.vo.pf.change.ExchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * VOת��������
 * @author guowl
 *
 */
public interface IPfExchangeService {

	/**
	 * ִ��VOת��
	 * @param srcBillOrTranstype����Դ�������ͻ򵥾�����
	 * @param destBillOrTranstype��Ŀ�Ľ������ͻ򵥾�����
	 * @param srcBillVO����Դ����VO
	 * @param paraVo
	 * @return ���ɵ�Ŀ�ĵ���VO
	 * @throws BusinessException
	 */
	public AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject srcBillVO, PfParameterVO paraVo) throws BusinessException;
	
	/**
	 * ����ִ��VOת��
	 * @param srcBillOrTranstype����Դ�������ͻ򵥾�����
	 * @param destBillOrTranstype��Ŀ�Ľ������ͻ򵥾�����
	 * @param sourceBillVOs����Դ����VO����
	 * @param srcParaVo
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO srcParaVo) throws BusinessException;
	
	/**
	 * ����ִ��VOת��
	 * @param srcBillOrTranstype����Դ�������ͻ򵥾�����
	 * @param destBillOrTranstype��Ŀ�Ľ������ͻ򵥾�����
	 * @param sourceBillVOs����Դ����VO����
	 * @param srcParaVo
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject[] runChangeDataAryNeedClassify(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO srcParaVo, int classifyMode) throws BusinessException;
	
	/**
	 * ��������ʵ�VOת������
	 * @param srcType����Դ�������ͻ򵥾�����
	 * @param destType��Ŀ�Ľ������ͻ򵥾�����
	 * @param pkBusitype��ҵ������
	 * @param pkgroup������
	 * @param excludeVO
	 * @return
	 * @throws BusinessException
	 */
	public ExchangeVO queryMostSuitableExchangeVO(String srcType, String destType,
			String pkBusitype, String pkgroup, ExchangeVO excludeVO) throws BusinessException;
	
	/**
	 * �����ݿ��ȡVO��������
	 * 
	 * @param srcBillOrTranstype
	 * @param destBillOrTranstype
	 * @param sourceBillVO
	 * @param pk_group
	 * @param bmc
	 * @throws BusinessException
	 */
	public ExchangeVO findVOConversionFromDB(String srcBillOrTranstype, String destBillOrTranstype,
			AggregatedValueObject sourceBillVO, String pk_group) throws BusinessException;
	
}
