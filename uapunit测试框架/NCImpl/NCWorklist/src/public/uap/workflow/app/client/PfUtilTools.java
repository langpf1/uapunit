package uap.workflow.app.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.sf.IConfigFileService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.rbac.role.RoleVO;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.action.IplatFormEntry;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.app.vo.PfDataTypes;
import uap.workflow.app.vo.PfOperatorTypes;
import uap.workflow.app.vochange.IPfExchangeService;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.vos.AssignableInfo;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 流程平台后台工具类
 * <li>注意：只可后台调用
 * 
 * @author 樊冠军 2002-4-12 12:29:25
 * @modifier 雷军 2004-03-09 增加解析单据动作约束不满足的信息提示
 * @modifier leijun 2006-4-29 BOOLEAN类型也要支持右值判定，如果无右值，则直接计算左值。
 * @modifier leijun 2008-12 返回String类型的单据函数支持更多比较运算符：not like,in,not in
 * 
 * @modifier leijun 2009-3 修改VO交换的逻辑，改为从数据库读取交换规则
 */
public class PfUtilTools  {

	private static IPfExchangeService exchangeService;

	/**
	 * 实例化与某单据类型相关联的类
	 * <li>前提：从单据类型可获取类所在的模块名
	 * 
	 * @param pkBilltype 单据类型PK
	 * @param checkClsName 类名
	 * @return 类实例
	 * @throws BusinessException
	 */
	public static Object instantizeObject(String pkBilltype, String checkClsName)
			throws BusinessException {
		//XXX:该接口的实现使用了后台缓存
		IPFMetaModel pfmeta = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());
		String strModule = pfmeta.queryModuleOfBilltype(pkBilltype);
		if (StringUtil.isEmptyWithTrim(strModule))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000050", null, new String[]{pkBilltype})/*该单据类型{0}没有定义所属的模块*/);
		return NewObjectService.newInstance(strModule, checkClsName);
	}

	/**
	 * 获取注册在bd_billtype.checkclassname中类的实例
	 * <li>只可后台调用
	 * 
	 * @param billType 单据类型（或交易类型）PK
	 * @return
	 * @throws BusinessException
	 */
	public static Object getBizRuleImpl(String billTypeStr) throws BusinessException {
		//transType new BillTypeCacheKey().buildBilltype(transType).buildPkGroup(pk_group)
		BillTypeCacheKey billType = 
			new BillTypeCacheKey().buildBilltype(billTypeStr).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId());
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(billType);
		if (!StringUtil.isEmptyWithTrim(btVO.getCheckclassname()))
			return findBizImplOfBilltype(billType.getBilltype(), btVO.getCheckclassname().trim());

		String strTypeCode = PfUtilBaseTools.getRealBilltype(billTypeStr);
		btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(strTypeCode));
		if (!StringUtil.isEmptyWithTrim(btVO.getCheckclassname()))
			return findBizImplOfBilltype(billType.getBilltype(), btVO.getCheckclassname().trim());
		return null;
	}
	
	
	/**
	 * 获取注册在bd_billtype.emendEnumClass中类的实例
	 * <li>只可后台调用
	 * 
	 * @param billType 单据类型（或交易类型）PK
	 * @return
	 * @throws BusinessException
	 */
	public static Object getEmendFlowImpl(String billTypeStr) throws BusinessException {
		//transType new BillTypeCacheKey().buildBilltype(transType).buildPkGroup(pk_group)
		BillTypeCacheKey billType = 
			new BillTypeCacheKey().buildBilltype(billTypeStr).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId());
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(billType);
		if (!StringUtil.isEmptyWithTrim(btVO.getEmendEnumClass()))
			return findBizImplOfBilltype(billType.getBilltype(), btVO.getEmendEnumClass().trim());

		String strTypeCode = PfUtilBaseTools.getRealBilltype(billTypeStr);
		btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(strTypeCode));
		if (!StringUtil.isEmptyWithTrim(btVO.getEmendEnumClass()))
			return findBizImplOfBilltype(billType.getBilltype(), btVO.getEmendEnumClass().trim());
		return null;
	}
	

	/**
	 * 获得单据类型中注册的业务实现类的实例
	 * <li>只可后台调用
	 * 
	 * @param billType 单据类型PK
	 * @param clzName 业务类名称
	 * @return
	 * @throws BusinessException
	 */
	public static Object findBizImplOfBilltype(String billType, String clzName)
			throws BusinessException {
		if (StringUtil.isEmptyWithTrim(clzName))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000051", null, new String[]{billType})/*流程平台：单据类型{0}注册的业务类名称为空*/);
		return instantizeObject(billType, clzName.trim());
	}

	/**
	 * 获得指定类明的类定义
	 * 
	 * @param className
	 * @return
	 * @throws BusinessException
	 */
	public static Class getClassByName(String className) throws BusinessException {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new PFBusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 返回当前使用数据源名称
	 * 
	 * @return
	 */
	public static String getCurrentDatabase() {
		String userDataSource = InvocationInfoProxy.getInstance().getUserDataSource();
		if (userDataSource == null)
			userDataSource = "design";
		return userDataSource;
	}

	/**
	 * 计算宏表达式的值 <BR>
	 * 注意：目前 仅支持六种表达式。
	 * 
	 * @param macro 宏表达式，带有宏标记
	 * @param type 表达式类型
	 * @param leftValue 左值
	 * @param rightValue 右值
	 * @return String
	 * @author 雷军
	 */
	private static String getExpressValue(String macro, String type, Object leftValue,
			Object rightValue) {
		// 目前所有支持的宏表达式
		// String[] macros = new String[] { "%%L%%", "%%R%%", "%%L-R%%",
		// "%%R-L%%", "%%L+R%%", "%%R+L%%" };

		if (macro.equalsIgnoreCase("L")) {
			return leftValue.toString();
		} else if (macro.equalsIgnoreCase("R")) {
			return rightValue.toString();
		} else if (macro.equalsIgnoreCase("L-R")) {
			if (type.equalsIgnoreCase("INTEGER")) {
				return Integer.valueOf(((Integer) leftValue).intValue() - ((Integer) rightValue).intValue())
						.toString();
			} else if (type.equalsIgnoreCase("Double")) {
				double value = ((UFDouble) leftValue).doubleValue()
						- Double.parseDouble((String) rightValue);
				return String.valueOf(value);
			}
		} else if (macro.equalsIgnoreCase("R-L")) {
			if (type.equalsIgnoreCase("INTEGER")) {
				return Integer.valueOf(((Integer) rightValue).intValue() - ((Integer) leftValue).intValue())
						.toString();
			} else if (type.equalsIgnoreCase("Double")) {
				double value = ((UFDouble) rightValue).doubleValue()
						- Double.parseDouble((String) leftValue);
				return String.valueOf(value);
			}
		} else if (macro.equalsIgnoreCase("L+R") || macro.equalsIgnoreCase("R+L")) {
			if (type.equalsIgnoreCase("INTEGER")) {
				return Integer.valueOf(((Integer) leftValue).intValue() + ((Integer) rightValue).intValue())
						.toString();
			} else if (type.equalsIgnoreCase("Double")) {
				double value = ((UFDouble) leftValue).doubleValue()
						+ Double.parseDouble((String) rightValue);
				return String.valueOf(value);
			}
		}
		return macro;
	}

	/**
	 * 功能：判断关系式是否成功。 类型Type;字符型"STRING"、整型“INTEGER“、浮点型"DOUBLE"、布尔型"BOOLEAN"
	 * 字符型运算符为:等于"=",包含于"like" 整型运算符为: 等于"=",大于等于">=",小于等于" <=",不等于"!="
	 * 大于">",小于" <" 浮点型运算符为:等于"=",大于等于">=",小于等于" <=",不等于"!=" 大于">",小于" <"
	 * 布尔型BOOLEAN运算符为:等于"=".
	 * 
	 * @param InObject 左值
	 * @param objType 左值类型，仅支持"BOOLEAN","STRING","INTEGER","DOUBLE"
	 * @param opType 比较符
	 * @param value 右值
	 * @return
	 * @throws BusinessException
	 * @modifier leijun 2007-9-1 左值类型支持"VOID"
	 */
	public static boolean isTrueOrNot(Object InObject, String objType, String opType, String value)
			throws BusinessException {
		Logger.debug("****比较类型:" + objType + "比较类型运算符:" + opType + "****");

		if (PfDataTypes.VOID.getTag().equals(objType))
			// XXX:如果左值函数返回值为"VOID"，则永真
			return true;
		else if (PfDataTypes.UFBOOLEAN.getTag().equals(objType))
			return compareBoolean((UFBoolean) InObject, opType, value);
		else if (PfDataTypes.STRING.getTag().equals(objType))
			return compareString((String) InObject, opType, value);
		else if (PfDataTypes.INTEGER.getTag().equals(objType))
			return compareInteger((Integer) InObject, opType, value);
		else if (PfDataTypes.UFDOUBLE.getTag().equals(objType))
			return compareDouble((UFDouble) InObject, opType, value);

		return false;
	}

	/**
	 * 判断Double型的真假
	 * 
	 * @param InObject 左值
	 * @param opType 比较运算符
	 * @param value 右值
	 * @return
	 */
	private static boolean compareDouble(UFDouble InObject, String opType, String value) {
		// 判断Double型的真假
		if (InObject == null) {
			InObject = new UFDouble(0);
		}
		if (value == null) {
			value = "0";
		}
		// XXX::V5前的版本中函数表达式运算符为"="，V5修改为"=="
		// if (PfOperatorTypes.EQ.toString().equals(opType)) {
		if (PfOperatorTypes.EQ.toString().equals(opType) || "=".equals(opType)) {
			if (InObject.doubleValue() == Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.GE.toString().equals(opType)) {
			if (InObject.doubleValue() >= Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LE.toString().equals(opType)) {
			if (InObject.doubleValue() <= Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.NE.toString().equals(opType)) {
			if (InObject.doubleValue() != Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LT.toString().equals(opType)) {
			if (InObject.doubleValue() < Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.GT.toString().equals(opType)) {
			if (InObject.doubleValue() > Double.parseDouble(value)) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	/**
	 * 判断Integer型的真假
	 * 
	 * @param InObject 左值
	 * @param opType 比较运算符
	 * @param value 右值
	 * @return
	 */
	private static boolean compareInteger(Integer InObject, String opType, String value) {
		Integer typeInteger;
		// 判断Integer型的真假
		if (InObject == null) {
			InObject = Integer.valueOf(0);
		}
		if (value == null) {
			value = "0";
		}
		typeInteger = (Integer) InObject;
		// XXX::V5前的版本中函数表达式运算符为"="，V5修改为"=="
		// if (PfOperatorTypes.EQ.toString().equals(opType)) {
		if (PfOperatorTypes.EQ.toString().equals(opType) || "=".equals(opType)) {
			if (typeInteger.intValue() == Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.GE.toString().equals(opType)) {
			if (typeInteger.intValue() >= Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LE.toString().equals(opType)) {
			if (typeInteger.intValue() <= Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.NE.toString().equals(opType)) {
			if (typeInteger.intValue() != Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LT.toString().equals(opType)) {
			if (typeInteger.intValue() < Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.GT.toString().equals(opType)) {
			if (typeInteger.intValue() > Integer.parseInt(value)) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	/**
	 * 判断String型的真假
	 * 
	 * @param strLeftValue 左值
	 * @param opType 比较运算符
	 * @param strRightValue 右值
	 * @return
	 */
	private static boolean compareString(String strLeftValue, String opType, String strRightValue) {
		if (strLeftValue == null)
			return false;
		// XXX::V5前的版本中函数表达式运算符为"="，V5修改为"=="
		if (PfOperatorTypes.EQ.toString().equals(opType) || "=".equals(opType)) {
			if (strLeftValue.equals(strRightValue)) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LIKE.toString().equals(opType)) {
			if (strLeftValue.indexOf(strRightValue) > -1) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.NOTLIKE.toString().equals(opType)) {
			if (strLeftValue.indexOf(strRightValue) == -1) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.IN.toString().equals(opType)) {
			String[] tokens = StringUtil.split(strRightValue, ",");
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0; i < (tokens == null ? 0 : tokens.length); i++) {
				hs.add(tokens[i]);
			}
			return hs.contains(strLeftValue);
		} else if (PfOperatorTypes.IN.toString().equals(opType)) {
			String[] tokens = StringUtil.split(strRightValue, ",");
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0; i < (tokens == null ? 0 : tokens.length); i++) {
				hs.add(tokens[i]);
			}
			return !hs.contains(strLeftValue);
		} else if (PfOperatorTypes.GE.toString().equals(opType)) {
			if (strLeftValue.compareTo(strRightValue) >= 0) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.LE.toString().equals(opType)) {
			if (strLeftValue.compareTo(strRightValue) <= 0) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.NE.toString().equals(opType)) {
			if (strLeftValue.equals(strRightValue)) {
				return false;
			} else {
				return true;
			}
		} else if (PfOperatorTypes.LT.toString().equals(opType)) {
			if (strLeftValue.compareTo(strRightValue) < 0) {
				return true;
			} else {
				return false;
			}
		} else if (PfOperatorTypes.GT.toString().equals(opType)) {
			if (strLeftValue.compareTo(strRightValue) > 0) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	/**
	 * 判断boolean型的真假
	 * 
	 * @param InObject 左值
	 * @param opType 比较运算符
	 * @param value 右值
	 * @return
	 */
	private static boolean compareBoolean(UFBoolean InObject, String opType, String value) {
		// //lj@2006-4-29 BOOLEAN类型也要支持右值判定

		// XXX::V5前的版本中函数表达式运算符为"="，V5修改为"=="
		// if (PfOperatorTypes.EQ.toString().equals(opType)) {
		if (PfOperatorTypes.EQ.toString().equals(opType) || "=".equals(opType)) {
			if (InObject.equals(UFBoolean.valueOf(value)))
				return true;
			else
				return false;
		} else
			// 如果没有比较符，则直接计算左值
			return InObject.booleanValue();
	}

	/**
	 * 执行函数并返回结果
	 * 
	 * @param functionNote 函数方法说明
	 * @param className 类名
	 * @param method 方法名
	 * @param parameter 参数
	 * @param paraVo 工作流上下文参数
	 * @param methodReturnHas
	 * @return
	 * @throws BusinessException
	 */
	public static Object parseFunction(String functionNote, String className, String method,
			String parameter, PfParameterVO paraVo)
			throws BusinessException {

		String errString;
		Object checkObject = null;

		Logger.debug("parseFunction解析函数类名:" + className + "方法名:" + method + "参数:" + parameter + "开始");
		if (className == null) {
			errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000052")/*parseFunction无类名，无法运行函数*/;
			throw new PFBusinessException(errString);
		} else if (method == null) {
			errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000053")/*parseFunction无方法名，无法运行函数*/;
			throw new PFBusinessException(errString);
		}
		// 执行类的方法
		checkObject = runClass(className, method, parameter, paraVo, null);
		if (functionNote != null) {
			Logger.debug("函数#" + functionNote + "#运行返回值为：" + String.valueOf(checkObject));
		} else {
			Logger.debug("函数运行返回值为：" + String.valueOf(checkObject));
		}
		Logger.debug("parseFunction解析函数类名:" + className + "方法名:" + method + "参数:" + parameter
				+ "****结束");
		return checkObject;
	}

	/**
	 * 解析属性参数
	 * 
	 * @param dealStr
	 * @param paraObjects
	 * @param paraClasses
	 * @param arrIndex
	 * @param paraVo
	 * @param methodReturnHas
	 * @throws BusinessException
	 */
	private static void parseParmeter(String dealStr, Object[] paraObjects, Class[] paraClasses,
			int arrIndex, PfParameterVO paraVo)
			throws BusinessException {
		String fieldName = null;
		String datatype = null;
		int retIndex = dealStr.indexOf(":");
		if (retIndex < 0) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000054")/*注册参数不正确*/);
		} else {
			try {
				// 通过参数名称前的第一字符标号区分组件返回属性返回COM..组件返回
				fieldName = dealStr.substring(0, retIndex);
				if (fieldName.startsWith("OBJ")) {
					// 表示为 用户自定义参数
					if (fieldName.endsWith("ARY")) {
						paraObjects[arrIndex] = paraVo.m_userObjs;
					} else {
						paraObjects[arrIndex] = paraVo.m_userObj;
					}
				} else {
					// 表示为 属性参数（主要是主表的一些数据）
					paraObjects[arrIndex] = paraVo.m_standHeadVo.getAttributeValue(fieldName);
				}
				datatype = dealStr.substring(retIndex + 1);
				paraClasses[arrIndex] = PfUtilBaseTools.parseTypeClass(datatype);
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000055")/*未找到注册的类文件*/, ex);
			}
		}
	}

	public static boolean parseSyntax(String className, String method, String parameter,
			String funNote, String returnType, String ysf, String value, String className2,
			String method2, String parameter2, String funNote2, PfParameterVO paraVo
			) throws BusinessException {
		return parseSyntax(className, method, parameter, funNote, returnType, ysf, value, className2,
				method2, parameter2, funNote2, paraVo, null, null);
	}

	public static boolean parseSyntax(String className, String method, String parameter,
			String funNote, String returnType, String ysf, String value, String className2,
			String method2, String parameter2, String funNote2, PfParameterVO paraVo,
			StringBuffer errorMessageBuffer) throws BusinessException {
		return parseSyntax(className, method, parameter, funNote, returnType, ysf, value, className2,
				method2, parameter2, funNote2, paraVo, errorMessageBuffer, null);
	}

	/**
	 * 功能：用于解析语法判断条件
	 * 
	 * @author 雷军 2004-03-09 单据动作约束不满足提示信息通过参数errorMessageBuffer传出
	 * 
	 * @param className 类名（左）
	 * @param method 方法名（左）
	 * @param parameter 参数
	 * @param funNote 方法说明
	 * @param returnType 左函数返回值类型
	 * @param ysf 运算符
	 * @param value 值
	 * @param className2 类名（右）
	 * @param method2 方法名（右）
	 * @param parameter2 参数
	 * @param funNote2 方法说明
	 * @param paraVo 工作流上下文参数
	 * @param methodReturnHas
	 * @param errorMessageBuffer 返回的消息串
	 * @param originalHintBuffer 宏消息串
	 * @return
	 * @throws BusinessException
	 */
	public static boolean parseSyntax(String className, String method, String parameter,
			String funNote, String returnType, String ysf, String value, String className2,
			String method2, String parameter2, String funNote2, PfParameterVO paraVo,
			StringBuffer errorMessageBuffer, StringBuffer originalHintBuffer)
			throws BusinessException {

		String strTmp = "";
		Object leftObject = null, rightObject = null;

		strTmp = "parseSyntax解析语法开始";
		Logger.debug(strTmp);
		if (className == null) {
			// 发送消息
			Logger.debug("parseSyntax解析语法结束");
			return false;
		}

		// 返回变量
		boolean retBool = true;
		// 运行左值函数类
		leftObject = parseFunction(funNote, className, method, parameter, paraVo);
		if (className2 == null) {
			// 返回的值直接与用户输入比较
			Logger.debug("用户输入的右值为：" + value);
		} else {
			// 运行右值函数类
			rightObject = parseFunction(funNote2, className2, method2, parameter2, paraVo);
			value = String.valueOf(rightObject);
			Logger.debug("右函数的运行值为:" + value);
		}

		// 判断表达式
		retBool = isTrueOrNot(leftObject, returnType.toUpperCase(), ysf, value);
		if (retBool) {
			Logger.debug("函数运行结果判断成功!");
		} else {
			Logger.debug("函数运行结果判断不成功!");
			if (errorMessageBuffer != null) {
				errorMessageBuffer.append(funNote);
				errorMessageBuffer.append("\n");
				errorMessageBuffer.append(leftObject);
				errorMessageBuffer.append("\n");
				errorMessageBuffer.append(ysf);
				errorMessageBuffer.append("\n");
				errorMessageBuffer.append(funNote2);
				errorMessageBuffer.append("\n");
				errorMessageBuffer.append(rightObject);
			}

			// added by 雷军 2004-03-09 解析单据动作约束不满足时的信息提示宏表达式
			String MACRO_TAG = "%%";
			String originalHint = "";
			if (originalHintBuffer != null) {
				originalHint = originalHintBuffer.toString();
				String hintAfterParse = translateMacro(originalHint, MACRO_TAG, returnType, leftObject,
						value);
				originalHintBuffer.append(hintAfterParse);
			}
		}

		strTmp = "parseSyntax解析语法结束";
		Logger.debug(strTmp);
		return retBool;
	}

	/**
	 * 进行VO交换 <li>不支持分单
	 * 
	 * @param srcTranstype 源交易类型PK
	 * @param destTranstype 目的交易类型PK
	 * @param sourceBillVO 源单据聚合VO
	 * @return 目的单据聚合VO
	 * @throws BusinessException
	 */
	public static AggregatedValueObject runChangeData(String srcTranstype, String destTranstype,
			AggregatedValueObject sourceBillVO) throws BusinessException {
		return getExchangeService().runChangeData(srcTranstype, destTranstype, sourceBillVO, null);
	}

	/**
	 * 进行VO交换 <li>不支持分单
	 * 
	 * @param srcBillOrTranstype 源交易类型PK
	 * @param destBillOrTranstype 目的交易类型PK
	 * @param sourceBillVO 源单据聚合VO
	 * @param paraVo 工作流参数VO
	 * @return 目的单据聚合VO
	 * @throws BusinessException
	 */
	public static AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject sourceBillVO, PfParameterVO paraVo)
			throws BusinessException {
		return getExchangeService().runChangeData(srcBillOrTranstype, destBillOrTranstype, sourceBillVO, paraVo);
		
	}

	private synchronized static IPfExchangeService getExchangeService() {
		if(exchangeService==null)
			exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		return exchangeService;
	}
	
	/**
	 * 运行数据交换类(VO数组) <li>支持分单
	 * 
	 * @param sourceBillType 源单据类型PK
	 * @param destBillType 目的单据类型PK
	 * @param sourceBillVOs 源单据聚合VO数组
	 * @return 目的单据聚合VO数组
	 * @throws BusinessException
	 */
	public static AggregatedValueObject[] runChangeDataAry(String sourceBillType,
			String destBillType, AggregatedValueObject[] sourceBillVOs) throws BusinessException {

		return getExchangeService().runChangeDataAry(sourceBillType, destBillType, sourceBillVOs, null);
	}

	/**
	 * 运行数据交换类(VO数组) <li>支持分单
	 * 
	 * @param srcBillOrTranstype 源单据类型PK
	 * @param destBillOrTranstype 目的单据类型PK
	 * @param sourceBillVOs  源单据聚合VO数组
	 * @param paraVo 工作流参数VO，仅用于获取一些系统变量
	 * @return 目的单据聚合VO数组
	 * @throws BusinessException
	 */
	public static AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo)
			throws BusinessException {

		return getExchangeService().runChangeDataAry(srcBillOrTranstype, destBillOrTranstype, sourceBillVOs, paraVo);
	}

	

	/**
	 * 根据来源、目的单据类型，实例化某个类
	 * <li>优先在目的单据类型所属模块 查找类
	 * 
	 * @param sourceBillType 源单据类型PK
	 * @param destBillType 目的单据类型PK
	 * @param fullyQualifiedClassName 类名
	 * @return 类实例
	 * @throws BusinessException
	 */
	public static Object newImplOfClz(String sourceBillType, String destBillType,
			String fullyQualifiedClassName) throws BusinessException {
		Object changeObj = null;
		IPFMetaModel pfmeta = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());

		// 目的单据类型 或其父 所属模块
		String moduleOfDest = pfmeta.queryModuleOfBilltype(destBillType);
		// 源单据类型所属模块
		String moduleOfSrc = pfmeta.queryModuleOfBilltype(sourceBillType);

		if (moduleOfDest == null || moduleOfDest.trim().length() == 0) {
			if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000056")/*错误：源或目的单据类型都没有注册模块名*/);
			else {
				changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
				Logger.debug("OK-目的单据没有所属模块，但在来源单据所属模块" + moduleOfSrc + "中找到交换类=" + changeObj);
			}
		} else {
			// 优先在目的单据类型所属模块 查找类
			try {
				changeObj = NewObjectService.newInstance(moduleOfDest, fullyQualifiedClassName);
				Logger.debug("OK-在目的单据所属模块" + moduleOfDest + "中找到交换类=" + changeObj);
			} catch (Exception e) {
				if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000057")/*错误：VO交换类不在目的单据类型所属模块中，且来源单据没有注册所属模块*/);
				else {
					changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
					Logger.debug("OK-在目的单据所属模块中找不到交换类，但在来源单据所属模块" + moduleOfSrc + "中找到交换类=" + changeObj);
				}
			}
		}

		Class c = changeObj.getClass();
		Logger.debug(">>>OBJ=" + changeObj + ";CL=" + c.getProtectionDomain().getClassLoader());
		Logger.debug(">>>LOC=" + c.getProtectionDomain().getCodeSource().getLocation());
		return changeObj;
	}

	/**
	 * 运行类
	 * 
	 * @param className 运行的类名称
	 * @param method 方法名称
	 * @param parameter 参数
	 * @param paraVo 工作流参数VO
	 * @param keyHas 参数值Hash
	 * @param methodReturnHas 返回值Hash
	 * 
	 * @modifier leijun 2005-6-20 单据类型PK最大限制为4字符,非固定的2个字符
	 */
	public static Object runClass(String className, String method, String parameter,
			PfParameterVO paraVo, Hashtable keyHas) throws BusinessException {
		Logger.debug("**********执行类PfUtilTools.runClass()开始************");

		Logger.debug("执行类:" + className + ";方法:" + method + ";参数:" + parameter);

		long begin = System.currentTimeMillis();

		// /1.解析参数，因为参数是以","分割的
		// String[] paraStrs = nc.vo.pf.pub.PfComm.dealString(parameter, ",");
		StringTokenizer tmpStrToken = new StringTokenizer(parameter, ",");
		String[] paraStrs = new String[tmpStrToken.countTokens()];
		int index = 0;
		while (tmpStrToken.hasMoreTokens())
			paraStrs[index++] = tmpStrToken.nextElement().toString();

		Object[] paraObjects = new Object[paraStrs.length];
		Class[] paraClasses = new Class[paraStrs.length];

		for (int i = 0; i < paraStrs.length; i++) {
			// 遍历参数串
			// 如果含有"."，则表示VO参数
			boolean isVo = paraStrs[i].indexOf(".") > 0 ? true : false;
			int colonPos = paraStrs[i].indexOf(":");

			if (paraStrs[i].startsWith("&")) {
				// 1.如果以"&"开头，则表示运行参数
				String paramKey = paraStrs[i].substring(1, colonPos);
				String strDataType = paraStrs[i].substring(colonPos + 1);
				Object valueObj = keyHas == null ? null : keyHas.get(paramKey);

				if (isVo) {
					if (strDataType.endsWith("[]")) {
						strDataType = strDataType.substring(0, strDataType.length() - 2);
						int intAryLen = 0;
						if (valueObj instanceof AggregatedValueObject[]) {
							intAryLen = ((AggregatedValueObject[]) valueObj).length;
						} else if (valueObj instanceof nc.vo.pub.ValueObject[]) {
							intAryLen = ((nc.vo.pub.ValueObject[]) valueObj).length;
						}
						paraClasses[i] = PfUtilTools.getClassNameClass(strDataType, intAryLen);
					} else {
						paraClasses[i] = getClassByName(strDataType);
					}
				} else {
					paraClasses[i] = PfUtilBaseTools.parseTypeClass(strDataType);
				}
				paraObjects[i] = valueObj;
				// XXX::直接再次遍历
				continue;
			}

			if (isVo) {
				// 2.参数中包含VO类名
				if (colonPos < 0)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000058")/*参数注册处VO必须重新注册*/);
				// 获取单据类型和VO类名 lj@2005-6-17
				String tmpbillType = paraStrs[i].substring(colonPos + 1);
				String voClassName = paraStrs[i].substring(0, colonPos);
				/***************************************************************
				 * 从单据Vo与单据类型对照表中获取单据类型,判断单据类型是否与当前类型相同: 如果相同则不用交换单据VO,否则交换单据VO
				 **************************************************************/
				if (tmpbillType.equals("00")) {
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000059")/*参数注册无标准VO*/);
				} else if (tmpbillType.equals("01")) {
					// 表示 通用的VO类型
					if (voClassName.endsWith("[]")) {
						paraObjects[i] = paraVo.m_preValueVos; // 参数值
						paraClasses[i] = AggregatedValueObject[].class; // 参数类
					} else {
						paraObjects[i] = paraVo.m_preValueVo; // 参数值
						paraClasses[i] = AggregatedValueObject.class;
					}
				} else {
					// 处理单据类型相同的或同类的单据的不需要进行转换
					if (isSimilarBilltype(paraVo.m_billType, tmpbillType,paraVo.m_pkGroup)) {
						if (voClassName.endsWith("[]")) {
							paraObjects[i] = paraVo.m_preValueVos; // 参数值
							paraClasses[i] = PfUtilTools.getClassNameClass(
									voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
						} else {
							paraObjects[i] = paraVo.m_preValueVo; // 参数值
							paraClasses[i] = getClassByName(voClassName);
						}
					} else {
						// 异种单据类型之间需要进行转换
						if (voClassName.endsWith("[]")) {
							paraObjects[i] = getExchangeService().runChangeDataAry(paraVo.m_billType, tmpbillType,
									paraVo.m_preValueVos, paraVo); // 参数值
							paraClasses[i] = PfUtilTools.getClassNameClass(
									voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
						} else {
							paraObjects[i] = runChangeData(paraVo.m_billType, tmpbillType, paraVo.m_preValueVo,
									paraVo); // 参数值
							paraClasses[i] = getClassByName(voClassName);
						}
					}
				}
			} else {
				// 3.参数中不包含VO类名
				parseParmeter(paraStrs[i], paraObjects, paraClasses, i, paraVo);
			}
		} // /{end for}

		// /2.实例化类，产生对象
		Object tmpObj = null;
		try {
			tmpObj = instantizeObject(paraVo.m_billType, className);
		} catch (Exception e) {
			Logger.warn("在模块中找不到类，则假设为PUBLIC类：" + className, e);
			try {
				// WARN::如果在模块中找不到类，则假设为PUBLIC类
				Class cls = Class.forName(className);
				tmpObj = cls.newInstance();
			} catch (ClassNotFoundException ex) {
				Logger.error(ex.getMessage(), ex);
			} catch (InstantiationException ex) {
				Logger.error(ex.getMessage(), ex);
			} catch (IllegalAccessException ex) {
				Logger.error(ex.getMessage(), ex);
			}
		}

		if (tmpObj == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000060", null, new String[]{className})/*无法实例化类：{0}*/);

		// /3.执行方法
		Object retObj = null;
		try {
			Class c = tmpObj.getClass();
			// 获取方法
			Method cm = c.getMethod(method, paraClasses);

			// fangj 2001-11-08返回为VOID的判断
			// 进行线程同步，保证事务在同一线程内运行
			synchronized (Thread.currentThread()) {
				if (cm.getReturnType().toString().equals("void")) {
					cm.invoke(tmpObj, paraObjects);
				} else {
					retObj = cm.invoke(tmpObj, paraObjects);
				}
			}
		} catch (SecurityException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			// XXX::需要把反射调用中的异常抛出来!
			Throwable expt = e.getTargetException();
			Logger.error(e.getMessage(), e);
			if (expt instanceof BusinessException)
				// 业务异常直接抛出！
				throw (BusinessException) expt;
			else if (expt instanceof RemoteException && expt.getCause() instanceof BusinessException) {
				throw (BusinessException) expt.getCause();
			} else
				throw new PFBusinessException(expt.getMessage(), expt);
		}

		long end = System.currentTimeMillis();
		Logger.debug("执行类:" + className + ";方法:" + method + ";参数:" + parameter);
		Logger.debug("************执行类PfUtilTools.runClass()结束,耗时=" + (end - begin) + "ms************");
		return retObj;
	}

	/**
	 * 判定两个单据类型（或交易类型）是否为同源关系
	 * 
	 * @param aTypeCode
	 * @param bTypeCode
	 * @return
	 * @since 5.5
	 */
	private static boolean isSimilarBilltype(String aTypeCode, String bTypeCode, String pk_group) {
		boolean isSimilar = false;
		BilltypeVO aTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(aTypeCode).buildPkGroup(pk_group));
		BilltypeVO bTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(bTypeCode).buildPkGroup(pk_group));
		if (aTypeVO.getIstransaction() != null && aTypeVO.getIstransaction().booleanValue()) {
			// a为交易类型
			BilltypeVO aParentTypeVO = PfDataCache.getBillTypeInfo( new BillTypeCacheKey().buildBilltype(aTypeVO.getParentbilltype())   );
			Integer aParentStyle = aParentTypeVO.getBillstyle();
			if (bTypeVO.getIstransaction() != null && bTypeVO.getIstransaction().booleanValue()) {
				// b为交易类型
				BilltypeVO bParentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(bTypeVO.getParentbilltype()));
				Integer bParentStyle = bParentTypeVO.getBillstyle();
				isSimilar = aTypeCode.equals(bTypeCode)
						|| aParentTypeVO.getPk_billtypecode().equals(bParentTypeVO.getPk_billtypecode())
						|| (aParentStyle != null && bParentStyle != null && aParentStyle.equals(bParentStyle));
			} else {
				// b为单据类型
				Integer bStyle = bTypeVO.getBillstyle();
				isSimilar = aParentTypeVO.getPk_billtypecode().equals(bTypeCode)
						|| (aParentStyle != null && bStyle != null && aParentStyle.equals(bStyle));
			}
		} else {
			// a为单据类型
			Integer aStyle = aTypeVO.getBillstyle();
			if (bTypeVO.getIstransaction() != null && bTypeVO.getIstransaction().booleanValue()) {
				// b为交易类型
				BilltypeVO bParentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(bTypeVO.getParentbilltype()));
				Integer bParentStyle = bParentTypeVO.getBillstyle();
				isSimilar = bParentTypeVO.getPk_billtypecode().equals(aTypeCode)
						|| (bParentStyle != null && aStyle != null && bParentStyle.equals(aStyle));
			} else {
				// b为单据类型
				Integer bStyle = bTypeVO.getBillstyle();
				isSimilar = (aTypeCode.equals(bTypeCode) || (aStyle != null && bStyle != null && aStyle
						.equals(bStyle)));
			}
		}

		return isSimilar;
	}

	/**
	 * 宏替换，比如：“你好，审批%%e.getName%%通过了” 
	 * XXX::宏替换前最好先检查一下宏的起点和终点是否匹配，也就是说是否含有偶数个宏标记
	 */
	protected static String translateMacro(String content, String macro_tag, String type,
			Object leftValue, Object rightValue) {
		int pos_b = 0; // 起点
		int pos_e = 0; // 终点
		int offset = 0;
		boolean bFound = false;
		boolean isMatch = false;
		StringBuffer buffer = new StringBuffer();

		do {
			pos_e = content.indexOf(macro_tag, pos_b);
			bFound = pos_e == -1 ? false : true;
			offset = bFound ? macro_tag.length() : 0;
			if (bFound) {
				// 还没有匹配两个"%% %%"号
				if (isMatch) {
					// 替换宏"%%macro%%"中的内容
					String macro = content.substring(pos_b, pos_e);
					// Logger.debug("macro = " + express);
					// ExpressParser parser = new ExpressParser(context,
					// express);
					buffer.append(getExpressValue(macro, type, leftValue, rightValue));
				} else {
					// 加入在奇数位“%%"号之前的字符串
					buffer.append(content.substring(pos_b, pos_e));
				}
				isMatch = !isMatch;
			} else {
				buffer.append(content.substring(pos_b));
			}
			pos_b = pos_e + offset;
		} while (pos_b != -1);

		return buffer.toString();
	}

	private PfUtilTools() {
		super();
	}

	/**
	 * 判断是否增加VO。返回为true 表示该当前数据与该操作人或组相关，返回false，则不相关。 
	 * 算法： 
	 * 1.操作人或角色无关，则返回该记录。
	 * 2.操作人有关，则返回跟该操作人有关的记录。 
	 * 3.角色有关，则返回该角色相关的记录
	 * 
	 * @param pkCorp 公司PK
	 * @param currUserPK 当前操作的用户
	 * @param configflag 用户或角色相关
	 * @param configedOperator 配置的用户或角色PK
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isContinue(String pkCorp, String currUserPK, int configflag,
			String configedOperator) throws BusinessException {
		switch (configflag) {
			case IPFConfigInfo.UserRelation:
				// 判断当前事件是否与操作人有关
				if (configedOperator.equals(currUserPK))
					return true;
				return false;
			case IPFConfigInfo.RoleRelation: {
				// 查找当前用户所属的角色
				IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
						IRoleManageQuery.class.getName());

				RoleVO[] roles = roleBS.queryRoleByUserID(currUserPK, pkCorp);

				if (roles == null || roles.length == 0)
					return false;

				for (int i = 0; i < roles.length; i++) {
					if (configedOperator.equals(roles[i].getPrimaryKey()))
						return true;
				}
				return false;
			}
			default:
				return true;
		}

	} 

	/**
	 * 每个Servlet都需要注册数据源信息才可调用DMO
	 * 
	 * @param dsName
	 */
	public static void regDataSourceForServlet(String dsName) throws BusinessException {
		// 1.校验
		try {
			IConfigFileService iAccount = (IConfigFileService) NCLocator.getInstance().lookup(
					IConfigFileService.class.getName());

			String[] dataSources = iAccount.findDatasource();
			if (dataSources == null)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000061")/*系统异常，请找管理员*/);
			boolean bValid = Arrays.asList(dataSources).contains(dsName);
			if (!bValid)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000062")/*错误：无效数据源*/);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000061")/*系统异常，请找管理员*/);
		}

		// 2.注册
		InvocationInfoProxy.getInstance().setUserDataSource(dsName);
	}

	/**
	 * 后台审批某张单据
	 * 
	 * @param billType
	 * @param billId
	 * @param checkResult
	 * @param checkNote
	 * @param checkman
	 * @param dispatched_ids
	 * @return
	 * @throws Exception
	 */
	public static String approveSilently(String billType, String billId, String checkResult,
			String checkNote, String checkman, String[] dispatched_ids) throws Exception {
		Logger.debug("******进入PfUtilTools.approveSilently方法*************************");
		Logger.debug("* billType=" + billType);
		Logger.debug("* billId=" + billId);
		Logger.debug("* checkResult=" + checkResult);
		Logger.debug("* checkNote=" + checkNote);
		Logger.debug("* checkman=" + checkman);
		
		// 1.获得单据聚合VO
		IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
		AggregatedValueObject billVo = bsConfig.queryBillDataVO(billType, billId);
		if (billVo == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000063")/*错误：根据单据类型和单据ID获取不到单据聚合VO*/);

		// 2.获得工作项并设置审批意见
		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator.getInstance().lookup(
				IWorkflowMachine.class.getName());
		HashMap hmPfExParams = new HashMap();
		WorkflownoteVO worknoteVO = bsWorkflow.checkWorkFlow(IPFActionName.APPROVE + checkman,
				billType, billVo, hmPfExParams);
		if (worknoteVO != null) {
			worknoteVO.setChecknote(checkNote);
			// 获取审批结果-通过/不通过/驳回
			if ("Y".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("Y");
			} else if ("N".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("N");
			} else if ("R".equalsIgnoreCase(checkResult)) {
				worknoteVO.getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.Reject.getIntValue());
				//xry TODO:worknoteVO.getTaskInstanceVO().getTask().setBackToFirstActivity(true);
			} else
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000064")/*错误：消息格式不对*/;

			// 指派信息
			if (dispatched_ids != null && dispatched_ids.length > 0) {
				// 分离活动与其指派的参与者
				HashMap hm = new HashMap();
				for (int i = 0; i < dispatched_ids.length; i++) {
					int index = dispatched_ids[i].indexOf("#");
					String userid = dispatched_ids[i].substring(0, index);
					String actDefid = dispatched_ids[i].substring(index + 1);
					if (hm.get(actDefid) == null)
						hm.put(actDefid, new HashSet());
					((HashSet) hm.get(actDefid)).add(userid);
				}
				// 填写到活动的指派信息中
				Vector vecDispatch = worknoteVO.getAssignableInfos();
				for (int i = 0; i < vecDispatch.size(); i++) {
					AssignableInfo ai = (AssignableInfo) vecDispatch.get(i);
					HashSet hs = (HashSet) hm.get(ai.getActivityDefId());
					if (hs != null) {
						// XXX:要避免添加重复的指派用户PK
						for (Iterator iter = hs.iterator(); iter.hasNext();) {
							String userId = (String) iter.next();
							if (!ai.getAssignedOperatorPKs().contains(userId))
								ai.getAssignedOperatorPKs().add(userId);
						}
					}
				}
			}
		}else
			Logger.debug("checkWorkflow返回的结果为null");

		// 3.执行审批动作
		IplatFormEntry pff = (IplatFormEntry) NCLocator.getInstance().lookup(
				IplatFormEntry.class.getName());
		pff.processAction(IPFActionName.APPROVE + checkman, billType, worknoteVO, billVo, null, hmPfExParams);

		return null;
	}

	public static String joinHtml(String billHtml, String approveHtml) {
		billHtml = "<head><body>" + billHtml + "<hr>" + approveHtml + "</body></html>";
		return billHtml;
	}
	
	/**
	 * 查询某VO的TS
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public static String queryTSByVO(SuperVO vo) throws BusinessException {
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select ts from " + vo.getTableName() + " where " + vo.getPKFieldName() + " = ?";
		SQLParameter param = new SQLParameter(); //构造参数对象
		param.addParam(vo.getPrimaryKey()); //添加参数
		Object objTs = baseDAO.executeQuery(sql, param, new ColumnProcessor());
		return objTs == null ? null : objTs.toString();
	}

	/**
	 * 获得指定类明的数组类定义
	 * 
	 * @param className
	 * @param intLen
	 * @return
	 * @throws BusinessException
	 */
	public static Class getClassNameClass(String className, int intLen) throws BusinessException {
		AggregatedValueObject[] retVos = PfUtilBaseTools.pfInitVos(className, intLen);
		return retVos.getClass();
	}

	public static String[] getOrgInfoByPK(String pkOrg) {
		if(StringUtil.isEmptyWithTrim(pkOrg))
			return null;
		String sql = "select code, name from org_orgs where pk_org=?";
		SQLParameter param = new SQLParameter();
		param.addParam(pkOrg);
		BaseDAO baseDAO = new BaseDAO();
		try {
			List res = (List) baseDAO.executeQuery(sql, param, new ArrayListProcessor());
			if (res != null && res.size() != 0) {
				Object[] arrs = (Object[]) res.get(0);
				return new String[] { (String) arrs[0], (String) arrs[1] };
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
}