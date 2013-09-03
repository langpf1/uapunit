package uap.workflow.reslet.application.receiveData;

import java.util.List;

/* 抄送*/
public class CarbonCopy {
	
	/* 参与者名称的列表*/
	private List<Participant> participants ;
	

	/*角色组的名称的列表*/
	private List<Role> roles;
	/**
	 * 抄送的方式
	 * 消息（0）或者是邮件（1）
	 */
	private int[] notifytype;
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public int[] getNotifytype() {
		return notifytype;
	}
	public void setNotifytype(int[] notifytype) {
		this.notifytype = notifytype;
	}

}
