package uap.workflow.app.extend.action;

import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 动作处理结束后，业务额外处理
 * <li>由审批流检查类实现，注册在bd_billtype.checkclassname字段
 * 
 * @author leijun 2010-2
 */
public interface IPfAfterAction {
	/**
	 * 平台动作处理完成后，业务组的额外处理 
	 * @param reloadVO
	 * @param objAfterAction
	 * @param hmPfExParams 扩展参数
	 * @return
	 * @throws BusinessException
	 */
	Object afterAction(AggregatedValueObject reloadVO, Object objAfterAction, HashMap hmPfExParams)
			throws BusinessException;

	/**
	 * 平台动作批处理完成后，业务组的额外处理
	 * @param reloadVOs
	 * @param objsAfterAction
	 * @param hmPfExParams 扩展参数
	 * @return
	 * @throws BusinessException
	 */
	Object[] afterBatch(AggregatedValueObject[] reloadVOs, Object[] objsAfterAction, HashMap hmPfExParams) throws BusinessException;
}
