package uap.workflow.reslet.application.receiveData;

public class RejectTask extends BaseReceiveTask{

	/* ���ػ���id */
	private String rejectNodeID;

	/** �������ڵĵ������� */
	private String pk_bizobject;

	/** ���ݺ� */
	private String pk_form_ins_version;

	public String getPk_bizobject() {
		return pk_bizobject;
	}

	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}

	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}

	public String getPk_form_ins_version() {
		return pk_form_ins_version;
	}

	public void setRejectNodeID(String rejectNodeID) {
		this.rejectNodeID = rejectNodeID;
	}

	public String getRejectNodeID() {
		return rejectNodeID;
	}

}
