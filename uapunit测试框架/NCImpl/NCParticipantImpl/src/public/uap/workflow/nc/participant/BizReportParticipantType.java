package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   业务汇报关系参与者类型
 * @author 
 */

public class BizReportParticipantType implements IParticipantType{
	String code = "BusiReport";
	String name = "业务汇报关系";

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