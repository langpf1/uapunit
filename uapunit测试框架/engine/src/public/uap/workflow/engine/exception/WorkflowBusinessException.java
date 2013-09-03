package uap.workflow.engine.exception;
public class WorkflowBusinessException extends Exception {
	private static final long serialVersionUID = -7701050274700646799L;
	public WorkflowBusinessException() {
		super();
	}
	public WorkflowBusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	public WorkflowBusinessException(String s) {
		super(s);
	}
	public WorkflowBusinessException(Throwable cause) {
		super(cause);
	}
}
