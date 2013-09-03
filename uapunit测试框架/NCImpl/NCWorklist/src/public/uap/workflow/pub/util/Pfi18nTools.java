package uap.workflow.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.MultiLangUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.pfflow.BillactionVO;
import nc.vo.pub.workflowusergroup.WFUserGroupVO;

/**
 * 平台相关的多语化工具类 <li>包括单据类型名称、业务类型名称等
 * 
 * @author leijun 2005-10-27
 */
public class Pfi18nTools {
	// 系统类型i18n区域
	public final static String DAPSYSTEMP_PATH = "funcode";

	// 单据类型i18n区域
	public final static String BILLTYPEP_PATH = "billtype";

	// 单据动作i18n区域
	public final static String BILLACTION_PATH = "pub_billaction";

	// 单据动作组i18n区域
	public final static String BILLACTIONGROUP_PATH = "pub_billactiongroup";

	// 单据项目i18n区域
	public final static String BILLITEM_PATH = "fidap";

	// 工作流相关的i18n区域
	public final static String WORKFLOW_PATH = "pfworkflow";

	/**
	 * 多语流程用户组名称
	 * @param busiTypeVO
	 * @return
	 */
	public static String i18nWfUserGroupName(WFUserGroupVO wfUserGroupVO) {
		String name = (String) wfUserGroupVO.getAttributeValue("name" + MultiLangUtil.getCurrentLangSeqSuffix());
		if(StringUtil.isEmptyWithTrim(name)) {
			name = wfUserGroupVO.getName();
		}
		return name;
	}
	
	/**
	 * 多语业务流程名
	 * @param busiTypeVO
	 * @return
	 */
	public static String i18nBusiTypeName(BusitypeVO busiTypeVO) {
		String name = (String) busiTypeVO.getAttributeValue("businame" + MultiLangUtil.getCurrentLangSeqSuffix());
		if(StringUtil.isEmptyWithTrim(name)) {
			name = busiTypeVO.getBusiname();
		}
		return name;
	}	

	/**
	 * 对参与者限定过滤器的多语化
	 * 
	 * @param filterCode
	 *            过滤器标识
	 * @param billType
	 *            单据类型
	 * @return
	 */
	public static String i18nParticipantFilter(String filterCode, String billType) {
		String resId = "Dfilter." + billType + "." + filterCode;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(WORKFLOW_PATH, resId);
		return strName;
	}

