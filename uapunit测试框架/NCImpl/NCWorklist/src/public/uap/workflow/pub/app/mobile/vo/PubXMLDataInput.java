package uap.workflow.pub.app.mobile.vo;

/**
 * 订阅模式接收使用的data对象
 * @author ewei
 *
 */
public class PubXMLDataInput {
	
	String type = "";
	
	String MobileNumber = "";
	
	String Command = "";
	
	String Content = "" ;

	public String getCommand() {
		return Command;
	}

	public void setCommand(String command) {
		Command = command;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}



	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public PubXMLDataInput(String mobileNumber, String command, String content) {
		super();
		MobileNumber = mobileNumber;
		Command = command;
		Content = content;
	}
	
	public PubXMLDataInput() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}

