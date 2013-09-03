package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   岗位参与者类型
 * @author 
 */

public class PostParticipantType implements IParticipantType{

	String code = "Post";
	String name = "岗位";

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