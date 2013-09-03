package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/**
 * 同公司和同部门参与者过滤类型
 * 
 * @author
 */

public class SameCorpAndDeptFilterType implements IParticipantFilterType {
	String code = "SameCorpAndDept";
	String name = "同公司和同部门";

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