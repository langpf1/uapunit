package uap.workflow.pub.app.mobile.vo;

import java.util.ArrayList;

import nc.vo.pub.mobile.PubXMLRow;

/**
 * ����ģʽ����ʹ�õ�data����
 * @author ewei
 */
public class PubXMLData {
	String System = "nc";

	String type = "01";

	String accountnum = "accout";

	String accountname = "name";

	String sendname = "sname";
	

	ArrayList<PubXMLRow> pubxmlrow = null;

	public ArrayList<PubXMLRow> getPubxmlrow() {
		if (pubxmlrow == null)
			pubxmlrow = new ArrayList<PubXMLRow>();
		return pubxmlrow;
	}
}
