package uap.workflow.reslet.application.receiveData;

public class Attachement {
	/*时间*/
	private String  uploadingDate ;
	/*人员*/
	private Participant participant;
	/*活动名称*/
	private String activityname;
	/*附件*/
	private byte[] bytes;
	public String getUploadingDate() {
		return uploadingDate;
	}
	public void setUploadingDate(String uploadingDate) {
		this.uploadingDate = uploadingDate;
	}
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public String getActivityname() {
		return activityname;
	}
	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
