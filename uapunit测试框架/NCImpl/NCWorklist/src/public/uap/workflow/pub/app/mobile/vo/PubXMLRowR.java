package uap.workflow.pub.app.mobile.vo;

import java.util.ArrayList;

import nc.vo.pub.mobile.PubXMLMobileR;
/**
 * 回复模式发送短信Row对象
 * @author ewei
 *
 */
public class PubXMLRowR {

	ArrayList<PubXMLMobileR> Mobile = new ArrayList<PubXMLMobileR>();

	String Content;

	public PubXMLRowR(String msg) {
		this.Content = msg;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public ArrayList<PubXMLMobileR> getMobile() {
		return Mobile;
	}
}
