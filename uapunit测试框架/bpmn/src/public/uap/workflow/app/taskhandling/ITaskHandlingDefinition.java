package uap.workflow.app.taskhandling;


/**
 * ����������ӿ� 
 * ���������������ʱ�Ľӿ�
 * @author 
 */

public interface ITaskHandlingDefinition{
  ///��������������
  public ITaskHandlingType getTaskHandleType();
  public void setTaskHandleType(ITaskHandlingType taskHandleType);
}