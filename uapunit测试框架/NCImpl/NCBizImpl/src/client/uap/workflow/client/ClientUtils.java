
package uap.workflow.client;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import uap.workflow.bizimpl.BizAction;
import uap.workflow.bizimpl.BizContext;
import uap.workflow.bizitf.bizaction.IBizActionHelper;

public class ClientUtils {
	
	/*
	 * Login Context
	 */
	InvocationInfo loginContext = new InvocationInfo();
	
	public void forward(BizContext context){
		
	}
	
	public void start(BizContext context){
	
	}
	
	public void backward(BizContext context){
	}
	
	public void discard(BizContext context){
		
	}
	
	private void getLoginContext(){
		loginContext.setUserId(InvocationInfoProxy.getInstance().getUserId());
		loginContext.setGroupId(InvocationInfoProxy.getInstance().getGroupId());
	}
	
	
	public BizAction taskQuery(String userID, String transType, String billID){
		//TODO 调用查询服务
		return null;
	}
	
	public BizAction getCurrentAction(String transType, String billID){
		getLoginContext();
		String userID = loginContext.getUserId();
		
		BizAction action = taskQuery(userID, transType, billID);
		if (action == null)
			return null;
		//TODO call Engine task query 
		IBizActionHelper helper = NCLocator.getInstance().lookup(IBizActionHelper.class);
		helper.getBizActionByAction(action.getBizObjectType().getTransType(), action.getBizActionType().getAction());
		return action;
	}
}
