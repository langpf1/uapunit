package uap.workflow.reslet.application.receiveData;

public class BaseReceiveTask {
	/**
	 * ������߱�ע
	 */
	private String comment;	
	/**
	 * ����ִ�еĶ���
	 */
	private int actioncode;
	/**
	 * �Ƿ����
	 */
	private boolean track;
	/**
	 * ��ǰ����������id
	 */
	private String taskID;
	/**
	 * ����
	 * */
	private CarbonCopy cc;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getActioncode() {
		return actioncode;
	}
	public void setActioncode(int actioncode) {
		this.actioncode = actioncode;
	}
	public boolean isTrack() {
		return track;
	}
	public void setTrack(boolean track) {
		this.track = track;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public void setCc(CarbonCopy cc) {
		this.cc = cc;
	}
	public CarbonCopy getCc() {
		return cc;
	}
}
