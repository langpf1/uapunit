package uap.workflow.bizimpl.expression;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.org.IGroupQryService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.function.NcInnerFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class SystemEnvVariables extends NcInnerFunction {

	public SystemEnvVariables() {
		numberOfParameters = 0;
		functionType = IFormulaConstant.FUN_CUSTOM;
		functionDesc ="获取环境信息"; 
	}
	/*
	 * 环境变量函数
	 */
	
//	setVariable("SYSOPERATOR", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//	setVariable("SYSGROUP", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getGroupId(),String.class));
//	setVariable("BUZIDATE", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//	setVariable("BUZITIME", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//	setVariable("SYSDATE", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//	setVariable("SYSTIME", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//
//	setVariable("DESTTRANSTYPE", new ObjectValueExpression(null, null,String.class));
//	setVariable("DESTBILLTYPE", new ObjectValueExpression(null, null,String.class));
	public static String getUserCode(){
		String userID = InvocationInfoProxy.getInstance().getUserId();
		IUserManageQuery qry = NCLocator.getInstance().lookup(IUserManageQuery.class);
		try {
			return qry.getUser(userID).getUser_code();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getUserID(){
		return InvocationInfoProxy.getInstance().getUserId();		
	}
	
	public static String getGroupCode(){
		String groupID = InvocationInfoProxy.getInstance().getGroupId();
		IGroupQryService qry = NCLocator.getInstance().lookup(IGroupQryService.class);
		try {
			GroupVO[] groups = (GroupVO[])qry.queryAllGroupVOSByCodeS(new String[]{groupID});
			return groups != null && groups.length >0 ? groups[0].getCode() : null; 
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getGroupID(){
		return InvocationInfoProxy.getInstance().getGroupId();
	}
	
	public static UFDate getBizDate(){
		return new UFDate(InvocationInfoProxy.getInstance().getBizDateTime());
	}
	
	public static UFDateTime getBizDateTime(){
		return new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime());
	}
	
	public static String getLangCode(){
		return InvocationInfoProxy.getInstance().getLangCode();
	}
//	public static long getLogonDate(){
//		InvocationInfoProxy.getInstance().get
//		return 0;
//	}
//	
//	public static long getLogonTime(){
//		return 0;
//	}
}
