package uap.workflow.engine.exception;
/**
 * 
 * @author tianchw
 * 
 */
public class WorkflowRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;
	public WorkflowRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	public WorkflowRuntimeException(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	public WorkflowRuntimeException(String message) {
		super(message);
	}
	public WorkflowRuntimeException(String message, String hint) {
		super(message);
		this.hint = hint;
	}
	public WorkflowRuntimeException(Throwable cause) {
		super(cause);
	}
	public String getHint() {
		if (hint == null)
			hint = this.getMessage();
		if (hint == null) {
			if (this.getCause() != null)
				hint = this.getCause().getMessage();
		}
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
}
