package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   操作员参与者类型
 * @author 
 */

public class OperatorParticipantType implements IParticipantType{

	String code = "Operator";
	String name = "操作员";

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