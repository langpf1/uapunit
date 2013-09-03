package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   角色组参与者类型
 * @author 
 */

public class RoleGroupParticipantType implements IParticipantType{

	String code = "RoleGroup";
	String name = "角色组";

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