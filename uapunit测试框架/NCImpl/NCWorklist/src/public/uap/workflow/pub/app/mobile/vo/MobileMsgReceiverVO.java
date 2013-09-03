package uap.workflow.pub.app.mobile.vo;

import uap.workflow.app.notice.ReceiverVO;

/**
 * 移动短信接收者VO
 * @author 雷军 15/10/2003
 */
public class MobileMsgReceiverVO extends ReceiverVO {
  //用户的手机号
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