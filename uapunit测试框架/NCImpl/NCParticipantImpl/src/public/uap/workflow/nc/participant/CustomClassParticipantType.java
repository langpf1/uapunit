package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   用户自定义类参与者类型
 * @author 
 */

public class CustomClassParticipantType implements IParticipantType{

	String code = "CustomClass";
	String name = "用户自定义类";

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