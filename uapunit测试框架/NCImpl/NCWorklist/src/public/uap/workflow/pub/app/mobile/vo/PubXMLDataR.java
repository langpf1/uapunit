package uap.workflow.pub.app.mobile.vo;

import java.util.ArrayList;

import nc.vo.pub.mobile.PubXMLRowR;
/**
 * 回复模式发送短信data对象
 * @author ewei
 *
 */
public class PubXMLDataR {
	
	String System = "nc";

	String type = "03";

	String appname = "appname";
	
	String accountnum = "accout";

	String accountname = "name";

	String sendname = "sname";
	

	ArrayList<PubXMLRowR> pubxmlrow = null;

	public ArrayList<PubXMLRowR> getPubxmlrow() {
		if (pubxmlrow == null)
			pubxmlrow = new ArrayList<PubXMLRowR>();
		return pubxmlrow;
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

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
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

	public void setPubxmlrow(ArrayList<PubXMLRowR> pubxmlrow) {
		this.pubxmlrow = pubxmlrow;
	}
}
