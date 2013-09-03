package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class AssignTask {
     /**
      *  指派人员列表
      */
	private List<Participant> participants ;   
	/**
	 * 串并执行
	 */
	private boolean parallel;
	/**
	 * 是否跟踪
	 */
	private boolean track;
	/**
	 * 批语或者备注
	 */
	private String comment;
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public boolean isParallel() {
		return parallel;
	}
	public void setParallel(boolean parallel) {
		this.parallel = parallel;
	}
	public boolean isTrack() {
		return track;
	}
	public void setTrack(boolean track) {
		this.track = track;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}	
	
}
