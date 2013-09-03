package uap.workflow.pub.util;

import java.util.ArrayList;
import java.util.Iterator;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pf.pub.PfDataCache;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.MultiLangUtil;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.graph.element.AbstractGraphObject;
import nc.vo.pub.pfflow.BillactionVO;

public class PfMultiLangUtil {

	private static String cuserid = InvocationInfoProxy.getInstance()
			.getUserId();

	private static String contentlang = (String) DBCacheQueryFacade.runQuery(
			"select contentlang from sm_user where cuserid='" + cuserid + "'",
			new ColumnProcessor());

	public static String getMultiGraphObejcetName(AbstractGraphObject obj) {
		if (obj == null)
			return "";
		int Suffix = MultiLangUtil.getCurrentLangSeq();
		String muliName = "";
		switch (Suffix) {
		case 1:
			muliName = obj.getName();
			break;
		case 2:
			muliName = obj.getName2();
			break;
		case 3:
			muliName = obj.getName3();
			break;
		}
		if (StringUtil.isEmptyWithTrim(muliName)
				&& !StringUtil.isEmptyWithTrim(contentlang)) {
			if (contentlang.startsWith("zhCN")) {
				muliName = obj.getName();
			} else if (contentlang.startsWith("zhTW")) {
				muliName = obj.getName2();
			} else if (contentlang.startsWith("enGB")) {
				muliName = obj.getName3();
			}
		}
		if (StringUtil.isEmptyWithTrim(muliName)) {
			muliName = obj.getName();
		}
		return muliName == null ? "" : muliName;
	}

	public static String getMultiBillActionName(BillactionVO billactionvo) {
		if (billactionvo == null)
			return "";
		int Suffix = MultiLangUtil.getCurrentLangSeq();
		String billactionName = "";
		switch (Suffix) {
		case 1:
			billactionName = billactionvo.getActionnote();
			break;
		case 2:
			billactionName = billactionvo.getActionnote2();
			break;
		case 3:
			billactionName = billactionvo.getActionnote3();
			break;
		case 4:
			billactionName = billactionvo.getActionnote4();
			break;
		case 5:
			billactionName = billactionvo.getActionnote5();
			break;
		case 6:
			billactionName = billactionvo.getActionnote6();
			break;
		}

		if (StringUtil.isEmptyWithTrim(billactionName)
				&& !StringUtil.isEmptyWithTrim(contentlang)) {
			if (contentlang.startsWith("zhCN")) {
				billactionName = billactionvo.getActionnote();
			} else if (contentlang.startsWith("zhTW")) {
				billactionName = billactionvo.getActionnote2();
			} else if (contentlang.startsWith("enGB")) {
				billactionName = billactionvo.getActionnote3();
			}
		}

		if (StringUtil.isEmptyWithTrim(billactionName)) {
			billactionName = billactionvo.getActionnote();
		}
		return billactionName;
	}

	public static String getMultiBillActionName(String actionCode,
			ArrayList alActions) {
		BillactionVO billactionvo = null;
		for (Iterator iterator = alActions.iterator(); iterator.hasNext();) {
			BillactionVO baVO = (BillactionVO) iterator.next();
			if (actionCode.equals(baVO.getActiontype())) {
				billactionvo = baVO;
				break;
			}
		}
		return getMultiBillActionName(billactionvo);
	}

	public static String getMultiBusitypeName(BusitypeVO vo) {
		if (vo == null) {
			return "";
		}
		int Suffix = MultiLangUtil.getCurrentLangSeq();
		String busitypename = "";
		switch (Suffix) {
		case 1:
			busitypename = vo.getBusiname();
			break;
		case 2:
			busitypename = vo.getBusiname2();
			break;
		case 3:
			busitypename = vo.getBusiname3();
			break;
		case 4:
			busitypename = vo.getBusiname4();
			break;
		case 5:
			busitypename = vo.getBusiname5();
			break;
		case 6:
			busitypename = vo.getBusiname6();
			break;
		}

		/**
		 * 找不到内容语种再找个性化中心的用户个性化语种，如果找不到就找系统的默认主语种，
		 * */
		if (StringUtil.isEmptyWithTrim(busitypename)
				&& !StringUtil.isEmptyWithTrim(contentlang)) {
			if (contentlang.startsWith("zhCN")) {
				busitypename = vo.getBusiname();
			} else if (contentlang.startsWith("zhTW")) {
				busitypename = vo.getBusiname2();
			} else if (contentlang.startsWith("enGB")) {
				busitypename = vo.getBusiname3();
			}
		}

		if (StringUtil.isEmptyWithTrim(busitypename)) {
			busitypename = vo.getBusiname();
		}

		return busitypename;
	}

	public static String getMultiBilltypeName(BilltypeVO vo) {
		if (vo == null)
			return "";
		int Suffix = MultiLangUtil.getCurrentLangSeq();
		String billtypeName = "";
		switch (Suffix) {
		case 1:
			billtypeName = vo.getBilltypename();
			break;
		case 2:
			billtypeName = vo.getBilltypename2();
			break;
		case 3:
			billtypeName = vo.getBilltypename3();
			break;
		case 4:
			billtypeName = vo.getBilltypename4();
			break;
		case 5:
			billtypeName = vo.getBilltypename5();
			break;
		case 6:
			billtypeName = vo.getBilltypename6();
			break;
		default:
			billtypeName = vo.getBilltypename2();
			break;
		}
		/**
		 * 找不到内容语种再找个性化中心的用户个性化语种，如果找不到就找系统的默认主语种，
		 * */
		if (StringUtil.isEmptyWithTrim(billtypeName)
				&& !StringUtil.isEmptyWithTrim(contentlang)) {
			if (contentlang.startsWith("zhCN")) {
				billtypeName = vo.getBilltypename();
			} else if (contentlang.startsWith("zhTW")) {
				billtypeName = vo.getBilltypename2();
			} else if (contentlang.startsWith("enGB")) {
				billtypeName = vo.getBilltypename3();
			}
		}

		if (StringUtil.isEmptyWithTrim(billtypeName)) {
			billtypeName = vo.getBilltypename();
		}

		return billtypeName;
	}

	public static String getMultiBilltypeName(String billtype) {
		BilltypeVO vo = PfDataCache.getBillType(billtype);
		return getMultiBilltypeName(vo);
	}

	/**
	 * 取得SuperVO在当前语种下的名称
	 * 
	 * @param vo
	 * @param nameField
	 *            SuperVO中name字段的名称
	 * @param defaultName
	 *            若SuperVO中当前语种名称为空，则返回此名称
	 * @return
	 */
	public static String getSuperVONameOfCurrentLang(SuperVO vo,
			String nameField, String defaultName) {
		String suffix = MultiLangUtil.getCurrentLangSeqSuffix();
		String name = (String) vo.getAttributeValue(nameField + suffix);

		if (StringUtil.isEmptyWithTrim(name)) {
			return defaultName;
		} else {
			return name;
		}
	}

	public static String getSuperVONameOfCurrentLang(SuperVO vo,
			String nameField) {
		String defaultName = (String) vo.getAttributeValue(nameField);
		return getSuperVONameOfCurrentLang(vo, nameField, defaultName);
	}

}
