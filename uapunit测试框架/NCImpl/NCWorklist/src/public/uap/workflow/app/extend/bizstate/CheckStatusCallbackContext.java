package uap.workflow.app.extend.bizstate;

import java.io.Serializable;

import nc.vo.pub.pf.IPfRetCheckInfo;

/**
 * ����״̬�ص��ӿ� ʹ�õ�������
 * @author leijun 2008-8
 * @since 5.5
 */
public class CheckStatusCallbackContext implements Serializable {
	/**
	 * ����VOʵ��
	 */
	private Object billVo;

	/**
	 * ������ID
	 */
	private String approveId;

	/**
	 * ��������
	 */
	private String approveDate;

	/**
	 * ��������
	 */
	private String checkNote;

	/**
	 * ����״̬
	 * @see IPfRetCheckInfo
	 */
	private int checkStatus;

	/**
	 * �Ƿ�Ϊ����
	 */
	private boolean isReject;
	
	/**
	 * �Ƿ�Ϊ��ֹ
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
