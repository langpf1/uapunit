package uap.workflow.reslet.application.receiveData;

import uap.workflow.restlet.application.RuntimeConstants;

/**��Ӧǰ̨�е����̼�صĴ�����������Ϣ�������������̵�������pk���������ı���*/
public class OperatorProDef {
       
	/**���̵�pk*/
	private String pk_prodef ;
	
	/**����*/	
	private RuntimeConstants actioncode;

	public String getPk_prodef() {
		return pk_prodef;
	}

	public void setPk_prodef(String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}

	public RuntimeConstants getActioncode() {
		return actioncode;
	}

	public void setActioncode(RuntimeConstants actioncode) {
		this.actioncode = actioncode;
	}
	
	
}
