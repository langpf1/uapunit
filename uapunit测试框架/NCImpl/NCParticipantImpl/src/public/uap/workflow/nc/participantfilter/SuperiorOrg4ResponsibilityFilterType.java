package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   上级组织供职责使用参与者过滤类型
 * @author 
 */

public class SuperiorOrg4ResponsibilityFilterType implements IParticipantFilterType{

	String code = "SuperiorOrg4Responsibility";
	String name = "上级组织供职责使用";

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