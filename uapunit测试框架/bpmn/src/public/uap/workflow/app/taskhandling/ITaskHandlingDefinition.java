package uap.workflow.app.taskhandling;


/**
 * 工作任务处理接口 
 * 工作任务处理设计期时的接口
 * @author 
 */

public interface ITaskHandlingDefinition{
  ///工作任务处理类型
  public ITaskHandlingType getTaskHandleType();
  public void setTaskHandleType(ITaskHandlingType taskHandleType);
}