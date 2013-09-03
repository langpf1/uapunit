package uap.workflow.pub.app.message.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * 消息接收者VO
 * @author 雷军 15/10/2003
 */
public class MsgReceiverVO extends ReceiverVO {
  //是否为组 废弃 by lj
  //private boolean m_bgroup = false;
  //是否可用
  private boolean m_bavailable = true;

  //构造器
  public MsgReceiverVO() {
    super();
    //m_bgroup = false;
    m_bavailable = true;
  }

  public boolean getIsAvailable() {
    return this.m_bavailable;
  }

  public void setIsAvailable(boolean m_isAvailable) {
    this.m_bavailable = m_isAvailable;
  }
}