package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ��ɫ����������
 * @author 
 */

public class RoleParticipantType implements IParticipantType{
	String code = "Role";
	String name = "��ɫ";

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