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
 * ƽ̨��صĶ��ﻯ������ <li>���������������ơ�ҵ���������Ƶ�
 * 
 * @author leijun 2005-10-27
 */
public class Pfi18nTools {
	// ϵͳ����i18n����
	public final static String DAPSYSTEMP_PATH = "funcode";

	// ��������i18n����
	public final static String BILLTYPEP_PATH = "billtype";

	// ���ݶ���i18n����
	public final static String BILLACTION_PATH = "pub_billaction";

	// ���ݶ�����i18n����
	public final static String BILLACTIONGROUP_PATH = "pub_billactiongroup";

	// ������Ŀi18n����
	public final static String BILLITEM_PATH = "fidap";

	// ��������ص�i18n����
	public final static String WORKFLOW_PATH = "pfworkflow";

	/**
	 * ���������û�������
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
	 * ����ҵ��������
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
	 * �Բ������޶��������Ķ��ﻯ
	 * 
	 * @param filterCode
	 *            ��������ʶ
	 * @param billType
	 *            ��������
	 * @return
	 */
	public static String i18nParticipantFilter(String filterCode, String billType) {
		String resId = "Dfilter." + billType + "." + filterCode;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(WORKFLOW_PATH, resId);
		return strName;
	}

	/**
	 * ����ϵͳ����i18n����
	 * 
	 * @param systemCode
	 *            ϵͳ���ͱ���
	 * @param defaultName
	 *            Ĭ������
	 * @return ���ȡ����������Դ������ʹ��Ĭ������
	 */
	public static String i18nSystemtypeName(String resId, String defaultName) {

		if (StringUtil.isEmptyWithTrim(resId))
			return defaultName;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(DAPSYSTEMP_PATH, resId);
		if (resId.equals(strName)) {
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}
	
	public static String i18nBilltypeName(String pkBilltype) {
		BilltypeVO billTypeVO = PfDataCache.getBillType(pkBilltype);
		return i18nBilltypeNameByVO(pkBilltype, billTypeVO);
	}

	/**
	 * ���ص�������i18n����
	 * 
	 * @param pkBilltype
	 *            �������ͱ��룬Ҳ������
	 * @param defaultName
	 *            Ĭ������
	 * @return ���ȡ����������Դ������ʹ��Ĭ������
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
	 * ���ص�������i18n����
	 * 
	 * @param pkBilltype
	 *            �������ͱ��룬Ҳ������
	 * @param billTypeVO
	 *            ����VO
	 * @return ��ȡ������Դ������ȡVO�����ֶΣ�û����ʹ��Ĭ������
	 */
	public static String i18nBilltypeNameByVO(String pkBilltype, BilltypeVO billTypeVO) {
		if (pkBilltype == null)
			return null;
		//����ʹ����Դ�����Ϊ���ݶ���
		String strName = null;
		//ͨ��VO�Ķ����ֶ�ȡ
		String billTypeName = billTypeVO == null ? null : billTypeVO.getBilltypenameOfCurrLang();
		if(!StringUtil.isEmptyWithTrim(billTypeName)) {
			strName = billTypeName;
		} else {
			//����ȡ������ȡĬ��ֵ
			strName = (billTypeVO == null || StringUtil.isEmptyWithTrim(billTypeVO.getBilltypename())) 
							? strName : billTypeVO.getBilltypename();
		}
		return strName;
	}

	/**
	 * ���ص�����Ŀ��i18n���� <li>��ʽ��itemname=XXX <li>
	 * ��Ϊ��������Ŀdap_defitem.itemname�ֶξ��Ƕ�����ԴID
	 * 
	 * @param itemName
	 *            ��ԴID
	 * @param defaultName
	 *            Ĭ������
	 * @return ���ȡ����������Դ������ʹ��Ĭ������
	 */
	public static String i18nBillItemName(String itemName, String defaultName) {
		if (itemName == null)
			return defaultName;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLITEM_PATH, itemName);
		if (itemName.equals(strName)) {
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * ���ﻯ������Ŀ��������Ϣ
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
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * ���ص�����Ŀ��i18n���� <li>��ʽ��D88.billno=XXX
	 * 
	 * @param pk_billtype
	 *            ��������PK
	 * @param attrname
	 *            ����
	 * @param defaultName
	 *            Ĭ������
	 * @return ���ȡ����������Դ������ʹ��Ĭ������
	 * @since NC50 ���ǲ���wsw�ķ�ʽ�ɣ�
	 */
	public static String i18nBillItemName2(String pk_billtype, String attrname, String defaultName) {
		if (attrname == null)
			return null;
		String resId = "D" + pk_billtype + "." + attrname;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLITEM_PATH, resId);
		if (resId.equals(strName)) {
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * ���ض���i18n����
	 * 
	 * @param pkBilltype
	 *            ��������PK
	 * @param pk_actiontype
	 *            ��������
	 * @param defaultName
	 *            Ĭ������
	 * @return
	 */
	public static String i18nActionName(String pkBilltype, String pk_actiontype, String defaultName) {
		String resId = "D" + pkBilltype + pk_actiontype;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLACTION_PATH, resId);
		if (resId.equals(strName)) {
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * ���ض�����i18n����
	 * 
	 * @param pkBilltype
	 *            ��������PK
	 * @param actionstyle
	 *            ���������
	 * @param defaultName
	 *            Ĭ������
	 * @return
	 */
	public static String i18nActionGroupName(String pkBilltype, String actionstyle, String defaultName) {
		String resId = "D" + pkBilltype + actionstyle;
		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID(BILLACTIONGROUP_PATH, resId);
		if (resId.equals(strName)) {
			// ȡ����������Դ��
			strName = defaultName == null ? strName : defaultName;
		}
		return strName;
	}

	/**
	 * ��VO�����в�ѯ���ݶ�������������
	 * 
	 * @param actionCode
	 *            ��������
	 * @param alActions
	 *            ����VO����
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
	 * ����û������֣������ݿ���δ���ã���ȡĬ������
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
	 * ������ѯ�û������֣����������ֽ��з���
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
