package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   �ϼ���֯��ְ��ʹ�ò����߹�������
 * @author 
 */

public class SuperiorOrg4ResponsibilityFilterType implements IParticipantFilterType{

	String code = "SuperiorOrg4Responsibility";
	String name = "�ϼ���֯��ְ��ʹ��";

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