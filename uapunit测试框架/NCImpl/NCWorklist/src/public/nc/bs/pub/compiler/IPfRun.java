package nc.bs.pub.compiler;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;

/**
 * 流程平台动作脚本执行接口
 * 
 * @author 樊冠军 2002-2-27
 */
public interface IPfRun{
 
	/**
	 * 平台动作脚本类必须实现的接口
	 * @param paraVo 工作流参数VO
	 * @return
	 * @throws BusinessException
	 */
	Object runComClass(PfParameterVO paraVo) throws BusinessException;
}
