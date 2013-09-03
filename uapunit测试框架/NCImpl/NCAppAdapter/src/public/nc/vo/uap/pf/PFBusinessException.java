package nc.vo.uap.pf;

public class PFBusinessException extends uap.workflow.app.exeception.PFBusinessException {
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
