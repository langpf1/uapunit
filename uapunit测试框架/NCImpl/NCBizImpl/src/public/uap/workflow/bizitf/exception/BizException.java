package uap.workflow.bizitf.exception;

import uap.workflow.bizimpl.BizAction;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = -2827088470742592931L;
	
	private BizAction bizAction = null;
	
	public BizException(){
		
	}
	
	public BizException(String message){
		super(message);
	}

	public BizException(Throwable t){
		super(t);
	}
	
	public BizException(String message, Throwable t){
		super(message, t);
	}

	public BizException(Throwable e, BizAction action){
		super(getMessage(action),e);
	}
	

	public BizAction getBizAction(){
		return bizAction;
	}
		
	public void setBizAction(BizAction bizAction){
		this.bizAction = bizAction;
	}

	public static String getMessage(BizAction action){
		String errorString = "";
		if (action != null)
			errorString = action.getBizObjectType().getName() + 
			" operation " + action.getBizActionType().getActionName() + errorString;
		return errorString;
	}
}
