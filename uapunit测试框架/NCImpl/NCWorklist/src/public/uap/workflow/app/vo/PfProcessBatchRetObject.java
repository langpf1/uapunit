package uap.workflow.app.vo;

import java.io.Serializable;

import uap.workflow.app.exception.PFBatchExceptionInfo;



public class PfProcessBatchRetObject implements Serializable {

	private PFBatchExceptionInfo exceptionMsg = null;
	
	private Object[] retObj = null;

	public PfProcessBatchRetObject(Object[] retObjsAfterAction,
			PFBatchExceptionInfo batchExceptionInfo) {
		this.retObj = retObjsAfterAction;
		this.exceptionMsg = batchExceptionInfo;
	}

	public PFBatchExceptionInfo getExceptionInfo(){
		return this.exceptionMsg;
	}
	
	public String getExceptionMsg() {
		if(exceptionMsg != null) {
			return exceptionMsg.getErrorMessage();
		}
		return null;
	}

	public Object[] getRetObj() {
		return retObj;
	}
}
