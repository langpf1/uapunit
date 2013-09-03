package uap.workflow.pub.app.mobile.vo;

import nc.vo.pub.mobile.PubXMLRowInput;

/**
 * 回复模式接收短信使用data对象
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
