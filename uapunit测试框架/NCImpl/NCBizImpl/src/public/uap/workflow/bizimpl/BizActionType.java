package uap.workflow.bizimpl;

import java.io.Serializable;

public class BizActionType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 业务对象对应的操作编码
	 */
	private String action;
	
	/*
	 * 业务对象对应的操作名称
	 */
	private String actionName;

	/*
	 * 操作的ID
	 */
	private String actionID;
	
	public BizActionType(String code, String name, String id){
		action = code;
		actionName = name;
		actionID = id;
	}
	
	public String getAction(){
		return action;
	}
	
	public void setAction(String action){
		this.action = action;
	}
	
	public String getActionName(){
		return actionName;
	}
	
	public void setActionName(String actionName){
		this.actionName = actionName;
	}

	public String getActionID(){
		return actionID;
	}
	
	public void setActionID(String actionID){
		this.actionID = actionID;
	}
}
