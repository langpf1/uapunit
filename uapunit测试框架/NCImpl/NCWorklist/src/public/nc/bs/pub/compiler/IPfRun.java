package nc.bs.pub.compiler;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * ����ƽ̨�����ű�ִ�нӿ�
 * 
 * @author ���ھ� 2002-2-27
 */
public interface IPfRun{
 
	/**
	 * ƽ̨�����ű������ʵ�ֵĽӿ�
	 * @param paraVo ����������VO
	 * @return
	 * @throws BusinessException
	 */
	Object runComClass(PfParameterVO paraVo) throws BusinessException;
}
