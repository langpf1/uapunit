package uap.workflow.pub.app.mail.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * 邮件接收者VO
 * @author 雷军 15/10/2003
 */
public class MailReceiverVO extends ReceiverVO {
  //电子邮件地址
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