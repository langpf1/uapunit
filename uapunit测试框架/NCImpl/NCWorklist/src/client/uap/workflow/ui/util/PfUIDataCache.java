package uap.workflow.ui.util;

import java.util.Collection;
import java.util.HashMap;

import uap.workflow.ui.taskhandling.MessagePanelOptions;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFConfig;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.cache.CacheKey;
import nc.vo.cache.CacheManager;
import nc.vo.cache.ICache;
import nc.vo.cache.config.CacheConfig;
import nc.vo.cache.exception.CacheException;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.msg.MessageLetVO;
import nc.vo.pub.pfflow.BillactionVO;
import nc.vo.pub.pfflow01.BillbusinessVO;

/**
 * ƽ̨�Ŀͻ��˻���
 * 
 * @author fangj 2001-12-15
 * @modifier leijun 2005-10-7 ʹ��NC50������ƣ����ֲ�ͬ����
 * @modifier leijun 2006-3-30 ʹ�ð汾���еĻ���
 * @modifier leijun 2008-10 ����һ���������
 */
public class PfUIDataCache extends PfDataCache {

	// ���幫˾+�������͵�ҵ����������
	public static final String BUSIBYCORPANDBILL = "BUSIBYCORPANDBILL";

	// ���幫˾+��������(��������)+ҵ�����͵���Դ����
	private static final String SOURCEBYCORPANDBILLANDBUSI = "SOURCEBYCORPANDBILLANDBUSI";

	// ���幫˾+��������+��������+ҵ�����͵���Դ����
	// private static final String SOURCEBYCORPANDBILLANDBUSI2 =
	// "SOURCEBYCORPANDBILLANDBUSI2";

	// ���嵥������+���������Ϣ����
	private static final String BUTTONBYBILLANDGRP = "BUTTONBYBILLANDGRP";

	// ����BdInfo����
	private static final String KHHBDINFO = "KHHBDINFO";

	// �ͻ�����Ϣ����������Ϣ
	private static final String MSGPANEL_OPTIONS = "MSGPANELOPTIONS";

	// ������ϢƬע�������
	private static final String MESSAGE_LETS = "MESSAGELETS";

	private PfUIDataCache() {
		super();
	}

	/**
	 * ��ó�ʼ���Ŀͻ���ƽ̨����������
	 * <li>�ļ�����
	 * 
	 * @return
	 * @throws CacheException
	 */
	private static ICache getFileCacheManager() throws CacheException {
		CacheConfig cc = new CacheConfig();
		cc.setRegionName(getCacheRegion());
		cc.setCacheType(CacheConfig.CacheType.DYNAMIC_FILE);
		cc.setFlushInterval(-1);
		cc.setMLevel(CacheConfig.MemoryCacheLevel.STRONG);
		cc.setNeedLog(false);
		cc.setSize(-1);
		ICache cache = CacheManager.getInstance().getCache(cc);
		return cache;
	}

	public static String getCacheRegion() {
		// ��õ�ǰ��¼������Դ
		String dsName = WorkbenchEnvironment.getInstance().getDSName();
		return dsName + CACHE_REGION;
	}

