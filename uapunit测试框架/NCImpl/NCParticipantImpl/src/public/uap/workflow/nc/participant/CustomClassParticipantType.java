package uap.workflow.nc.participant;

import uap.workflow.app.participant.IParticipantType;

/** 
   �û��Զ��������������
 * @author 
 */

public class CustomClassParticipantType implements IParticipantType{

	String code = "CustomClass";
	String name = "�û��Զ�����";

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