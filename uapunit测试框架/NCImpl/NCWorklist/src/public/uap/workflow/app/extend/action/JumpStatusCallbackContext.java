package uap.workflow.app.extend.action;

public class JumpStatusCallbackContext {

	/**
	 * ����VOʵ��
	 */
	private Object billVo;
	
	/**
	 * ��ת��ҵ����PK
	 * */
	private String busitype;
	
	private String billtypeOrTranstype;
	
	
	public String getBilltypeOrTranstype() {
		return billtypeOrTranstype;
	}

	public void setBilltypeOrTranstype(String billtypeOrTranstype) {
		this.billtypeOrTranstype = billtypeOrTranstype;
	}

	public Object getBillVo() {
		return billVo;
	}

	public void setBillVo(Object billVo) {
		this.billVo = billVo;
	}

	public String getBusitype() {
		return busitype;
	}

	public void setBusitype(String busitype) {
		this.busitype = busitype;
	}	
}
