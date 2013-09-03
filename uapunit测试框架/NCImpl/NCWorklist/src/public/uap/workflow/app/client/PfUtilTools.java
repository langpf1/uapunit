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
 * ����ƽ̨��̨������
 * <li>ע�⣺ֻ�ɺ�̨����
 * 
 * @author ���ھ� 2002-4-12 12:29:25
 * @modifier �׾� 2004-03-09 ���ӽ������ݶ���Լ�����������Ϣ��ʾ
 * @modifier leijun 2006-4-29 BOOLEAN����ҲҪ֧����ֵ�ж����������ֵ����ֱ�Ӽ�����ֵ��
 * @modifier leijun 2008-12 ����String���͵ĵ��ݺ���֧�ָ���Ƚ��������not like,in,not in
 * 
 * @modifier leijun 2009-3 �޸�VO�������߼�����Ϊ�����ݿ��ȡ��������
 */
public class PfUtilTools  {

	private static IPfExchangeService exchangeService;

	/**
	 * ʵ������ĳ�����������������
	 * <li>ǰ�᣺�ӵ������Ϳɻ�ȡ�����ڵ�ģ����
	 * 
	 * @param pkBilltype ��������PK
	 * @param checkClsName ����
	 * @return ��ʵ��
	 * @throws BusinessException
	 */
	public static Object instantizeObject(String pkBilltype, String checkClsName)
			throws BusinessException {
		//XXX:�ýӿڵ�ʵ��ʹ���˺�̨����
		IPFMetaModel pfmeta = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());
		String strModule = pfmeta.queryModuleOfBilltype(pkBilltype);
		if (StringUtil.isEmptyWithTrim(strModule))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000050", null, new String[]{pkBilltype})/*�õ�������{0}û�ж���������ģ��*/);
		return NewObjectService.newInstance(strModule, checkClsName);
	}

	/**
	 * ��ȡע����bd_billtype.checkclassname�����ʵ��
	 * <li>ֻ�ɺ�̨����
	 * 
	 * @param billType �������ͣ��������ͣ�PK
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
	 * ��ȡע����bd_billtype.emendEnumClass�����ʵ��
	 * <li>ֻ�ɺ�̨����
	 * 
	 * @param billType �������ͣ��������ͣ�PK
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
	 * ��õ���������ע���ҵ��ʵ�����ʵ��
	 * <li>ֻ�ɺ�̨����
	 * 
	 * @param billType ��������PK
	 * @param clzName ҵ��������
	 * @return
	 * @throws BusinessException
	 */
	public static Object findBizImplOfBilltype(String billType, String clzName)
			throws BusinessException {
		if (StringUtil.isEmptyWithTrim(clzName))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000051", null, new String[]{billType})/*����ƽ̨����������{0}ע���ҵ��������Ϊ��*/);
		return instantizeObject(billType, clzName.trim());
	}

	/**
	 * ���ָ���������ඨ��
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
	 * ���ص�ǰʹ������Դ����
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
	 * �������ʽ��ֵ <BR>
	 * ע�⣺Ŀǰ ��֧�����ֱ��ʽ��
	 * 
	 * @param macro ����ʽ�����к���
	 * @param type ���ʽ����
	 * @param leftValue ��ֵ
	 * @param rightValue ��ֵ
	 * @return String
	 * @author �׾�
	 */
	private static String getExpressValue(String macro, String type, Object leftValue,
			Object rightValue) {
		// Ŀǰ����֧�ֵĺ���ʽ
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
	 * ���ܣ��жϹ�ϵʽ�Ƿ�ɹ��� ����Type;�ַ���"STRING"�����͡�INTEGER����������"DOUBLE"��������"BOOLEAN"
	 * �ַ��������Ϊ:����"=",������"like" ���������Ϊ: ����"=",���ڵ���">=",С�ڵ���" <=",������"!="
	 * ����">",С��" <" �����������Ϊ:����"=",���ڵ���">=",С�ڵ���" <=",������"!=" ����">",С��" <"
	 * ������BOOLEAN�����Ϊ:����"=".
	 * 
	 * @param InObject ��ֵ
	 * @param objType ��ֵ���ͣ���֧��"BOOLEAN","STRING","INTEGER","DOUBLE"
	 * @param opType �ȽϷ�
	 * @param value ��ֵ
	 * @return
	 * @throws BusinessException
	 * @modifier leijun 2007-9-1 ��ֵ����֧��"VOID"
	 */
	public static boolean isTrueOrNot(Object InObject, String objType, String opType, String value)
			throws BusinessException {
		Logger.debug("****�Ƚ�����:" + objType + "�Ƚ����������:" + opType + "****");

		if (PfDataTypes.VOID.getTag().equals(objType))
			// XXX:�����ֵ��������ֵΪ"VOID"��������
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
	 * �ж�Double�͵����
	 * 
	 * @param InObject ��ֵ
	 * @param opType �Ƚ������
	 * @param value ��ֵ
	 * @return
	 */
	private static boolean compareDouble(UFDouble InObject, String opType, String value) {
		// �ж�Double�͵����
		if (InObject == null) {
			InObject = new UFDouble(0);
		}
		if (value == null) {
			value = "0";
		}
		// XXX::V5ǰ�İ汾�к������ʽ�����Ϊ"="��V5�޸�Ϊ"=="
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
	 * �ж�Integer�͵����
	 * 
	 * @param InObject ��ֵ
	 * @param opType �Ƚ������
	 * @param value ��ֵ
	 * @return
	 */
	private static boolean compareInteger(Integer InObject, String opType, String value) {
		Integer typeInteger;
		// �ж�Integer�͵����
		if (InObject == null) {
			InObject = Integer.valueOf(0);
		}
		if (value == null) {
			value = "0";
		}
		typeInteger = (Integer) InObject;
		// XXX::V5ǰ�İ汾�к������ʽ�����Ϊ"="��V5�޸�Ϊ"=="
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
	 * �ж�String�͵����
	 * 
	 * @param strLeftValue ��ֵ
	 * @param opType �Ƚ������
	 * @param strRightValue ��ֵ
	 * @return
	 */
	private static boolean compareString(String strLeftValue, String opType, String strRightValue) {
		if (strLeftValue == null)
			return false;
		// XXX::V5ǰ�İ汾�к������ʽ�����Ϊ"="��V5�޸�Ϊ"=="
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
	 * �ж�boolean�͵����
	 * 
	 * @param InObject ��ֵ
	 * @param opType �Ƚ������
	 * @param value ��ֵ
	 * @return
	 */
	private static boolean compareBoolean(UFBoolean InObject, String opType, String value) {
		// //lj@2006-4-29 BOOLEAN����ҲҪ֧����ֵ�ж�

		// XXX::V5ǰ�İ汾�к������ʽ�����Ϊ"="��V5�޸�Ϊ"=="
		// if (PfOperatorTypes.EQ.toString().equals(opType)) {
		if (PfOperatorTypes.EQ.toString().equals(opType) || "=".equals(opType)) {
			if (InObject.equals(UFBoolean.valueOf(value)))
				return true;
			else
				return false;
		} else
			// ���û�бȽϷ�����ֱ�Ӽ�����ֵ
			return InObject.booleanValue();
	}

	/**
	 * ִ�к��������ؽ��
	 * 
	 * @param functionNote ��������˵��
	 * @param className ����
	 * @param method ������
	 * @param parameter ����
	 * @param paraVo �����������Ĳ���
	 * @param methodReturnHas
	 * @return
	 * @throws BusinessException
	 */
	public static Object parseFunction(String functionNote, String className, String method,
			String parameter, PfParameterVO paraVo)
			throws BusinessException {

		String errString;
		Object checkObject = null;

		Logger.debug("parseFunction������������:" + className + "������:" + method + "����:" + parameter + "��ʼ");
		if (className == null) {
			errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000052")/*parseFunction���������޷����к���*/;
			throw new PFBusinessException(errString);
		} else if (method == null) {
			errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000053")/*parseFunction�޷��������޷����к���*/;
			throw new PFBusinessException(errString);
		}
		// ִ����ķ���
		checkObject = runClass(className, method, parameter, paraVo, null);
		if (functionNote != null) {
			Logger.debug("����#" + functionNote + "#���з���ֵΪ��" + String.valueOf(checkObject));
		} else {
			Logger.debug("�������з���ֵΪ��" + String.valueOf(checkObject));
		}
		Logger.debug("parseFunction������������:" + className + "������:" + method + "����:" + parameter
				+ "****����");
		return checkObject;
	}

	/**
	 * �������Բ���
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
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000054")/*ע���������ȷ*/);
		} else {
			try {
				// ͨ����������ǰ�ĵ�һ�ַ������������������Է���COM..�������
				fieldName = dealStr.substring(0, retIndex);
				if (fieldName.startsWith("OBJ")) {
					// ��ʾΪ �û��Զ������
					if (fieldName.endsWith("ARY")) {
						paraObjects[arrIndex] = paraVo.m_userObjs;
					} else {
						paraObjects[arrIndex] = paraVo.m_userObj;
					}
				} else {
					// ��ʾΪ ���Բ�������Ҫ�������һЩ���ݣ�
					paraObjects[arrIndex] = paraVo.m_standHeadVo.getAttributeValue(fieldName);
				}
				datatype = dealStr.substring(retIndex + 1);
				paraClasses[arrIndex] = PfUtilBaseTools.parseTypeClass(datatype);
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000055")/*δ�ҵ�ע������ļ�*/, ex);
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
	 * ���ܣ����ڽ����﷨�ж�����
	 * 
	 * @author �׾� 2004-03-09 ���ݶ���Լ����������ʾ��Ϣͨ������errorMessageBuffer����
	 * 
	 * @param className ��������
	 * @param method ����������
	 * @param parameter ����
	 * @param funNote ����˵��
	 * @param returnType ��������ֵ����
	 * @param ysf �����
	 * @param value ֵ
	 * @param className2 �������ң�
	 * @param method2 ���������ң�
	 * @param parameter2 ����
	 * @param funNote2 ����˵��
	 * @param paraVo �����������Ĳ���
	 * @param methodReturnHas
	 * @param errorMessageBuffer ���ص���Ϣ��
	 * @param originalHintBuffer ����Ϣ��
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

		strTmp = "parseSyntax�����﷨��ʼ";
		Logger.debug(strTmp);
		if (className == null) {
			// ������Ϣ
			Logger.debug("parseSyntax�����﷨����");
			return false;
		}

		// ���ر���
		boolean retBool = true;
		// ������ֵ������
		leftObject = parseFunction(funNote, className, method, parameter, paraVo);
		if (className2 == null) {
			// ���ص�ֱֵ�����û�����Ƚ�
			Logger.debug("�û��������ֵΪ��" + value);
		} else {
			// ������ֵ������
			rightObject = parseFunction(funNote2, className2, method2, parameter2, paraVo);
			value = String.valueOf(rightObject);
			Logger.debug("�Һ���������ֵΪ:" + value);
		}

		// �жϱ��ʽ
		retBool = isTrueOrNot(leftObject, returnType.toUpperCase(), ysf, value);
		if (retBool) {
			Logger.debug("�������н���жϳɹ�!");
		} else {
			Logger.debug("�������н���жϲ��ɹ�!");
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

			// added by �׾� 2004-03-09 �������ݶ���Լ��������ʱ����Ϣ��ʾ����ʽ
			String MACRO_TAG = "%%";
			String originalHint = "";
			if (originalHintBuffer != null) {
				originalHint = originalHintBuffer.toString();
				String hintAfterParse = translateMacro(originalHint, MACRO_TAG, returnType, leftObject,
						value);
				originalHintBuffer.append(hintAfterParse);
			}
		}

		strTmp = "parseSyntax�����﷨����";
		Logger.debug(strTmp);
		return retBool;
	}

	/**
	 * ����VO���� <li>��֧�ֵַ�
	 * 
	 * @param srcTranstype Դ��������PK
	 * @param destTranstype Ŀ�Ľ�������PK
	 * @param sourceBillVO Դ���ݾۺ�VO
	 * @return Ŀ�ĵ��ݾۺ�VO
	 * @throws BusinessException
	 */
	public static AggregatedValueObject runChangeData(String srcTranstype, String destTranstype,
			AggregatedValueObject sourceBillVO) throws BusinessException {
		return getExchangeService().runChangeData(srcTranstype, destTranstype, sourceBillVO, null);
	}

	/**
	 * ����VO���� <li>��֧�ֵַ�
	 * 
	 * @param srcBillOrTranstype Դ��������PK
	 * @param destBillOrTranstype Ŀ�Ľ�������PK
	 * @param sourceBillVO Դ���ݾۺ�VO
	 * @param paraVo ����������VO
	 * @return Ŀ�ĵ��ݾۺ�VO
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
	 * �������ݽ�����(VO����) <li>֧�ֵַ�
	 * 
	 * @param sourceBillType Դ��������PK
	 * @param destBillType Ŀ�ĵ�������PK
	 * @param sourceBillVOs Դ���ݾۺ�VO����
	 * @return Ŀ�ĵ��ݾۺ�VO����
	 * @throws BusinessException
	 */
	public static AggregatedValueObject[] runChangeDataAry(String sourceBillType,
			String destBillType, AggregatedValueObject[] sourceBillVOs) throws BusinessException {

		return getExchangeService().runChangeDataAry(sourceBillType, destBillType, sourceBillVOs, null);
	}

	/**
	 * �������ݽ�����(VO����) <li>֧�ֵַ�
	 * 
	 * @param srcBillOrTranstype Դ��������PK
	 * @param destBillOrTranstype Ŀ�ĵ�������PK
	 * @param sourceBillVOs  Դ���ݾۺ�VO����
	 * @param paraVo ����������VO�������ڻ�ȡһЩϵͳ����
	 * @return Ŀ�ĵ��ݾۺ�VO����
	 * @throws BusinessException
	 */
	public static AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo)
			throws BusinessException {

		return getExchangeService().runChangeDataAry(srcBillOrTranstype, destBillOrTranstype, sourceBillVOs, paraVo);
	}

	

	/**
	 * ������Դ��Ŀ�ĵ������ͣ�ʵ����ĳ����
	 * <li>������Ŀ�ĵ�����������ģ�� ������
	 * 
	 * @param sourceBillType Դ��������PK
	 * @param destBillType Ŀ�ĵ�������PK
	 * @param fullyQualifiedClassName ����
	 * @return ��ʵ��
	 * @throws BusinessException
	 */
	public static Object newImplOfClz(String sourceBillType, String destBillType,
			String fullyQualifiedClassName) throws BusinessException {
		Object changeObj = null;
		IPFMetaModel pfmeta = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());

		// Ŀ�ĵ������� ���丸 ����ģ��
		String moduleOfDest = pfmeta.queryModuleOfBilltype(destBillType);
		// Դ������������ģ��
		String moduleOfSrc = pfmeta.queryModuleOfBilltype(sourceBillType);

		if (moduleOfDest == null || moduleOfDest.trim().length() == 0) {
			if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000056")/*����Դ��Ŀ�ĵ������Ͷ�û��ע��ģ����*/);
			else {
				changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
				Logger.debug("OK-Ŀ�ĵ���û������ģ�飬������Դ��������ģ��" + moduleOfSrc + "���ҵ�������=" + changeObj);
			}
		} else {
			// ������Ŀ�ĵ�����������ģ�� ������
			try {
				changeObj = NewObjectService.newInstance(moduleOfDest, fullyQualifiedClassName);
				Logger.debug("OK-��Ŀ�ĵ�������ģ��" + moduleOfDest + "���ҵ�������=" + changeObj);
			} catch (Exception e) {
				if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000057")/*����VO�����಻��Ŀ�ĵ�����������ģ���У�����Դ����û��ע������ģ��*/);
				else {
					changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
					Logger.debug("OK-��Ŀ�ĵ�������ģ�����Ҳ��������࣬������Դ��������ģ��" + moduleOfSrc + "���ҵ�������=" + changeObj);
				}
			}
		}

		Class c = changeObj.getClass();
		Logger.debug(">>>OBJ=" + changeObj + ";CL=" + c.getProtectionDomain().getClassLoader());
		Logger.debug(">>>LOC=" + c.getProtectionDomain().getCodeSource().getLocation());
		return changeObj;
	}

	/**
	 * ������
	 * 
	 * @param className ���е�������
	 * @param method ��������
	 * @param parameter ����
	 * @param paraVo ����������VO
	 * @param keyHas ����ֵHash
	 * @param methodReturnHas ����ֵHash
	 * 
	 * @modifier leijun 2005-6-20 ��������PK�������Ϊ4�ַ�,�ǹ̶���2���ַ�
	 */
	public static Object runClass(String className, String method, String parameter,
			PfParameterVO paraVo, Hashtable keyHas) throws BusinessException {
		Logger.debug("**********ִ����PfUtilTools.runClass()��ʼ************");

		Logger.debug("ִ����:" + className + ";����:" + method + ";����:" + parameter);

		long begin = System.currentTimeMillis();

		// /1.������������Ϊ��������","�ָ��
		// String[] paraStrs = nc.vo.pf.pub.PfComm.dealString(parameter, ",");
		StringTokenizer tmpStrToken = new StringTokenizer(parameter, ",");
		String[] paraStrs = new String[tmpStrToken.countTokens()];
		int index = 0;
		while (tmpStrToken.hasMoreTokens())
			paraStrs[index++] = tmpStrToken.nextElement().toString();

		Object[] paraObjects = new Object[paraStrs.length];
		Class[] paraClasses = new Class[paraStrs.length];

		for (int i = 0; i < paraStrs.length; i++) {
			// ����������
			// �������"."�����ʾVO����
			boolean isVo = paraStrs[i].indexOf(".") > 0 ? true : false;
			int colonPos = paraStrs[i].indexOf(":");

			if (paraStrs[i].startsWith("&")) {
				// 1.�����"&"��ͷ�����ʾ���в���
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
				// XXX::ֱ���ٴα���
				continue;
			}

			if (isVo) {
				// 2.�����а���VO����
				if (colonPos < 0)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000058")/*����ע�ᴦVO��������ע��*/);
				// ��ȡ�������ͺ�VO���� lj@2005-6-17
				String tmpbillType = paraStrs[i].substring(colonPos + 1);
				String voClassName = paraStrs[i].substring(0, colonPos);
				/***************************************************************
				 * �ӵ���Vo�뵥�����Ͷ��ձ��л�ȡ��������,�жϵ��������Ƿ��뵱ǰ������ͬ: �����ͬ���ý�������VO,���򽻻�����VO
				 **************************************************************/
				if (tmpbillType.equals("00")) {
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000059")/*����ע���ޱ�׼VO*/);
				} else if (tmpbillType.equals("01")) {
					// ��ʾ ͨ�õ�VO����
					if (voClassName.endsWith("[]")) {
						paraObjects[i] = paraVo.m_preValueVos; // ����ֵ
						paraClasses[i] = AggregatedValueObject[].class; // ������
					} else {
						paraObjects[i] = paraVo.m_preValueVo; // ����ֵ
						paraClasses[i] = AggregatedValueObject.class;
					}
				} else {
					// ������������ͬ�Ļ�ͬ��ĵ��ݵĲ���Ҫ����ת��
					if (isSimilarBilltype(paraVo.m_billType, tmpbillType,paraVo.m_pkGroup)) {
						if (voClassName.endsWith("[]")) {
							paraObjects[i] = paraVo.m_preValueVos; // ����ֵ
							paraClasses[i] = PfUtilTools.getClassNameClass(
									voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
						} else {
							paraObjects[i] = paraVo.m_preValueVo; // ����ֵ
							paraClasses[i] = getClassByName(voClassName);
						}
					} else {
						// ���ֵ�������֮����Ҫ����ת��
						if (voClassName.endsWith("[]")) {
							paraObjects[i] = getExchangeService().runChangeDataAry(paraVo.m_billType, tmpbillType,
									paraVo.m_preValueVos, paraVo); // ����ֵ
							paraClasses[i] = PfUtilTools.getClassNameClass(
									voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
						} else {
							paraObjects[i] = runChangeData(paraVo.m_billType, tmpbillType, paraVo.m_preValueVo,
									paraVo); // ����ֵ
							paraClasses[i] = getClassByName(voClassName);
						}
					}
				}
			} else {
				// 3.�����в�����VO����
				parseParmeter(paraStrs[i], paraObjects, paraClasses, i, paraVo);
			}
		} // /{end for}

		// /2.ʵ�����࣬��������
		Object tmpObj = null;
		try {
			tmpObj = instantizeObject(paraVo.m_billType, className);
		} catch (Exception e) {
			Logger.warn("��ģ�����Ҳ����࣬�����ΪPUBLIC�ࣺ" + className, e);
			try {
				// WARN::�����ģ�����Ҳ����࣬�����ΪPUBLIC��
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
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000060", null, new String[]{className})/*�޷�ʵ�����ࣺ{0}*/);

		// /3.ִ�з���
		Object retObj = null;
		try {
			Class c = tmpObj.getClass();
			// ��ȡ����
			Method cm = c.getMethod(method, paraClasses);

			// fangj 2001-11-08����ΪVOID���ж�
			// �����߳�ͬ������֤������ͬһ�߳�������
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
			// XXX::��Ҫ�ѷ�������е��쳣�׳���!
			Throwable expt = e.getTargetException();
			Logger.error(e.getMessage(), e);
			if (expt instanceof BusinessException)
				// ҵ���쳣ֱ���׳���
				throw (BusinessException) expt;
			else if (expt instanceof RemoteException && expt.getCause() instanceof BusinessException) {
				throw (BusinessException) expt.getCause();
			} else
				throw new PFBusinessException(expt.getMessage(), expt);
		}

		long end = System.currentTimeMillis();
		Logger.debug("ִ����:" + className + ";����:" + method + ";����:" + parameter);
		Logger.debug("************ִ����PfUtilTools.runClass()����,��ʱ=" + (end - begin) + "ms************");
		return retObj;
	}

	/**
	 * �ж������������ͣ��������ͣ��Ƿ�ΪͬԴ��ϵ
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
			// aΪ��������
			BilltypeVO aParentTypeVO = PfDataCache.getBillTypeInfo( new BillTypeCacheKey().buildBilltype(aTypeVO.getParentbilltype())   );
			Integer aParentStyle = aParentTypeVO.getBillstyle();
			if (bTypeVO.getIstransaction() != null && bTypeVO.getIstransaction().booleanValue()) {
				// bΪ��������
				BilltypeVO bParentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(bTypeVO.getParentbilltype()));
				Integer bParentStyle = bParentTypeVO.getBillstyle();
				isSimilar = aTypeCode.equals(bTypeCode)
						|| aParentTypeVO.getPk_billtypecode().equals(bParentTypeVO.getPk_billtypecode())
						|| (aParentStyle != null && bParentStyle != null && aParentStyle.equals(bParentStyle));
			} else {
				// bΪ��������
				Integer bStyle = bTypeVO.getBillstyle();
				isSimilar = aParentTypeVO.getPk_billtypecode().equals(bTypeCode)
						|| (aParentStyle != null && bStyle != null && aParentStyle.equals(bStyle));
			}
		} else {
			// aΪ��������
			Integer aStyle = aTypeVO.getBillstyle();
			if (bTypeVO.getIstransaction() != null && bTypeVO.getIstransaction().booleanValue()) {
				// bΪ��������
				BilltypeVO bParentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(bTypeVO.getParentbilltype()));
				Integer bParentStyle = bParentTypeVO.getBillstyle();
				isSimilar = bParentTypeVO.getPk_billtypecode().equals(aTypeCode)
						|| (bParentStyle != null && aStyle != null && bParentStyle.equals(aStyle));
			} else {
				// bΪ��������
				Integer bStyle = bTypeVO.getBillstyle();
				isSimilar = (aTypeCode.equals(bTypeCode) || (aStyle != null && bStyle != null && aStyle
						.equals(bStyle)));
			}
		}

		return isSimilar;
	}

	/**
	 * ���滻�����磺����ã�����%%e.getName%%ͨ���ˡ� 
	 * XXX::���滻ǰ����ȼ��һ�º�������յ��Ƿ�ƥ�䣬Ҳ����˵�Ƿ���ż��������
	 */
	protected static String translateMacro(String content, String macro_tag, String type,
			Object leftValue, Object rightValue) {
		int pos_b = 0; // ���
		int pos_e = 0; // �յ�
		int offset = 0;
		boolean bFound = false;
		boolean isMatch = false;
		StringBuffer buffer = new StringBuffer();

		do {
			pos_e = content.indexOf(macro_tag, pos_b);
			bFound = pos_e == -1 ? false : true;
			offset = bFound ? macro_tag.length() : 0;
			if (bFound) {
				// ��û��ƥ������"%% %%"��
				if (isMatch) {
					// �滻��"%%macro%%"�е�����
					String macro = content.substring(pos_b, pos_e);
					// Logger.debug("macro = " + express);
					// ExpressParser parser = new ExpressParser(context,
					// express);
					buffer.append(getExpressValue(macro, type, leftValue, rightValue));
				} else {
					// ����������λ��%%"��֮ǰ���ַ���
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
	 * �ж��Ƿ�����VO������Ϊtrue ��ʾ�õ�ǰ������ò����˻�����أ�����false������ء� 
	 * �㷨�� 
	 * 1.�����˻��ɫ�޹أ��򷵻ظü�¼��
	 * 2.�������йأ��򷵻ظ��ò������йصļ�¼�� 
	 * 3.��ɫ�йأ��򷵻ظý�ɫ��صļ�¼
	 * 
	 * @param pkCorp ��˾PK
	 * @param currUserPK ��ǰ�������û�
	 * @param configflag �û����ɫ���
	 * @param configedOperator ���õ��û����ɫPK
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isContinue(String pkCorp, String currUserPK, int configflag,
			String configedOperator) throws BusinessException {
		switch (configflag) {
			case IPFConfigInfo.UserRelation:
				// �жϵ�ǰ�¼��Ƿ���������й�
				if (configedOperator.equals(currUserPK))
					return true;
				return false;
			case IPFConfigInfo.RoleRelation: {
				// ���ҵ�ǰ�û������Ľ�ɫ
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
	 * ÿ��Servlet����Ҫע������Դ��Ϣ�ſɵ���DMO
	 * 
	 * @param dsName
	 */
	public static void regDataSourceForServlet(String dsName) throws BusinessException {
		// 1.У��
		try {
			IConfigFileService iAccount = (IConfigFileService) NCLocator.getInstance().lookup(
					IConfigFileService.class.getName());

			String[] dataSources = iAccount.findDatasource();
			if (dataSources == null)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000061")/*ϵͳ�쳣�����ҹ���Ա*/);
			boolean bValid = Arrays.asList(dataSources).contains(dsName);
			if (!bValid)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000062")/*������Ч����Դ*/);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000061")/*ϵͳ�쳣�����ҹ���Ա*/);
		}

		// 2.ע��
		InvocationInfoProxy.getInstance().setUserDataSource(dsName);
	}

	/**
	 * ��̨����ĳ�ŵ���
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
		Logger.debug("******����PfUtilTools.approveSilently����*************************");
		Logger.debug("* billType=" + billType);
		Logger.debug("* billId=" + billId);
		Logger.debug("* checkResult=" + checkResult);
		Logger.debug("* checkNote=" + checkNote);
		Logger.debug("* checkman=" + checkman);
		
		// 1.��õ��ݾۺ�VO
		IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
		AggregatedValueObject billVo = bsConfig.queryBillDataVO(billType, billId);
		if (billVo == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000063")/*���󣺸��ݵ������ͺ͵���ID��ȡ�������ݾۺ�VO*/);

		// 2.��ù���������������
		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator.getInstance().lookup(
				IWorkflowMachine.class.getName());
		HashMap hmPfExParams = new HashMap();
		WorkflownoteVO worknoteVO = bsWorkflow.checkWorkFlow(IPFActionName.APPROVE + checkman,
				billType, billVo, hmPfExParams);
		if (worknoteVO != null) {
			worknoteVO.setChecknote(checkNote);
			// ��ȡ�������-ͨ��/��ͨ��/����
			if ("Y".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("Y");
			} else if ("N".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("N");
			} else if ("R".equalsIgnoreCase(checkResult)) {
				worknoteVO.getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.Reject.getIntValue());
				//xry TODO:worknoteVO.getTaskInstanceVO().getTask().setBackToFirstActivity(true);
			} else
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000064")/*������Ϣ��ʽ����*/;

			// ָ����Ϣ
			if (dispatched_ids != null && dispatched_ids.length > 0) {
				// ��������ָ�ɵĲ�����
				HashMap hm = new HashMap();
				for (int i = 0; i < dispatched_ids.length; i++) {
					int index = dispatched_ids[i].indexOf("#");
					String userid = dispatched_ids[i].substring(0, index);
					String actDefid = dispatched_ids[i].substring(index + 1);
					if (hm.get(actDefid) == null)
						hm.put(actDefid, new HashSet());
					((HashSet) hm.get(actDefid)).add(userid);
				}
				// ��д�����ָ����Ϣ��
				Vector vecDispatch = worknoteVO.getAssignableInfos();
				for (int i = 0; i < vecDispatch.size(); i++) {
					AssignableInfo ai = (AssignableInfo) vecDispatch.get(i);
					HashSet hs = (HashSet) hm.get(ai.getActivityDefId());
					if (hs != null) {
						// XXX:Ҫ��������ظ���ָ���û�PK
						for (Iterator iter = hs.iterator(); iter.hasNext();) {
							String userId = (String) iter.next();
							if (!ai.getAssignedOperatorPKs().contains(userId))
								ai.getAssignedOperatorPKs().add(userId);
						}
					}
				}
			}
		}else
			Logger.debug("checkWorkflow���صĽ��Ϊnull");

		// 3.ִ����������
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
	 * ��ѯĳVO��TS
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public static String queryTSByVO(SuperVO vo) throws BusinessException {
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select ts from " + vo.getTableName() + " where " + vo.getPKFieldName() + " = ?";
		SQLParameter param = new SQLParameter(); //�����������
		param.addParam(vo.getPrimaryKey()); //��Ӳ���
		Object objTs = baseDAO.executeQuery(sql, param, new ColumnProcessor());
		return objTs == null ? null : objTs.toString();
	}

	/**
	 * ���ָ�������������ඨ��
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