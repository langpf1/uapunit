package uap.workflow.reslet.application.receiveData;

import java.util.List;

/* ����*/
public class CarbonCopy {
	
	/* ���������Ƶ��б�*/
	private List<Participant> participants ;
	

	/*��ɫ������Ƶ��б�*/
	private List<Role> roles;
	/**
	 * ���͵ķ�ʽ
	 * ��Ϣ��0���������ʼ���1��
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
