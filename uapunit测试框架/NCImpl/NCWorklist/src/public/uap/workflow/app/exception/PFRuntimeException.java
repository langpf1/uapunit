package uap.workflow.app.exception;

/**
 * ����ƽ̨����ʱ�쳣����
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
