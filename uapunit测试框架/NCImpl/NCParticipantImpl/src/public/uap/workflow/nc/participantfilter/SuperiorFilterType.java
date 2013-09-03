package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   上级领导参与者过滤类型
 * @author 
 */

public class SuperiorFilterType implements IParticipantFilterType{

	String code = "Superior";
	String name = "上级";

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