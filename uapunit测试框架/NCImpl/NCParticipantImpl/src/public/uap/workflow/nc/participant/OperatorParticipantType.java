package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ����Ա����������
 * @author 
 */

public class OperatorParticipantType implements IParticipantType{

	String code = "Operator";
	String name = "����Ա";

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