package uap.workflow.pub.app.mail.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * �ʼ�������VO
 * @author �׾� 15/10/2003
 */
public class MailReceiverVO extends ReceiverVO {
  //�����ʼ���ַ
  public String m_mailaddress = "";

  public MailReceiverVO() {
  }

  public String getMailAddress() {
    return this.m_mailaddress;
  }

  public void setMailAddress(String mailAddress) {
    this.m_mailaddress = mailAddress;
  }
}