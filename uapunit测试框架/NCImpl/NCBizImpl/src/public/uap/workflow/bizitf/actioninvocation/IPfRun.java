package uap.workflow.bizitf.actioninvocation;

import uap.workflow.bizimpl.bizinvocation.PfParameterVO;
import nc.vo.pub.BusinessException;


/**
 * ����ƽ̨�����ű�ִ�нӿ�
 * 
 * @author ���ھ� 2002-2-27
 */
public interface IPfRun {
 
	/**
	 * ƽ̨�����ű������ʵ�ֵĽӿ�
	 * @param paraVo ����������VO
	 * @return
	 * @throws BusinessException
	 */
	Object runComClass(PfParameterVO paraVo) throws BusinessException;
}