	/**
	 * @param billType
	 * @return
	 */
	public static BilltypeVO getBillTypeUI(String billType) {
		return getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billType).buildPkGroup(PfUtilUITools.getLoginGroup()));
	}

	/**
	 * ��û���Ŀͻ�����Ϣ�������
	 * 
	 * @return
	 */
	public static MessagePanelOptions getMsgPanelOptions() {
		// XXX:ÿ���û������Լ�������
		MessagePanelOptions mpo = null;
		CacheKey cacheKey = null;
		try {
			String userId = PfUtilUITools.getLoginUser();

			cacheKey = CacheKey.createKey(userId + MSGPANEL_OPTIONS);

			// ʹ�ÿͻ��˵��ļ��������
			mpo = (MessagePanelOptions) getFileCacheManager().get(cacheKey);
			if (mpo == null) {
				mpo = new MessagePanelOptions();
				getFileCacheManager().put(cacheKey, mpo);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			if (cacheKey != null && mpo == null) {
				// WARN::�����쳣��Ҳ��Ҫ�յĶ���
				mpo = new MessagePanelOptions();
				try {
					getFileCacheManager().put(cacheKey, mpo);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(), ex);
				}
			} else {
				mpo = new MessagePanelOptions();
			}
		}
		return mpo;
	}

	/**
	 * ����ͻ�����Ϣ�������
	 * <li>ÿ���û������Լ�������
	 * 
	 * @param mpo
	 */
	public static void putMsgPanelOptions(MessagePanelOptions mpo) {
		// ÿ���û������Լ�������
		String userId = PfUtilUITools.getLoginUser();
		CacheKey cacheKey = CacheKey.createKey(userId + MSGPANEL_OPTIONS);
		try {
			getFileCacheManager().put(cacheKey, mpo);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �ӻ����л�ȡ��������VO
	 * 
	 * @param strPK
	 *            ��������PK
	 */
	public static nc.vo.pf.pub.BasedocVO getBdinfo(String strPK) {
		HashMap hashCacheObj = null;
		String hashKey = strPK;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(KHHBDINFO);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setBdInfo(hashCacheObj);
				getVersionSensitiveCache().put(KHHBDINFO, hashCacheObj);
			} else if (!hashCacheObj.containsKey(hashKey)) {
				setBdInfo(hashCacheObj);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return (nc.vo.pf.pub.BasedocVO) hashCacheObj.get(hashKey);
	}

	/**
	 * ���ݱ����ȡĳ����ϢƬ��ע����Ϣ
	 * 
	 * @param strCode
	 *            ��ϢƬ�ı�ʶ����
	 * @return
	 */
	public static MessageLetVO getMessageLet(String strCode) {
		HashMap hashCacheObj = null;
		String hashKey = strCode;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(MESSAGE_LETS);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setMessageLetInfo(hashCacheObj);
				getVersionSensitiveCache().put(MESSAGE_LETS, hashCacheObj);
			} else if (!hashCacheObj.containsKey(hashKey)) {
				setMessageLetInfo(hashCacheObj);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return (MessageLetVO) hashCacheObj.get(hashKey);
	}

	/**
	 * ��pub_messagelet���ȡ������ϢƬע����Ϣ
	 * 
	 * @param tmpHas
	 */
	private static void setMessageLetInfo(HashMap tmpHas) {
		try {
			Collection co = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveAll(MessageLetVO.class);
			for (Object obj : co) {
				MessageLetVO letVo = (MessageLetVO) obj;
				tmpHas.put(letVo.getCode(), letVo);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * �ӻ����� ���ݼ���PK,��������(��������)PK��ѯҵ����������
	 * 
	 * @param pk_group
	 *            ����PK
	 * @param billType
	 *            ��������(��������)PK
	 */
	public static BusitypeVO[] getBusiByCorpAndBill(String pk_group, String billType) {
		HashMap hashCacheObj = null;
		String hashKey = pk_group + billType;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(BUSIBYCORPANDBILL);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setBusiByCorpAndBill(hashCacheObj, pk_group, billType);
				getVersionSensitiveCache().put(BUSIBYCORPANDBILL, hashCacheObj);
			} else if (!hashCacheObj.containsKey(hashKey)) {
				setBusiByCorpAndBill(hashCacheObj, pk_group, billType);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return (BusitypeVO[]) hashCacheObj.get(hashKey);
	}

	/**
	 * �ӻ����� ���ݵ�������/���ѯ����
	 * 
	 * @param billType
	 *            ��������PK
	 * @param actionStyle
	 *            ���������
	 * @return
	 */
	public static BillactionVO[] getButtonByBillAndGrp(String billType, String actionStyle) {
		String hashKey = billType + actionStyle;
		HashMap hashCacheObj = null;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(BUTTONBYBILLANDGRP);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setButtonByBillAndGrp(hashCacheObj, billType, actionStyle);
				getVersionSensitiveCache().put(BUTTONBYBILLANDGRP, hashCacheObj);
			} else if (!hashCacheObj.containsKey(hashKey)) {
				setButtonByBillAndGrp(hashCacheObj, billType, actionStyle);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return (BillactionVO[]) hashCacheObj.get(hashKey);
	}

	/**
	 * ��ѯĳҵ��������ĳ��������(��������)��������Դ����VO����
	 * <li>�����������Դ��ϵ
	 * <li>��������Ƶ��ݣ�Ҳ������Ϊһ��VO����
	 * 
	 * @param pkGroup
	 * @param BillOrTransType
	 *            �������ͻ�������
	 * @param businessType
	 * @param includeBillType
	 *            billOrTranstypeΪ��������ʱ���Ƿ񷵻������������͵���Դ
	 * @return
	 */
	public static BillbusinessVO[] getSourceByCorpAndBillAndBusi(String pkGroup, String BillOrTransType, String businessType, boolean includeBillType) {
		String hashKey = null;
		HashMap hashCacheObj = null;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(SOURCEBYCORPANDBILLANDBUSI);
			// ����
			if (businessType == null) {
				hashKey = pkGroup + ":" + BillOrTransType;
			} else {
				hashKey = pkGroup + ":" + BillOrTransType + ":" + businessType;
			}
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setSourceByCorpAndBillAndBusi(hashCacheObj, pkGroup, BillOrTransType, businessType, includeBillType);
				getVersionSensitiveCache().put(SOURCEBYCORPANDBILLANDBUSI, hashCacheObj);
			} else if (!hashCacheObj.containsKey(hashKey)) {
				setSourceByCorpAndBillAndBusi(hashCacheObj, pkGroup, BillOrTransType, businessType, includeBillType);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return (BillbusinessVO[]) hashCacheObj.get(hashKey);
	}

	/**
	 * ��ȡ���л�������VO������PK-Object����
	 */
	private static void setBdInfo(HashMap tmpHas) {
		try {
			nc.vo.pf.pub.BasedocVO[] tmpVos = PfUtilBaseTools.getAllBasedocVO();
			for (int i = 0; i < (tmpVos == null ? 0 : tmpVos.length); i++)
				tmpHas.put(tmpVos[i].getDocPK(), tmpVos[i]);

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * ���ݼ���PK,��������(��������)PK��ѯҵ������
	 */
	private static void setBusiByCorpAndBill(HashMap tmpHas, String pk_group, String billType) {
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// ��
			String key = pk_group + billType;
			BusitypeVO[] billReferVos = pfConfig.querybillBusinessType(pk_group, billType);
			if (billReferVos != null)
				tmpHas.put(key, billReferVos);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * ��ȡĳ������������ĳ����������е��ݶ���,������
	 * 
	 * @param tmpHas
	 * @param billType
	 *            ��������PK
	 * @param actionStyle
	 *            ���������
	 */
	private static void setButtonByBillAndGrp(HashMap tmpHas, String billType, String actionStyle) {
		BillactionVO[] billActionVos = null;
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// Hash��
			String key = billType + actionStyle;
			billActionVos = pfConfig.querybillActionStyle(billType, actionStyle);
			if (billActionVos != null)
				tmpHas.put(key, billActionVos);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * ��ѯĳҵ��������ĳ�������͵�������Դ����VO����
	 * <li>�����������Դ��ϵ
	 * <li>��������Ƶ��ݣ�Ҳ������Ϊһ��VO����
	 * 
	 * @param hashCacheObj
	 * @param pkGroup
	 * @param billOrTranstype
	 * @param businessType
	 * @param includeBillType
	 *            billOrTranstypeΪ��������ʱ���Ƿ񷵻������������͵���Դ
	 */
	private static void setSourceByCorpAndBillAndBusi(HashMap hashCacheObj, String pkGroup, String billOrTranstype, String businessType, boolean includeBillType) {
		BillbusinessVO[] billReferVo = null;
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// ����
			String key = pkGroup + ":" + billOrTranstype + ":" + businessType;
			billReferVo = pfConfig.querybillSource(pkGroup, billOrTranstype, businessType, includeBillType);
			hashCacheObj.put(key, billReferVo);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}
	
	public static String getBillTypeNameByCode(String billtypecode){
		String sql = "select billtypename from bd_billtype where pk_billtypecode=? and ((pk_group=? and istransaction='Y') or isnull(istransaction,'N')='N')" ;
		SQLParameter para = new SQLParameter();
		para.addParam(billtypecode);
		para.addParam(InvocationInfoProxy.getInstance().getGroupId());
//		para.addParam(IOrgConst.GLOBEORG);
		return (String)DBCacheQueryFacade.runQuery(sql, para, new ColumnProcessor());
	}
}