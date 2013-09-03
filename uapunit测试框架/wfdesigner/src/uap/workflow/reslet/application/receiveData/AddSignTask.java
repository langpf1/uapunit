package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class AddSignTask extends BaseReceiveTask {
	/* 加签人员列表*/
	private List<Participant> participants ;
	
	/*加签角色列表*/
	private List<Role> roles;
    
	/*串并执行*/
	private boolean parallel;
	//TODO
	//定义成一个枚举的类型
	/*前加签还是 后加签*/
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
