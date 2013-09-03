package uap.workflow.reslet.application.receiveData;

import uap.workflow.restlet.application.RuntimeConstants;

/**对应前台中的流程监控的处理，包含的信息：被操作的流程的主键（pk），操作的编码*/
public class OperatorProDef {
       
	/**流程的pk*/
	private String pk_prodef ;
	
	/**操作*/	
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
