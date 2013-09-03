package uap.workflow.app.config;

import uap.workflow.engine.exception.WorkflowException;

/**
 * ���������ļ��쳣.
 */
public class ParseConfigException extends WorkflowException {

  private static final long serialVersionUID = 1L;

  public ParseConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParseConfigException(String message) {
    super(message);
  }
}
