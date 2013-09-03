package uap.workflow.pub.app.message;

import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQueryData;

/**
 * UI关联操作平台关联参数数据
 * 
 * @author leijun 2006-5-26
 */
public class PfLinkData implements ILinkAddData, ILinkMaintainData, ILinkApproveData,
		ILinkQueryData {

	private String sourceBillID;

	private String sourceBillType;

	private String sourcePkOrg;

	private String billID;

	private String billType;

	private String pkOrg;

	private Object userObject;

	/** 待办工作项的PK */
	private String pkMessage;

	/** 待办工作项所属的工作流类型：1=审批流 3=工作流，见<code>IApproveflowConst</code> */
	private int iWorkflowtype;

	public PfLinkData() {
		super();
	}

	public int getWorkflowtype() {
		return iWorkflowtype;
	}

	public void setWorkflowtype(int workflowtype) {
		iWorkflowtype = workflowtype;
	}

	public String getPkMessage() {
		return pkMessage;
	}

	public void setPkMessage(String pkMessage) {
		this.pkMessage = pkMessage;
	}

	public String getSourceBillID() {
		return sourceBillID;
	}

	public String getSourceBillType() {
		return sourceBillType;
	}

	public String getSourcePkOrg() {
		return sourcePkOrg;
	}

	public Object getUserObject() {
		return userObject;
	}

	public String getBillID() {
		return billID;
	}

	public String getBillType() {
		return billType;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public void setSourceBillID(String sourceBillID) {
		this.sourceBillID = sourceBillID;
	}

	public void setSourceBillType(String sourceBillType) {
		this.sourceBillType = sourceBillType;
	}

	public void setSourcePkOrg(String sourcePkOrg) {
		this.sourcePkOrg = sourcePkOrg;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

}
