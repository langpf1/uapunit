package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/**
 * ͬ��˾��ͬ���Ų����߹�������
 * 
 * @author
 */

public class SameCorpAndDeptFilterType implements IParticipantFilterType {
	String code = "SameCorpAndDept";
	String name = "ͬ��˾��ͬ����";

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