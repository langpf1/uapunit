package uap.workflow.app.participant;

import java.util.List;

import uap.workflow.engine.core.ITask;

/** 
   ����������ʱ����������
 * @author 
 */
public class ParticipantContext{

	List<IParticipant> participants;

	//�Ͳ�����������֯��ͬ
	String pk_org;
	//��������ְ��ʱ����֯�޶�����������¸�����Ϊnull
	String[] responsibility_orgs;
	ITask task;
	//����ʵ�壬һ��Ϊ���ݾۺ�VO
	Object billEntity;

	public void setParticipants(List<IParticipant> participants) {
		this.participants = participants;
	}
	
	public List<IParticipant> getParticipants() {
		return this.participants;
	}

	///ȡ���������ߵ�ID
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