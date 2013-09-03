package uap.workflow.app.action.impl;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * 流程平台的单据动作约束接口
 * 
 * @author fangj 2005-1-29
 * @modifier leijun 2007-4-26 增加动作后约束接口
 */
public interface IPFActionConstrict {
	/**
	 * 执行动作前的约束检查
	 * @param paraVo
	 * @param m_methodReturnHas
	 * @throws BusinessException
	 */
	public void actionConstrictBefore(PfParameterVO paraVo)
			throws BusinessException;
	
	/**
	 * 执行动作后的约束检查
	 * @param paraVo
	 * @param m_methodReturnHas
	 * @throws BusinessException
	 */
	public void actionConstrictAfter(PfParameterVO paraVo)
			throws BusinessException;
}