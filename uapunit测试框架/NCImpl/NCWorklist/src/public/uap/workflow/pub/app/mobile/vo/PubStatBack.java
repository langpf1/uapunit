package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * ��������������״̬VO
 * ״̬��ʽ
 *  <?xml version="1.0" encoding = "UTF-8"?>
 *  <Data System='nc/u8/u9' type=��01��  accountnum='���׺�' accountname='��������' sendname='����������'>	
 *		<TranFlag>0</ TranFlag >
 *		<ErrMsg>��������</ErrMsg>
 *  </Data>
 *
 * @author ewei 2007-10-10
 */
public class PubStatBack implements Serializable{

	String System = "nc";

	String type = "01";

	String accountnum = "accout";

	String accountname = "name";

	String sendname = "sname";
	
	String TranFlag = "";
	
	String ErrMsg="";
	
	public String getErrMsg() {
		return ErrMsg;
	}

	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}


	public String getTranFlag() {
		return TranFlag;
	}

	public void setTranFlag(String tranFlag) {
		TranFlag = tranFlag;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getSystem() {
		return System;
	}

	public void setSystem(String system) {
		System = system;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
