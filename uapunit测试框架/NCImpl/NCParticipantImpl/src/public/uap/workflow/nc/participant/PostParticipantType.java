package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   ��λ����������
 * @author 
 */

public class PostParticipantType implements IParticipantType{

	String code = "Post";
	String name = "��λ";

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