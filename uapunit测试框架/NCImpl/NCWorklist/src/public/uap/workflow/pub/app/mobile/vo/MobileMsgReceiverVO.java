package uap.workflow.pub.app.mobile.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * �ƶ����Ž�����VO
 * @author �׾� 15/10/2003
 */
public class MobileMsgReceiverVO extends ReceiverVO {
  //�û����ֻ���
  public String m_mobilephone = "";

  public MobileMsgReceiverVO() {
  }

  public String getMobilePhone() {
    return this.m_mobilephone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.m_mobilephone = mobilePhone;
  }
}