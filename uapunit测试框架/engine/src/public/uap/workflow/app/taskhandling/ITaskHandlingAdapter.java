package uap.workflow.app.taskhandling;

import uap.workflow.engine.vos.TaskInstanceVO;


/**
 * 工作任务处理适配器接口
 * 工作任务处理运行时的适配器接口，具体业务从此接口实现自己的具体工作任务处理适配类
 * @author 
 */

public interface ITaskHandlingAdapter{

  /** 
	发送工作任务处理
   */
  public void send(TaskInstanceVO taskInstanceVO);
  
  /** 
	接收工作任务处理
 */
  public void receive(TaskInstanceVO taskInstanceVO);
}