package uap.workflow.bizimpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import nc.vo.pub.AggregatedValueObject;

public class BizContext implements Serializable{
	
	private static final long serialVersionUID = -6515107150323722895L;

	protected BizAction action;
	
	protected String billID;
	
	protected String billCode;

	protected AggregatedValueObject bizEntity;
	
	protected AggregatedValueObject[] bizEntities;
	
	protected Map<String, Object> customProperties;
	
	public static BizContext construct( BizAction action, 
			String billID,
			String billCode,
			AggregatedValueObject bizEntity,
			AggregatedValueObject[] bizEntities
			){
		BizContext bizContext = new BizContext(); 
		bizContext.action = action;
		bizContext.billID = billID;  
		bizContext.billCode = billCode;
		bizContext.bizEntity = bizEntity;
		bizContext.bizEntities = bizEntities;
		if (bizContext.customProperties == null)
			bizContext.customProperties = new HashMap<String, Object>();
		return bizContext;
	}
	
	public BizAction getBizAction(){
		return action;
	}
	
	public String getBillID(){
		return billID;
	}
	
	public BizAction getAction(){
		return action;
	}
	
	public String getBillCode(){
		return billCode;
	}
	
	public AggregatedValueObject getBizEntity(){
		return bizEntity;
	}
	
	public AggregatedValueObject[] getBizEntities(){
		return bizEntities;
	}
	
	/*
	 * Custom Properties
	 */
	public Object getCustomProperty(String key){
		if (customProperties.containsKey(key))
			return customProperties.get(key);
		return null;
	}
	
	public void putCustomProperty(String key, Object obj){
		customProperties.put(key, obj);
	}
	
	public void removeCustomProperty(String key){
		if (customProperties.containsKey(key))
			customProperties.remove(key);
	}
}