	/**
	 * 返回系统类型i18n名称
	 * 
	 * @param systemCode
	 *            系统类型编码
	 * @param defaultName
	 *            默认名称
	 * @return 如果取不到多语资源串，则使用默认名称
	 */
	public static String i18nSystemtypeName(String resId, String defaultName) {

		if (StringUtil.isEmptyWithTrim(resId))
			return defaultName;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(DAPSYSTEMP_PATH, resId);
		if (resId.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}
	
	public static String i18nBilltypeName(String pkBilltype) {
		BilltypeVO billTypeVO = PfDataCache.getBillType(pkBilltype);
		return i18nBilltypeNameByVO(pkBilltype, billTypeVO);
	}

	/**
	 * 返回单据类型i18n名称
	 * 
	 * @param pkBilltype
	 *            单据类型编码，也是主键
	 * @param defaultName
	 *            默认名称
	 * @return 如果取不到多语资源串，则使用默认名称
	 */
	public static String i18nBilltypeName(String pkBilltype, String defaultName) {
		if (pkBilltype == null)
			return null;
		String strName = i18nBilltypeName(pkBilltype);
		if(StringUtil.isEmptyWithTrim(strName)) {
			strName = defaultName;
		}
		return strName;
	}

	/**
	 * 返回单据类型i18n名称
	 * 
	 * @param pkBilltype
	 *            单据类型编码，也是主键
	 * @param billTypeVO
	 *            单据VO
	 * @return 先取多语资源串，再取VO多语字段，没有则使用默认名称
	 */
	public static String i18nBilltypeNameByVO(String pkBilltype, BilltypeVO billTypeVO) {
		if (pkBilltype == null)
			return null;
		//不再使用资源多语，改为数据多语
		String strName = null;
		//通过VO的多语字段取
		String billTypeName = billTypeVO == null ? null : billTypeVO.getBilltypenameOfCurrLang();
		if(!StringUtil.isEmptyWithTrim(billTypeName)) {
			strName = billTypeName;
		} else {
			//还是取不到，取默认值
			strName = (billTypeVO == null || StringUtil.isEmptyWithTrim(billTypeVO.getBilltypename())) 
							? strName : billTypeVO.getBilltypename();
		}
		return strName;
	}

	/**
	 * 返回单据项目的i18n名称 <li>格式：itemname=XXX <li>
	 * 因为，单据项目dap_defitem.itemname字段就是多语资源ID
	 * 
	 * @param itemName
	 *            资源ID
	 * @param defaultName
	 *            默认名称
	 * @return 如果取不到多语资源串，则使用默认名称
	 */
	public static String i18nBillItemName(String itemName, String defaultName) {
		if (itemName == null)
			return defaultName;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLITEM_PATH, itemName);
		if (itemName.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * 多语化单据项目的描述信息
	 * 
	 * @param pk_billtype
	 * @param attrname
	 * @param defaultName
	 * @return
	 */
	public static String i18nBillItemDescription(String pk_billtype, String attrname, String defaultName) {
		if (attrname == null)
			return null;
		String resId = "D" + pk_billtype + "." + attrname + ".desc";
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLITEM_PATH, resId);
		if (resId.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * 返回单据项目的i18n名称 <li>格式：D88.billno=XXX
	 * 
	 * @param pk_billtype
	 *            单据类型PK
	 * @param attrname
	 *            属性
	 * @param defaultName
	 *            默认名称
	 * @return 如果取不到多语资源串，则使用默认名称
	 * @since NC50 还是采用wsw的方式吧？
	 */
	public static String i18nBillItemName2(String pk_billtype, String attrname, String defaultName) {
		if (attrname == null)
			return null;
		String resId = "D" + pk_billtype + "." + attrname;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLITEM_PATH, resId);
		if (resId.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * 返回动作i18n名称
	 * 
	 * @param pkBilltype
	 *            单据类型PK
	 * @param pk_actiontype
	 *            动作编码
	 * @param defaultName
	 *            默认名称
	 * @return
	 */
	public static String i18nActionName(String pkBilltype, String pk_actiontype, String defaultName) {
		String resId = "D" + pkBilltype + pk_actiontype;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLACTION_PATH, resId);
		if (resId.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * 返回动作组i18n名称
	 * 
	 * @param pkBilltype
	 *            单据类型PK
	 * @param actionstyle
	 *            动作组编码
	 * @param defaultName
	 *            默认名称
	 * @return
	 */
	public static String i18nActionGroupName(String pkBilltype, String actionstyle, String defaultName) {
		String resId = "D" + pkBilltype + actionstyle;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLACTIONGROUP_PATH, resId);
		if (resId.equals(strName)) {
			// 取不到多语资源串
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * 从VO数组中查询单据动作的名称属性
	 * 
	 * @param actionCode
	 *            动作编码
	 * @param alActions
	 *            动作VO数组
	 * @return
	 */
	public static String findActionName(String actionCode, ArrayList alActions) {
		String strActionDefaultName = null;
		for (Iterator iterator = alActions.iterator(); iterator.hasNext();) {
			BillactionVO baVO = (BillactionVO) iterator.next();
			if (actionCode.equals(baVO.getActiontype())) {
				strActionDefaultName = baVO.getActionnote();
				break;
			}
		}
		return strActionDefaultName;
	}

	/**
	 * 获得用户的语种，若数据库中未配置，则取默认语种
	 * @param cuserid
	 * @return
	 */
	public static String getLangCodeOfUser(String cuserid) {
		String sql = "select langcode from pub_multilang l join sm_user u"
				+ " on u.contentlang=l.pk_multilang where u.cuserid='" + cuserid + "'";
		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List list = null;
		try {
			list = (List) qry.executeQuery(sql, new ColumnListProcessor("langcode"));
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			return InvocationInfoProxy.getInstance().getLangCode();
		}

		if (list.size() > 0) {
			return String.valueOf(list.get(0));
		} else {
			return InvocationInfoProxy.getInstance().getLangCode();
		}
	}

	/**
	 * 批量查询用户的语种，并根据语种进行分类
	 * @param cuserids
	 * @return {@link Map} key = langcode, value = users list in that lang
	 */
	public static Map<String, List<String>> classifyUsersByLangcode(String[] cuserids) {

		Map<String, List<String>> langUserMap = new HashMap<String, List<String>>();
		String defaultLang = InvocationInfoProxy.getInstance().getLangCode();

		if (cuserids == null || cuserids.length == 0)
			return langUserMap;

		StringBuffer sb = new StringBuffer();

		for (String id : cuserids) {
			sb.append(",'");
			sb.append(id);
			sb.append("'");
		}

		String sql = "select l.langcode, u.cuserid from pub_multilang l join sm_user u"
				+ " on u.contentlang=l.pk_multilang where u.cuserid in (" + sb.substring(1) + ")";

		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Set<String> userWithLangConf = new HashSet<String>();
		try {
			List<Object[]> list = (List<Object[]>) qry.executeQuery(sql, new ArrayListProcessor());
			
			if (list != null && list.size() > 0) {
				for (Object[] row : list) {
					String langcode = (String) row[0];
					String cuserid = (String) row[1];
					
					if (!langUserMap.containsKey(langcode)) {
						List<String> userList = new ArrayList<String>();
						langUserMap.put(langcode, userList);
					}
					
					langUserMap.get(langcode).add(cuserid);
					userWithLangConf.add(cuserid);
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		
		for (String cuserid : cuserids) {
			if (userWithLangConf.contains(cuserid)) {
				continue;
			}
			
			if (!langUserMap.containsKey(defaultLang)) {
				List<String> userList = new ArrayList<String>();
				langUserMap.put(defaultLang, userList);
			}
			
			langUserMap.get(defaultLang).add(cuserid);
		}
		
		
		return langUserMap;
	}
}
