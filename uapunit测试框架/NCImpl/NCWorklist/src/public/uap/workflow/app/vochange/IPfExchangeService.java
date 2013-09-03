package uap.workflow.app.vochange;

import nc.vo.pf.change.ExchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * VO转换服务类
 * @author guowl
 *
 */
public interface IPfExchangeService {

	/**
	 * 执行VO转换
	 * @param srcBillOrTranstype：来源交易类型或单据类型
	 * @param destBillOrTranstype：目的交易类型或单据类型
	 * @param srcBillVO：来源单据VO
	 * @param paraVo
	 * @return 生成的目的单据VO
	 * @throws BusinessException
	 */
	public AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject srcBillVO, PfParameterVO paraVo) throws BusinessException;
	
	/**
	 * 批量执行VO转换
	 * @param srcBillOrTranstype：来源交易类型或单据类型
	 * @param destBillOrTranstype：目的交易类型或单据类型
	 * @param sourceBillVOs：来源单据VO数组
	 * @param srcParaVo
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO srcParaVo) throws BusinessException;
	
	/**
	 * 批量执行VO转换
	 * @param srcBillOrTranstype：来源交易类型或单据类型
	 * @param destBillOrTranstype：目的交易类型或单据类型
	 * @param sourceBillVOs：来源单据VO数组
	 * @param srcParaVo
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject[] runChangeDataAryNeedClassify(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO srcParaVo, int classifyMode) throws BusinessException;
	
	/**
	 * 查找最合适的VO转换规则
	 * @param srcType：来源交易类型或单据类型
	 * @param destType：目的交易类型或单据类型
	 * @param pkBusitype：业务流程
	 * @param pkgroup：集团
	 * @param excludeVO
	 * @return
	 * @throws BusinessException
	 */
	public ExchangeVO queryMostSuitableExchangeVO(String srcType, String destType,
			String pkBusitype, String pkgroup, ExchangeVO excludeVO) throws BusinessException;
	
	/**
	 * 从数据库读取VO交换规则
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
