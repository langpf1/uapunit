package uap.workflow.app.taskhandling;

import uap.workflow.engine.vos.TaskInstanceVO;


/**
 * �����������������ӿ�
 * ��������������ʱ���������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��幤��������������
 * @author 
 */

public interface ITaskHandlingAdapter{

  /** 
	���͹���������
   */
  public void send(TaskInstanceVO taskInstanceVO);
  
  /** 
	���չ���������
 */
  public void receive(TaskInstanceVO taskInstanceVO);
}