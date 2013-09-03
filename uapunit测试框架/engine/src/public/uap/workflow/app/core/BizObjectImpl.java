package uap.workflow.app.core;

/**
 * 业务对象的默认实现类
 */
public class BizObjectImpl implements IBusinessKey {

	private static final long serialVersionUID = -869867721516700448L;
	private String billtype;
	private String billNo;
	private String billId;
	private Object[] bizObjects = null;

	public String getBillType() {
		return billtype;
	}

	public void setBillType(String billtype) {
		this.billtype = billtype;
	}

	public String getBillId() {
		return this.billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}	

	public void setBizObjects(Object[] bizObjects) {
		this.bizObjects = bizObjects;
	}

	public Object[] getBizObjects() {
		return bizObjects;
	}
}
