package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   �����û������������
 * @author 
 */

public class WfUserGroupParticipantType implements IParticipantType{
	String code = "WfUserGroup";
	String name = "�����û���";

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