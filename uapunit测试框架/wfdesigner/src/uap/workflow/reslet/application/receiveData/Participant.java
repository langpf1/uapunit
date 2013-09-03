package uap.workflow.reslet.application.receiveData;

public class Participant {
	/**
	 * 参与者id
	 */
	private String participantID;	
	/**
	 * 任务被执行的动作
	 */
	private String name;

	public String getParticipantID() {
		return participantID;
	}
	public void setParticipantID(String participantID) {
		this.participantID = participantID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
