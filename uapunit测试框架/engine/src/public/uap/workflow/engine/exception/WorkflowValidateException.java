package uap.workflow.engine.exception;
public class WorkflowValidateException extends WorkflowBusinessException {
	private static final long serialVersionUID = 4327268254447664952L;
	public WorkflowValidateException() {
		super();
	}
	public WorkflowValidateException(String message, Throwable cause) {
		super(message, cause);
	}
	public WorkflowValidateException(String message) {
		super(message);
	}
	public WorkflowValidateException(Throwable cause) {
		super(cause);
	}
}
