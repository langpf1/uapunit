package uap.workflow.reslet.application.receiveData;

public class BaseReceiveTask {
	/**
	 * 批语或者备注
	 */
	private String comment;	
	/**
	 * 任务被执行的动作
	 */
	private int actioncode;
	/**
	 * 是否跟踪
	 */
	private boolean track;
	/**
	 * 当前操作的任务id
	 */
	private String taskID;
	/**
	 * 抄送
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
