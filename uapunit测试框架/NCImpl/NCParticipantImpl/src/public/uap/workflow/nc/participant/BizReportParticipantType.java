package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ҵ��㱨��ϵ����������
 * @author 
 */

public class BizReportParticipantType implements IParticipantType{
	String code = "BusiReport";
	String name = "ҵ��㱨��ϵ";

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