package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ��ɫ�����������
 * @author 
 */

public class RoleGroupParticipantType implements IParticipantType{

	String code = "RoleGroup";
	String name = "��ɫ��";

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