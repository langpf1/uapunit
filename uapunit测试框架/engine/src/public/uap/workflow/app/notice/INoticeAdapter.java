package uap.workflow.app.notice;

import uap.workflow.engine.vos.TaskInstanceVO;

/**
 * 通知适配器接口
 * 通知运行时的适配器接口，具体业务从此接口实现自己的具体通知适配类
 * @author 
 */

public interface INoticeAdapter{

  /** 
        通知发送信息
 * @param notice TODO
 * @param noticeContext TODO
   */
  public void sendNotice(TaskInstanceVO task, INoticeDefinition notice, NoticeContext noticeContext);
}