package uap.workflow.app.exception;

/**
 * 流程平台运行时异常基类
 * 
 * @author leijun 2005-10-18
 */
public class PFRuntimeException extends RuntimeException {

	public PFRuntimeException() {
		super();
	}

	public PFRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PFRuntimeException(String message) {
		super(message);
	}

	public PFRuntimeException(Throwable cause) {
		super(cause);
	}

}
