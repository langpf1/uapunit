package uap.workflow.app.metadata;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 流程平台业务接口-查询单据主子VO数据
 * <li>由单据的元数据对象实现
 * 
 * @author leijun 2008-7
 * @since 5.5
 * @modifier leijun 2010-3 新增方法queryAggData
 */
public interface IHeadBodyQueryItf {
	/**
	 * 查询子表VO数组
	 * <li>不适用于多子表
	 * 
	 * @param billType 单据类型
	 * @param headPK 主表的PK
	 * @param whereString 子表的查询条件 
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject[] queryAllBodyData(String billType, String headPK,
			String whereString) throws BusinessException;

	/**
	 * 查询某几个子表的VO数组
	 * <li>适用于多子表
	 *  
	 * @param billType
	 * @param headPK
	 * @param tableCode2Where 子表编码-子表where条件
	 * @return
	 * @throws BusinessException
	 */
	HashMap<String, CircularlyAccessibleValueObject[]> queryBodyDataByCode(String billType,
			String headPK, HashMap<String, String> tableCode2Where) throws BusinessException;

	/**
	 * 根据Where条件查询主表VO数组
	 * 
	 * @param billType 单据类型
	 * @param whereString 主表过滤条件
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject[] queryAllHeadData(String billType, String whereString)
			throws BusinessException;

	/**
	 * 根据单据ID查询主表VO
	 * @param billType 单据类型
	 * @param billId 单据主表PK
	 * @return
	 * @throws BusinessException
	 */
	CircularlyAccessibleValueObject queryHeadData(String billType, String billId)
			throws BusinessException;

	/**
	 * 查询出整个单据聚合VO
	 * <li>可以包含多子表
	 * 
	 * @param billType 单据类型
	 * @param billId 单据主表PK
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject queryAggData(String billType, String billId) throws BusinessException;
}
