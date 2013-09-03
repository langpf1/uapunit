package uap.workflow.app.taskhandling;


/** 
   工作任务处理运行时的对外服务接口，具体业务从此接口实现自己的具体服务类
   需要在配置文件中配置ITaskHandleService的具体实现类是哪个   
 * @author 
 */

public interface ITaskHandlingService{

  /** 
         发送工作任务处理
   */
  public void send(TaskHandlingContext taskContext);
  
  /** 
        接收工作任务处理
   */
  public void receive(TaskHandlingContext taskContext);
}