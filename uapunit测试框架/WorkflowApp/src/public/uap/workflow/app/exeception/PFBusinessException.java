package uap.workflow.app.exeception;

import nc.vo.pub.BusinessException;

/**
 * 流程平台业务异常基类
 * 
 * @author leijun 2005-10-18
 */
public class PFBusinessException extends BusinessException {

	public PFBusinessException() {
		super();
	}

	public PFBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PFBusinessException(String s) {
		super(s);
	}
	
	public PFBusinessException(Throwable cause) {
		super(cause);
	}

}
