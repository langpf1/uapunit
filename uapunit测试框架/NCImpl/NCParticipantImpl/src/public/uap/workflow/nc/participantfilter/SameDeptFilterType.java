package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   ͬ���Ų����߹�������
 * @author 
 */

public class SameDeptFilterType implements IParticipantFilterType{
	String code = "SameDept";
	String name = "ͬ����";

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}
}