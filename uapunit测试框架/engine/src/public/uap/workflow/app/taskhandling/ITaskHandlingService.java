package uap.workflow.app.taskhandling;


/** 
   ��������������ʱ�Ķ������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��������
   ��Ҫ�������ļ�������ITaskHandleService�ľ���ʵ�������ĸ�   
 * @author 
 */

public interface ITaskHandlingService{

  /** 
         ���͹���������
   */
  public void send(TaskHandlingContext taskContext);
  
  /** 
        ���չ���������
   */
  public void receive(TaskHandlingContext taskContext);
}