package uap.workflow.bizimpl;

import java.io.Serializable;

public class BizObjectType implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	/*
	 * ҵ��������
	 */
	private String group;

	/*
	 * ҵ��������ͣ���NC��˵��Ӧ���ǽ������ͻ򵥾�����ID
	 */
	private String billType;

	/*
	 * ��������
	 */
	private String transType;
	
	/*
	 * ҵ������������ƣ���Ϊ��ʾ������NC��˵�ǽ������ͻ򵥾����͵�����
	 */
	private String name;
	
	/*
	 * ��������ID
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
