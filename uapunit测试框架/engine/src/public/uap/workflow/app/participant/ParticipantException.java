package uap.workflow.app.participant;

import uap.workflow.engine.exception.WorkflowException;

/**
 * 参与者异常.
 */
public class ParticipantException extends WorkflowException {

  private static final long serialVersionUID = 1L;

  public ParticipantException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParticipantException(String message) {
    super(message);
  }
}
