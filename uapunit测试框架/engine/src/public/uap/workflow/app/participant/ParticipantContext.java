package uap.workflow.app.participant;

import java.util.List;

import uap.workflow.engine.core.ITask;

/** 
   参与者运行时的上下文类
 * @author 
 */
public class ParticipantContext{

	List<IParticipant> participants;

	//和参与者所属组织相同
	String pk_org;
	//参与者是职责时的组织限定。其他情况下该数组为null
	String[] responsibility_orgs;
	ITask task;
	//单据实体，一般为单据聚合VO
	Object billEntity;

	public void setParticipants(List<IParticipant> participants) {
		this.participants = participants;
	}
	
	public List<IParticipant> getParticipants() {
		return this.participants;
	}

	///取首条参与者的ID
	public String getParticipantID() {
		if(this.participants.size() > 0)
		{
			IParticipant participant = this.participants.get(0);
			return participant.getParticipantID();
		}
		
		return null;
	}

	public String[] getResponsibility_orgs() {
		return responsibility_orgs;
	}
	public void setResponsibility_orgs(String[] responsibility_orgs) {
		this.responsibility_orgs = responsibility_orgs;
	}

	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public ITask getTask() {
		return task;
	}

	public void setTask(ITask task) {
		this.task = task;
	}

	public Object getBillEntity() {
		return billEntity;
	}

	public void setBillEntity(Object billEntity) {
		this.billEntity = billEntity;
	}	
}