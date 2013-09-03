package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   同公司参与者过滤类型
 * @author 
 */

public class SameCorpFilterType implements IParticipantFilterType{

	String code = "SameCorp";
	String name = "同公司";

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