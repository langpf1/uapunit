package uap.workflow.app.action;

import java.util.HashMap;

import uap.workflow.vo.WorkflownoteVO;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.sm.UserVO;

/**
 * 流程平台动作处理的公共入口接口.
 * 解决如下问题:
 *		try {
 *			加锁
 *			校验ts
 *			业务动作
 *			}
 *		finally {
 *  		解锁
 *		}
 *	我们会发现执行过程如下
 *			得到事务
 *		加锁
 *		校验ts
 *		业务动作
 *		解锁
 *		提交或者回滚事务
 *		 
 *			这样进行并发操作时，（针对一张）有一个动作先解锁，但是没有提交事务，
 *			另一个动作加锁成功，由于第一个事务没有提交，ts检查也能通过。这样会造
 *			成两个都会成功。
 * 
 * @author 樊冠军 2006-5-26  
 * @modifier leijun 2008-2-1 新增接口方法
 */
public interface IplatFormEntry {
	/**
	 * 流程平台进行的单据动作处理的入口类.
	 * <li>数据加锁和一致性检查
	 * <li>动作执行IPFBusiAction
	 * <li>该接口是远程public接口，无事务，为了保证锁和事务的一致性，特提供该接口
	 * <li>平台默认调用该接口
	 * 
	 * @param actionName 动作编码，比如“SAVE”、“APPROVE”
	 * @param billType 单据类型PK
	 * @param currentDate 当前日期
	 * @param worknoteVO 工作项VO
	 * @param billvo 单据聚合VO
	 * @param userObj 用户对象
	 * @param eParam 环境参数
	 * @return 动作处理返回值
	 * @throws BusinessException
	 */
	public Object processAction(String actionName, String billType, WorkflownoteVO worknoteVO,
			AggregatedValueObject billvo, Object userObj, HashMap eParam) throws BusinessException;

	/**
	 * 流程平台进行的单据动作批处理的入口类.
	 * <li>数据加锁和一致性检查
	 * <li>动作执行IPFBusiAction
	 * <li>该接口是远程public接口，无事务，为了保证锁和事务的一致性，特提供该接口
	 * <li>平台默认调用该接口
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
	 * 批量处理不同单据类型下的多个单据
	 * @param actionName 动作
	 * @param worknoteVO 处理结果和意见
	 * @param billTypes 单据类型
	 * @param billIds 单据id数组, 每个元素需要与单据类型数组中的相应位置对应
	 * @return
	 * @throws BusinessException
	 */
	public Object processBatch(String actionName, WorkflownoteVO worknoteVO, String[] billTypes, String[] billIds)
			throws BusinessException;

	/**
	 * 根据单据ID查询有效的审核人
	 * 
	 * @param billId
	 * @param billType
	 * @return
	 * @throws BusinessException
	 */
	//xry TODO:public UserVO[] queryValidCheckers(String billId, String billType) throws BusinessException;

	/**
	 * 查询某业务类型下，某单据动作驱动的所有单据动作
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
