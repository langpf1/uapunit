package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   职责参与者类型
 * @author 
 */

public class ResponsibilityParticipantType implements IParticipantType{
	String code = "Responsibility";
	String name = "职责";

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