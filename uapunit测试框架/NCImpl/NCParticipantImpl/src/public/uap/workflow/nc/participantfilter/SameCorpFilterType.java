package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   ͬ��˾�����߹�������
 * @author 
 */

public class SameCorpFilterType implements IParticipantFilterType{

	String code = "SameCorp";
	String name = "ͬ��˾";

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