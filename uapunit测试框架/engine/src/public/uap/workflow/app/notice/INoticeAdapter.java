package uap.workflow.app.notice;

import uap.workflow.engine.vos.TaskInstanceVO;

/**
 * ֪ͨ�������ӿ�
 * ֪ͨ����ʱ���������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ���֪ͨ������
 * @author 
 */

public interface INoticeAdapter{

  /** 
        ֪ͨ������Ϣ
 * @param notice TODO
 * @param noticeContext TODO
   */
  public void sendNotice(TaskInstanceVO task, INoticeDefinition notice, NoticeContext noticeContext);
}