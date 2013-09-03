package uap.workflow.app.action.impl;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * ����ƽ̨�ĵ��ݶ���Լ���ӿ�
 * 
 * @author fangj 2005-1-29
 * @modifier leijun 2007-4-26 ���Ӷ�����Լ���ӿ�
 */
public interface IPFActionConstrict {
	/**
	 * ִ�ж���ǰ��Լ�����
	 * @param paraVo
	 * @param m_methodReturnHas
	 * @throws BusinessException
	 */
	public void actionConstrictBefore(PfParameterVO paraVo)
			throws BusinessException;
	
	/**
	 * ִ�ж������Լ�����
	 * @param paraVo
	 * @param m_methodReturnHas
	 * @throws BusinessException
	 */
	public void actionConstrictAfter(PfParameterVO paraVo)
			throws BusinessException;
}