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
 * 平台的客户端缓冲
 * 
 * @author fangj 2001-12-15
 * @modifier leijun 2005-10-7 使用NC50缓存机制，区分不同帐套
 * @modifier leijun 2006-3-30 使用版本敏感的缓存
 * @modifier leijun 2008-10 增加一个缓存对象
 */
public class PfUIDataCache extends PfDataCache {

	// 缓冲公司+单据类型的业务类型数据
	public static final String BUSIBYCORPANDBILL = "BUSIBYCORPANDBILL";

	// 缓冲公司+单据类型(交易类型)+业务类型的来源数据
	private static final String SOURCEBYCORPANDBILLANDBUSI = "SOURCEBYCORPANDBILLANDBUSI";

	// 缓冲公司+单据类型+交易类型+业务类型的来源数据
	// private static final String SOURCEBYCORPANDBILLANDBUSI2 =
	// "SOURCEBYCORPANDBILLANDBUSI2";

	// 缓冲单据类型+动作组的信息数据
	private static final String BUTTONBYBILLANDGRP = "BUTTONBYBILLANDGRP";

	// 缓冲BdInfo数据
	private static final String KHHBDINFO = "KHHBDINFO";

	// 客户端消息中心配置信息
	private static final String MSGPANEL_OPTIONS = "MSGPANELOPTIONS";

	// 缓冲消息片注册的数据
	private static final String MESSAGE_LETS = "MESSAGELETS";

	private PfUIDataCache() {
		super();
	}

	/**
	 * 获得初始化的客户端平台缓存管理对象
	 * <li>文件策略
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
		// 获得当前登录的数据源
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
	 * 获得缓存的客户端消息面板配置
	 * 
	 * @return
	 */
	public static MessagePanelOptions getMsgPanelOptions() {
		// XXX:每个用户都有自己的设置
		MessagePanelOptions mpo = null;
		CacheKey cacheKey = null;
		try {
			String userId = PfUtilUITools.getLoginUser();

			cacheKey = CacheKey.createKey(userId + MSGPANEL_OPTIONS);

			// 使用客户端的文件缓存策略
			mpo = (MessagePanelOptions) getFileCacheManager().get(cacheKey);
			if (mpo == null) {
				mpo = new MessagePanelOptions();
				getFileCacheManager().put(cacheKey, mpo);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			if (cacheKey != null && mpo == null) {
				// WARN::出现异常后也需要空的对象
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
	 * 缓存客户端消息面板配置
	 * <li>每个用户都有自己的设置
	 * 
	 * @param mpo
	 */
	public static void putMsgPanelOptions(MessagePanelOptions mpo) {
		// 每个用户都有自己的设置
		String userId = PfUtilUITools.getLoginUser();
		CacheKey cacheKey = CacheKey.createKey(userId + MSGPANEL_OPTIONS);
		try {
			getFileCacheManager().put(cacheKey, mpo);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 从缓存中获取基本档案VO
	 * 
	 * @param strPK
	 *            基本档案PK
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
	 * 根据编码获取某个消息片的注册信息
	 * 
	 * @param strCode
	 *            消息片的标识编码
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
	 * 从pub_messagelet表获取所有消息片注册信息
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
	 * 从缓存中 根据集团PK,单据类型(或交易类型)PK查询业务流程类型
	 * 
	 * @param pk_group
	 *            集团PK
	 * @param billType
	 *            单据类型(或交易类型)PK
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
	 * 从缓存中 根据单据类型/组查询动作
	 * 
	 * @param billType
	 *            单据类型PK
	 * @param actionStyle
	 *            动作组编码
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
	 * 查询某业务类型下某单据类型(或交易类型)的所有来源单据VO数组
	 * <li>不包含间接来源关系
	 * <li>如果是自制单据，也额外作为一个VO返回
	 * 
	 * @param pkGroup
	 * @param BillOrTransType
	 *            单据类型或交易类型
	 * @param businessType
	 * @param includeBillType
	 *            billOrTranstype为交易类型时候，是否返回所属单据类型的来源
	 * @return
	 */
	public static BillbusinessVO[] getSourceByCorpAndBillAndBusi(String pkGroup, String BillOrTransType, String businessType, boolean includeBillType) {
		String hashKey = null;
		HashMap hashCacheObj = null;
		try {
			hashCacheObj = (HashMap) getVersionSensitiveCache().get(SOURCEBYCORPANDBILLANDBUSI);
			// 主键
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
	 * 获取所有基本档案VO，并以PK-Object保存
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
	 * 根据集团PK,单据类型(或交易类型)PK查询业务类型
	 */
	private static void setBusiByCorpAndBill(HashMap tmpHas, String pk_group, String billType) {
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// 键
			String key = pk_group + billType;
			BusitypeVO[] billReferVos = pfConfig.querybillBusinessType(pk_group, billType);
			if (billReferVos != null)
				tmpHas.put(key, billReferVos);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 获取某个单据类型下某动作组的所有单据动作,并缓存
	 * 
	 * @param tmpHas
	 * @param billType
	 *            单据类型PK
	 * @param actionStyle
	 *            动作组编码
	 */
	private static void setButtonByBillAndGrp(HashMap tmpHas, String billType, String actionStyle) {
		BillactionVO[] billActionVos = null;
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// Hash键
			String key = billType + actionStyle;
			billActionVos = pfConfig.querybillActionStyle(billType, actionStyle);
			if (billActionVos != null)
				tmpHas.put(key, billActionVos);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 查询某业务类型下某单据类型的所有来源单据VO数组
	 * <li>不包含间接来源关系
	 * <li>如果是自制单据，也额外作为一个VO返回
	 * 
	 * @param hashCacheObj
	 * @param pkGroup
	 * @param billOrTranstype
	 * @param businessType
	 * @param includeBillType
	 *            billOrTranstype为交易类型时候，是否返回所属单据类型的来源
	 */
	private static void setSourceByCorpAndBillAndBusi(HashMap hashCacheObj, String pkGroup, String billOrTranstype, String businessType, boolean includeBillType) {
		BillbusinessVO[] billReferVo = null;
		try {
			IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());

			// 主键
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