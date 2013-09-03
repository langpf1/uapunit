package uap.workflow.pub.app.mobile.vo;

import nc.vo.pub.BusinessException;

/**
 * @author leijun 2006-2-20
 */
public class MobileException extends BusinessException {

	public MobileException() {
		super();
	}

	public MobileException(String s) {
		super(s);
	}

	public MobileException(String message, Throwable cause) {
		super(message, cause);
	}

	public MobileException(Throwable cause) {
		super(cause);
	}

}
