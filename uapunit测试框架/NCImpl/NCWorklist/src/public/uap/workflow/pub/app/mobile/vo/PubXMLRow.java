package uap.workflow.pub.app.mobile.vo;

import java.util.ArrayList;

import nc.vo.pub.mobile.PubXMLMobile;

/**
 * ����ģʽ���ͷ���ʹ�õ�Row����
 * @author ewei
 *
 */
public class PubXMLRow {

	ArrayList<PubXMLMobile> Mobile = new ArrayList<PubXMLMobile>();

	String Content;

	public PubXMLRow(String msg) {
		this.Content = msg;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public ArrayList<PubXMLMobile> getMobile() {
		return Mobile;
	}

}
