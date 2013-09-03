package uap.workflow.app.extend.bizstate;

import java.io.Serializable;

import nc.vo.pub.pf.IPfRetCheckInfo;

/**
 * 审批状态回调接口 使用的上下文
 * @author leijun 2008-8
 * @since 5.5
 */
public class CheckStatusCallbackContext implements Serializable {
	/**
	 * 单据VO实体
	 */
	private Object billVo;

	/**
	 * 审批人ID
	 */
	private String approveId;

	/**
	 * 审批日期
	 */
	private String approveDate;

	/**
	 * 审批批语
	 */
	private String checkNote;

	/**
	 * 审批状态
	 * @see IPfRetCheckInfo
	 */
	private int checkStatus;

	/**
	 * 是否为驳回
	 */
	private boolean isReject;
	
	/**
	 * 是否为终止
	 */
	boolean isTerminate = false;

	public Object getBillVo() {
		return billVo;
	}

	public void setBillVo(Object billVo) {
		this.billVo = billVo;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public boolean isReject() {
		return isReject;
	}

	public void setReject(boolean isReject) {
		this.isReject = isReject;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public boolean isTerminate() {
		return isTerminate;
	}

	public void setTerminate(boolean isTerminate) {
		this.isTerminate = isTerminate;
	}

}
