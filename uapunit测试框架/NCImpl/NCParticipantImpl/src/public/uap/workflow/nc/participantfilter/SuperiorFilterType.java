package uap.workflow.nc.participantfilter;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
   �ϼ��쵼�����߹�������
 * @author 
 */

public class SuperiorFilterType implements IParticipantFilterType{

	String code = "Superior";
	String name = "�ϼ�";

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