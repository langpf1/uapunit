package uap.workflow.engine.el;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import nc.vo.pub.formulaset.function.*;
import uap.workflow.bizimpl.expression.RetriveExchangeResult;
import uap.workflow.bizimpl.expression.SystemEnvVariables;
import uap.workflow.engine.javax.FunctionMapper;

public class ExtFunctions extends FunctionMapper {

	Map<String, Method> map = Collections.emptyMap();

	public ExtFunctions(){
		try {
			//环境变量函数
			setFunction("","getUserCode",SystemEnvVariables.class.getMethod("getUserCode"));
			setFunction("","getGroupCode",SystemEnvVariables.class.getMethod("getGroupCode"));
			setFunction("","getBizDate",SystemEnvVariables.class.getMethod("getBizDate"));
			setFunction("","getBizDateTime",SystemEnvVariables.class.getMethod("getBizDateTime"));
			setFunction("","getLangCode",SystemEnvVariables.class.getMethod("getLangCode"));
			setFunction("","getUserID",SystemEnvVariables.class.getMethod("getUserID"));
			setFunction("","getGroupID",SystemEnvVariables.class.getMethod("getGroupID"));
//			setVariable("SYSDATE", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//			setVariable("SYSTIME", new ObjectValueExpression(null, InvocationInfoProxy.getInstance().getUserId(),String.class));
//			setVariable("DESTTRANSTYPE", new ObjectValueExpression(null, null,String.class));
//			setVariable("DESTBILLTYPE", new ObjectValueExpression(null, null,String.class));

			//合计函数
			setFunction("","max",CommonFunction.class.getMethod("max", Double[].class));
			setFunction("","min",CommonFunction.class.getMethod("min", Double[].class));
			setFunction("","avarvge",CommonFunction.class.getMethod("avarvge", Double[].class));
			setFunction("","sum",CommonFunction.class.getMethod("sum", Double[].class));
			//字符串函数
			setFunction("","subString",CommonFunction.class.getMethod("subString", int.class, int.class, String.class));
			setFunction("","concat",CommonFunction.class.getMethod("concat", String.class, String.class));
			setFunction("","contains",CommonFunction.class.getMethod("contains", String.class, String.class));
			setFunction("","replace",CommonFunction.class.getMethod("replace", String.class, String.class));
			setFunction("","valueOf",CommonFunction.class.getMethod("valueof", Object.class));
	
			setFunction("","charAt",CommonFunction.class.getMethod("charAt", String.class, int.class));
			//setFunction("","corate",CommonFunction.class.getMethod("corate", Object.class));
			setFunction("","endsWith",CommonFunction.class.getMethod("endsWith", String.class, String.class));
			setFunction("","equalsIgnoreCase",CommonFunction.class.getMethod("equalsIgnoreCase", String.class, String.class));
			setFunction("","indexof",CommonFunction.class.getMethod("indexof", String.class, String.class));
			setFunction("","lastindexof",CommonFunction.class.getMethod("lastindexof", String.class, String.class));
			setFunction("","left",CommonFunction.class.getMethod("left", String.class, int.class));
			setFunction("","leftStr",CommonFunction.class.getMethod("leftStr", String.class, int.class, String.class));
			setFunction("","length",CommonFunction.class.getMethod("length", String.class));
			setFunction("","mid",CommonFunction.class.getMethod("mid", String.class, int.class, int.class));
			setFunction("","right",CommonFunction.class.getMethod("right", String.class, int.class));
			setFunction("","rightStr",CommonFunction.class.getMethod("rightStr", String.class, int.class, String.class));
			setFunction("","startswith",CommonFunction.class.getMethod("startswith", String.class, String.class));
			setFunction("","todecimal",CommonFunction.class.getMethod("todecimal", String.class));
			setFunction("","toLowerCase",CommonFunction.class.getMethod("toLowerCase", String.class));
			setFunction("","tostring",CommonFunction.class.getMethod("tostring", Object.class));
			setFunction("","touppercase",CommonFunction.class.getMethod("touppercase", String.class));
			setFunction("","trimzero",CommonFunction.class.getMethod("trimzero", String.class));

			
			//日期时间函数
			setFunction("", "compareDate",  CommonFunction.class.getMethod("compareDate", Object.class, Object.class, Object.class)); 
			setFunction("", "date",  CommonFunction.class.getMethod("date")); 
			setFunction("", "dateAdd",  CommonFunction.class.getMethod("dateAdd", Object.class, Object.class, Object.class)); 
			setFunction("", "formatDate",  CommonFunction.class.getMethod("formatDate", Object.class, Object.class)); 
			setFunction("", "datetime",  CommonFunction.class.getMethod("datetime")); 
			setFunction("", "dayof",  CommonFunction.class.getMethod("dayof", Object.class)); 
			setFunction("", "time",  CommonFunction.class.getMethod("time"));
			setFunction("", "year",  CommonFunction.class.getMethod("year"));
			setFunction("", "yearof",  CommonFunction.class.getMethod("yearof", Object.class));
			setFunction("", "todate",  CommonFunction.class.getMethod("todate", Object.class));
			setFunction("", "todatetime",  CommonFunction.class.getMethod("todatetime", Object.class));
			setFunction("", "toTime",  CommonFunction.class.getMethod("toTime", Object.class));
			
			//数学
//			abs( )
//			acos( )
//			acosh( )
//			add( , )
//			angle( , )
//			asin( )
//			asinh( )
//			atan( )
//			atanh( )
//			cos( )
//			cosh( )
//			div( , )
//			exp( )
//			getresult()
//			int( )
//			ln( )
//			log( )
//			max( , )
//			min( , )
//			mod( , )
//			mul( , )
//			rand()
//			round( , )
//			sgn( )
//			sin( )
//			sinh( )
//			sqrt( )
//			sub( , )
//			sum()
//			tan( )
//			tanh( )
//			tonumber( )
//			zeroifnull( )
			
			
			//其他
			setFunction("", "iif",  CommonFunction.class.getMethod("iif",boolean.class,Object.class, Object.class));

			setFunction("", "getExchangedResult",  
					RetriveExchangeResult.class.getMethod("getExchangedResult",Map.class,String.class));
			
			//审批变量
			setFunction("", "getApproveResult",  ApproveResultVariables.class.getMethod("getApproveResult",new Class<?>[0]));
			setFunction("", "getApproveNote",  ApproveResultVariables.class.getMethod("getApproveNote",new Class<?>[0]));
			setFunction("", "getAttachments",  ApproveResultVariables.class.getMethod("getAttachments",new Class<?>[0]));
			setFunction("", "getCCTo",  ApproveResultVariables.class.getMethod("getCCTo",new Class<?>[0]));
			setFunction("", "getAssignedTo",  ApproveResultVariables.class.getMethod("getAssignedTo",new Class<?>[0]));
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Method resolveFunction(String prefix, String localName) {
		return map.get(prefix + ":" + localName);
	}

	public void setFunction(String prefix, String localName, Method method) {
		if (map.isEmpty()) {
			map = new HashMap<String, Method>();
		}
		map.put(prefix + ":" + localName, method);
	}

}
