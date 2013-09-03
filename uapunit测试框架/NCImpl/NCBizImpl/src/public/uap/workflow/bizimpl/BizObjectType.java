package uap.workflow.bizimpl;

import java.io.Serializable;

public class BizObjectType implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	/*
	 * 业务对象分组
	 */
	private String group;

	/*
	 * 业务对象类型，对NC来说，应该是交易类型或单据类型ID
	 */
	private String billType;

	/*
	 * 交易类型
	 */
	private String transType;
	
	/*
	 * 业务对象类型名称，作为显示名，对NC来说是交易类型或单据类型的名称
	 */
	private String name;
	
	/*
	 * 交易类型ID
	 */
	private String ID;
	
	public BizObjectType (String group, String billType, String transType, String name, String id){
		this.group = group;
		this.billType = billType;
		this.transType = transType;
		this.name = name;
		this.ID = id;
	}
	
	public String getGroup(){
		return group;
	}
	
	public void setGroup(String bizObjectGroup){
		this.group = bizObjectGroup;
	}
	
	public String getBillType(){
		return billType;
	}
	
	public void setBillType(String billType){
		this.billType = billType;
	}

	public String getTransType(){
		return transType;
	}
	
	public void setTransType(String transType){
		this.transType = transType;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getID(){
		return ID;
	}
	
	public void setID(String id){
		this.ID = id;
	}
}
