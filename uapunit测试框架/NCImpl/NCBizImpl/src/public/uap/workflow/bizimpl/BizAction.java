
package uap.workflow.bizimpl;

import java.io.Serializable;

/*
 * ҵ����󼰲������壬���̵����в����Դ�Ϊ��Ԫ
 */
public class BizAction implements Serializable {

	private static final long serialVersionUID = -2827088470742592930L;
	
	private BizObjectType bizObjectType;
	
	
	private BizActionType bizActionType;
	
	public BizAction(BizObjectType object, BizActionType action){
		bizObjectType = object;
		bizActionType = action;
	}

	public BizObjectType getBizObjectType(){
		return bizObjectType;
	}
	
	public void setObjectType(BizObjectType objectType){
		bizObjectType = objectType;
	}

	public BizActionType getBizActionType(){
		return bizActionType;
	}
	
	public void setActionType(BizActionType objectType){
		bizActionType = objectType;
	}

}
