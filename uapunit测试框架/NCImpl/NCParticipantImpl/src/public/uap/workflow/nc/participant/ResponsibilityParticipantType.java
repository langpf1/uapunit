package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ְ�����������
 * @author 
 */

public class ResponsibilityParticipantType implements IParticipantType{
	String code = "Responsibility";
	String name = "ְ��";

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