package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class AddSignTask extends BaseReceiveTask {
	/* ��ǩ��Ա�б�*/
	private List<Participant> participants ;
	
	/*��ǩ��ɫ�б�*/
	private List<Role> roles;
    
	/*����ִ��*/
	private boolean parallel;
	//TODO
	//�����һ��ö�ٵ�����
	/*ǰ��ǩ���� ���ǩ*/
	private int addsignType ; 
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


	public boolean isParallel() {
		return parallel;
	}


	public void setParallel(boolean parallel) {
		this.parallel = parallel;
	}


	public void setAddsignType(int addsignType) {
		this.addsignType = addsignType;
	}


	public int getAddsignType() {
		return addsignType;
	}

}
