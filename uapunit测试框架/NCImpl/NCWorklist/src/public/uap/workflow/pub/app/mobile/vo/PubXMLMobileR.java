package uap.workflow.pub.app.mobile.vo;

/**
 * 回复模式发送短信使用的mobile对象
 * @author ewei
 *
 */
public class PubXMLMobileR {
	
	String ID;
	
	String MobileNumber ;
	

	public PubXMLMobileR(String mobileNumber, String id) {
		super();
		MobileNumber = mobileNumber;
		ID = id;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public PubXMLMobileR(String MobileNumber){
		this.MobileNumber=MobileNumber;
	}
	
	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
}
