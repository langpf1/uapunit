package uap.workflow.pub.app.message.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * ��Ϣ������VO
 * @author �׾� 15/10/2003
 */
public class MsgReceiverVO extends ReceiverVO {
  //�Ƿ�Ϊ�� ���� by lj
  //private boolean m_bgroup = false;
  //�Ƿ����
  private boolean m_bavailable = true;

  //������
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