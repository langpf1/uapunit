package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   用户组参与者类型
 * @author 
 */

public class UserGroupParticipantType implements IParticipantType{
	String code = "UserGroup";
	String name = "用户组";

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