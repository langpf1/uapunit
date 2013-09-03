package uap.workflow.pub.app.mobile.vo;

/**
 * 发布模式发送使用的Mobile对象
 * @author ewei
 *
 */
public class PubXMLMobile {
	
	String MobileNumber ;
		
	
	public PubXMLMobile(String MobileNumber){
		this.MobileNumber=MobileNumber;
	}
	
	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	
}
