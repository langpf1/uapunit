package uap.workflow.pub.app.mobile.vo;

import nc.vo.pub.mobile.PubXMLRowInput;

/**
 * �ظ�ģʽ���ն���ʹ��data����
 * @author ewei
 *
 */
public class PubXMLDataInputR {
	
	String System = "nc";

	String type = "03";
	
	String appname = "";

	String accountnum = "accout";

	String accountname = "name";

	String sendname = "sname";
	

	PubXMLRowInput Row = new PubXMLRowInput();

	public PubXMLRowInput getPubxmlrow() {
		return Row;
	}
	
	public void setPubxmlrow(PubXMLRowInput pubxmlrow){
		this.Row=pubxmlrow;
	}
}
