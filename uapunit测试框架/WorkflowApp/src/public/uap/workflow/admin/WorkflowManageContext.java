package uap.workflow.admin;

import java.io.Serializable;

import uap.workflow.engine.core.IActivity;

public class WorkflowManageContext implements Serializable {
	private String billIdOrBillVersionPK;
	
	private String billType;

	private String billNo;

	private Integer flowType;

	private Integer approveStatus;
	
	private String sendman;
	
	private String workflownotePk;
	
	private String flowinstancePk;
	
	private String actiontype;
	
	private String manageReason;
	
	private IActivity activity;
	

	public String getWorkflownotePk() {
		return workflownotePk;
	}

	public void setWorkflownotePk(String workflownotePk) {
		this.workflownotePk = workflownotePk;
	}

	public String getSendman() {
		return sendman;
	}

	public void setSendman(String sendman) {
		this.sendman = sendman;
	}

	public WorkflowManageContext() {
		super();
	}

	public String getBillId() {
		return billIdOrBillVersionPK;
	}

	public void setBillId(String billId) {
		this.billIdOrBillVersionPK = billId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getFlowType() {
		return flowType;
	}

	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getManageReason() {
		return manageReason;
	}

	public void setManageReason(String manageReason) {
		this.manageReason = manageReason;
	}

	public String getFlowinstancePk() {
		return flowinstancePk;
	}

	public void setFlowinstancePk(String flowinstancePk) {
		this.flowinstancePk = flowinstancePk;
	}

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

}
