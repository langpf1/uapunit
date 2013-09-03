package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   角色参与者类型
 * @author 
 */

public class RoleParticipantType implements IParticipantType{
	String code = "Role";
	String name = "角色";

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