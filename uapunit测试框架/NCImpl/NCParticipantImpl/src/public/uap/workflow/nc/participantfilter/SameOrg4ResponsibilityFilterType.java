package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   ͬ��֯��ְ��ʹ�ò����߹�������
 * @author 
 */

public class SameOrg4ResponsibilityFilterType implements IParticipantFilterType{

	String code = "SameOrg4Responsibility";
	String name = "ͬ��֯��ְ��ʹ��";

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