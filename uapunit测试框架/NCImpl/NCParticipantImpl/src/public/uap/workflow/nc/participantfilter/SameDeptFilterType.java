package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   同部门参与者过滤类型
 * @author 
 */

public class SameDeptFilterType implements IParticipantFilterType{
	String code = "SameDept";
	String name = "同部门";

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