package uap.workflow.vo;

import java.io.Serializable;

import uap.workflow.app.core.IBusinessKey;

/**
 * 流程平台运行的时候，与应用对接时的输入参数类
 * 							     
 */
public class WFAppParameter implements Serializable {
	private static final long serialVersionUID = -556533468870570221L;

	/** 动作名称 */
	private String actionName = null;

	/** 制单人PK */
	private String billMaker = null;

	/** 操作员PK */
	private String operator = null;

	/** 工作项VO */
	private WorkflownoteVO workFlow = null;

	/** 是否提交后自动审批  */
	private boolean autoApproveAfterCommit = false;

	/** 流程定义PK */
	private String processDefPK = null;
	
	private IBusinessKey businessObject;
	
	/** 组织主键  */
	private String orgPK = null;

	/** 集团主键  */
	private String groupPK = null;
	
	/**修订单据 */
	private int emendEnum;
	
	private long bizDateTime;
	
	public long getBizDateTime(){
		return bizDateTime;
	}
	
	public void setBizDateTime(long bizDateTime) {
		this.bizDateTime = bizDateTime;
	}	

	public String getBillId() {
		if(businessObject!=null)
			return businessObject.getBillId();
		return null;
	}

	public String getBillNo() {
		if(businessObject!=null)
			return businessObject.getBillNo();
		return null;
	}

	public String getBillType() {
		if(businessObject!=null)
			return businessObject.getBillType();
		return null;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getBillMaker() {
		return billMaker;
	}

	public void setBillMaker(String billMaker) {
		this.billMaker = billMaker;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public WorkflownoteVO getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkflownoteVO workFlow) {
		this.workFlow = workFlow;
	}

	public boolean isAutoApproveAfterCommit() {
		return autoApproveAfterCommit;
	}

	public void setAutoApproveAfterCommit(boolean autoApproveAfterCommit) {
		this.autoApproveAfterCommit = autoApproveAfterCommit;
	}

	public String getProcessDefPK() {
		return processDefPK;
	}

	public void setProcessDefPK(String processDefPK) {
		this.processDefPK = processDefPK;
	}

	public IBusinessKey getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(IBusinessKey businessObject) {
		this.businessObject = businessObject;
	}

	public String getOrgPK() {
		return orgPK;
	}

	public void setOrgPK(String orgPK) {
		this.orgPK = orgPK;
	}

	public String getGroupPK() {
		return groupPK;
	}

	public void setGroupPK(String groupPK) {
		this.groupPK = groupPK;
	}

	public int getEmendEnum() {
		return emendEnum;
	}

	public void setEmendEnum(int emendEnum) {
		this.emendEnum = emendEnum;
	}
}