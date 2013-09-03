package uap.workflow.app.extend.action;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 动作处理前，业务额外处理
 * <li>由审批流检查类实现，注册在bd_billtype.checkclassname字段
 * <li>主要用于在传差异VO时，给业务组提供补全VO的机会
 * 
 * @author guowl 2010-5
 */
public interface IPfBeforeAction {
	/**
	 * 平台动作处理前，业务组的额外处理 
	 * @param hmPfExParams 
	 * @param userObj 
	 * @param reloadVO
	 * @param objAfterAction
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject beforeAction(Object billVO, Object userObj, HashMap hmPfExParams) throws BusinessException;

	/**
	 * 平台动作批处理完成前，业务组的额外处理
	 * @param hmPfExParams 
	 * @param userObjAry 
	 * @param reloadVOs
	 * @param objsAfterAction
	 * @return
	 * @throws BusinessException
	 */
	AggregatedValueObject[] beforeBatch(Object[] billVOs, Object[] userObjAry, HashMap hmPfExParams) throws BusinessException;

	/**
	 * 返回克隆VO，如果不是批处理，返回只有一个元素的数组。
	 * @return
	 */
	AggregatedValueObject[] getCloneVO();
}
