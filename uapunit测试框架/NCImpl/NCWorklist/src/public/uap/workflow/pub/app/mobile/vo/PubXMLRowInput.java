package uap.workflow.pub.app.mobile.vo;


/**
 * 回复模式的接收使用的Row对象
 * @author ewei
 *
 */
public class PubXMLRowInput {
	
	String ID = "";
	
	String MobileNumber = "";
	
	String Content = "";

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	public PubXMLRowInput(String tranFlag, String id, String mobileNumber, String content) {
		super();
		ID = id;
		MobileNumber = mobileNumber;
		Content = content;
	}

	public PubXMLRowInput() {
		super();
	}	
	
}

