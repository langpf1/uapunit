package uap.workflow.pub.app.message;

public class WorkitemMsgContext {
	private String sender = "";
	private String billno = "";
	private String billType = "";
	private String billid = "";
	private String result = "";
	private String actionType = "";
	private String agent = "";
	private String checkNote = "";
	private String msgtempcode = "";
	private String checkman = "";
	private Object busiObj = null;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public Object getBusiObj() {
		return busiObj;
	}

	public void setBusiObj(Object busiObj) {
		this.busiObj = busiObj;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getBillid() {
		return billid;
	}

	public String getMsgtempcode() {
		return msgtempcode;
	}

	public void setMsgtempcode(String msgtempcode) {
		this.msgtempcode = msgtempcode;
	}

	public String getCheckman() {
		return checkman;
	}

	public void setCheckman(String checkman) {
		this.checkman = checkman;
	}

}
