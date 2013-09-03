package uap.workflow.bpmn2.exception;
/**
 * 
 */
public class Bpmn2Exception extends RuntimeException {
	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;
	public Bpmn2Exception(String message, Throwable cause) {
		super(message, cause);
	}
	public Bpmn2Exception(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	public Bpmn2Exception(String message) {
		super(message);
	}
	public Bpmn2Exception(String message, String hint) {
		super(message);
		this.hint = hint;
	}
	public Bpmn2Exception(Throwable cause) {
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
