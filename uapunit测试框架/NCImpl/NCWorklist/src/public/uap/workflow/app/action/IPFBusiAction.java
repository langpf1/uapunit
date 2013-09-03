package uap.workflow.app.action;

import java.util.HashMap;

import uap.workflow.app.exception.PFBatchExceptionInfo;
import uap.workflow.vo.WorkflownoteVO;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 流程平台动作处理 服务接口
 * 
 * @author leijun 2005-8-17 
 */
public interface IPFBusiAction {

	/**
	 * 流程平台进行的单据动作处理
	 * <li>动作执行前的工作流处理(弃审或删除)
	 * <li>进行动作约束检查
	 * <li>执行动作脚本
	 * <li>判断返回值 是否直接返回
	 * <li>判断动作是否为最后一个动作,执行动作驱动
	 * <li>动作执行后的工作流处理,启动审批流
	 * 
	 * @param actionName 动作编码，比如"SAVE"/"APPROVE"
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
	 * 动作批处理，多事务
	 * <li>遍历一遍单据VO数组,进行动作执行前的工作流处理(弃审或删除)和动作约束检查
	 * <li>执行动作脚本
	 * <li>遍历处理后的单据VO数组,判断动作是否为最后一个动作,执行动作驱动; 同时进行动作执行后的工作流处理,依次启动审批流
	 *  
	 * @param actionName 动作编码，比如"SAVE","APPROVE"
	 * @param billType 单据类型PK
	 * @param currentDate 处理日期
	 * @param billvos 单据聚合VO数组
	 * @param userObjAry 用户对象数组
	 * @param worknoteVO 工作项VO
	 * @param retError 
	 * @return 动作批处理返回值
	 * @throws BusinessException
	 */
	public Object[] processBatch(String actionName, String billType, AggregatedValueObject[] billvos,
			Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam, PFBatchExceptionInfo exceptionInfo) throws BusinessException;
	
	/**
	 * 动作批处理，单一事务
	 * <li>遍历一遍单据VO数组,进行动作执行前的工作流处理(弃审或删除)和动作约束检查
	 * <li>执行动作脚本
	 * <li>遍历处理后的单据VO数组,判断动作是否为最后一个动作,执行动作驱动; 同时进行动作执行后的工作流处理,依次启动审批流
	 *  
	 * @param actionName 动作编码，比如"SAVE","APPROVE"
	 * @param billType 单据类型PK
	 * @param currentDate 处理日期
	 * @param billvos 单据聚合VO数组
	 * @param userObjAry 用户对象数组
	 * @param worknoteVO 工作项VO
	 * @param retError 
	 * @return 动作批处理返回值
	 * @throws BusinessException
	 */
	public Object[] processBatch(String actionName, String billType, AggregatedValueObject[] billvos,
			Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam) throws BusinessException;

}