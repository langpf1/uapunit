package uap.workflow.reslet.application.receiveData;

public class Attachement {
	/*ʱ��*/
	private String  uploadingDate ;
	/*��Ա*/
	private Participant participant;
	/*�����*/
	private String activityname;
	/*����*/
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
