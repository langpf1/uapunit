package uap.workflow.pub.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uap.workflow.admin.IWorkflowDefine;
import uap.workflow.engine.core.IProcessDefinition;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bbd.func.IModuleQueryService;
import nc.itf.uap.pf.IPFBillItfDef;
import nc.itf.uap.pf.IWFModuleService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.cache.ext.CacheToMapAdapter;
import nc.vo.cache.ext.ElementVersionSensitiveMap;
import nc.vo.cache.ext.ICacheVersionMonitor;
import nc.vo.cache.ext.ObjectCacheVersionMonitor;
import nc.vo.cache.ext.VersionMonitorFactory;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.BillItfDefVO;
import nc.vo.pf.pub.FunctionVO;
import nc.vo.pf.pub.IPfPubConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.forwardbilltype.ForwardBillTypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.pfflow.BillactionVO;
import nc.vo.sm.funcreg.ModuleVO;
import nc.vo.uap.pf.DynamicOrgRegInfo;
import nc.vo.uap.pf.DynamicOrganizeUnitRegistry;
import nc.vo.wfengine.core.application.WorkflowgadgetVO;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.ext.ApproveFlowAdjustVO;

/**
 * 平台缓存工具类 <li>前、后台均可用
 * 
 * @author fangj 2003-3
 * @modifier leijun 2005-10-7 使用NC50缓存机制，区分不同数据源
 * @modifier leijun 2006-3-30 使用版本敏感的缓存
 * @modifier leijun 2009-1 增加流程定义对象的缓存
 */
public class PfDataCache {
	
	public static final String PRESET_TRANSTYPE_GROUPID = IPfPubConst.PreData_PKGroup;

	/**
	 * 版本监视器工厂
	 */
	static class PfVersionMonitorFacotry implements VersionMonitorFactory {

		public ICacheVersionMonitor createVersionMonitor(Object arg0) {
			// 1min 才检查版本的有效性
			return new ObjectCacheVersionMonitor((String) arg0, 1000 * 60 * 1);
		}
	}

	/**
	 * 平台缓存区域
	 */
	protected static final String CACHE_REGION = "platform";

	/**
	 * 单据类型表bd_billtype的缓存
	 */
	public static String STR_BILLTYPEINFO = "BILLTYPEINFO";

	/**
	 * 工作流组件表pub_workflowgadget的缓存
	 */
	public static String STR_WORKFLOWGADGETINFO = "WORKFLOWGADGETINFO";

	/**
	 * 单据类型辅表bd_billtype2的缓存
	 */
	public static String STR_BILLTYPE2INFO = "BILLTYPE2INFO";

	/**
	 * 单据类型附表bd_fwdbilltype的缓存
	 */
	public static String STR_FORWARDBILLTYPE = "FORWARDBILLTYPE";
	/**
	 * 单据来源信息表pub_billtobillrefer的缓存
	 */
	public static final String STR_BILLTOBILLREFER = "KHHINFOHAS";

	/**
	 * 单据大类与单据类型对照的缓存
	 */
	public static String STR_BILLSTYLETOTYPE = "BILLSTYLETOTYPE";

	/**
	 * 单据函数表pub_function的缓存
	 */
	public static String STR_BILLTYPETOFUNCTION = "BILLTYPETOFUNCTION";

	/**
	 * 系统类型表dap_dapsystem的缓存,以systypecode为key
	 */
	public static String STR_SYSTEMTYPEINFO = "SYSTEMTYPEINFO";

	/**
	 * 系统类型表dap_dapsystem的缓存，以modeuleid为key
	 */
	public static String STR_SYSTEMTYPEINFO_MODULE = "SYSTEMTYPEINFOMODULE";

	/**
	 * 动态组织注册表pub_dynamicorg中注册信息的缓存
	 */
	private static String DYNAMIC_ORG_REGINFO = "DYNAMICORGINFO";

	/**
	 * 单据项目表的缓存
	 */
	public static final String STR_BILLITEM = "BILLITEM";

	/**
	 * 单据动作表的缓存
	 */
	public static final String STR_BILLACTION = "BILLACTION";

	/**
	 * 流程定义对象的缓存
	 */
	public static final String STR_WORKFLOWPROCESS = "WORKFLOWPROCESS";
	
	/**
	 * 单据接口定义缓存
	 * */
	public static final String STR_BILLITFDEF="BILLITFDEF";
	
	/**
	 * 不同数据源的缓存空间
	 */
	private static Hashtable dsName_instance_map = new Hashtable();

	private static String getCurrentDs() {
		// 获取当前使用的数据源名称
		return InvocationInfoProxy.getInstance().getUserDataSource();
	}

	/**
	 * 获得初始化的客户端平台缓存Map <li>对版本敏感
	 * 
	 * @return
	 */
	public static ElementVersionSensitiveMap getVersionSensitiveCache() {
		String currDS = getCurrentDs();
		if (dsName_instance_map.get(currDS) == null) {
			CacheToMapAdapter adapter = (CacheToMapAdapter) CacheToMapAdapter
					.getInstance(currDS + CACHE_REGION);
			ElementVersionSensitiveMap versionMap = new ElementVersionSensitiveMap(
					adapter, new PfVersionMonitorFacotry());
			dsName_instance_map.put(currDS, versionMap);
		}
		return (ElementVersionSensitiveMap) dsName_instance_map.get(currDS);
	}

	/**
	 * 从服务器缓存中 获取某个单据类型额外注册信息
	 * 
	 * @param billType
	 *            单据类型，或交易类型
	 * @param classtype
	 *            注册类 的类型
	 * @return Billtype2VO数组
	 */
	public static ArrayList<Billtype2VO> getBillType2Info(String billType,
			int classtype) {
		HashMap<Integer, ArrayList<Billtype2VO>> hashCacheObj = null;
		String strTypeCode = PfUtilBaseTools.getRealBilltype(billType);
		try {
			hashCacheObj = (HashMap<Integer, ArrayList<Billtype2VO>>) getVersionSensitiveCache()
					.get(STR_BILLTYPE2INFO);

			if (hashCacheObj == null) {
				hashCacheObj = queryAllBilltype2();
				// 缓存所有单据类型VO
				getVersionSensitiveCache().put(STR_BILLTYPE2INFO, hashCacheObj);
			} else if (!hashCacheObj.containsKey(classtype)
					&& !hasCachedBilltype2(hashCacheObj, strTypeCode)) {
				// 如果找不到，则再次获取缓存（即更新之）
				HashMap<Integer, ArrayList<Billtype2VO>> hmBilltype2VOs = queryAllBilltype2();
				hashCacheObj.putAll(hmBilltype2VOs);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

		return findBilltype2ByBilltypeAndClasstype(strTypeCode, classtype,
				hashCacheObj);
	}

	/**
	 * 判定缓存对象中是否已经缓存了单据类型的额外注册信息
	 * 
	 * @param hashCacheObj
	 * @param billType
	 * @return
	 */
	private static boolean hasCachedBilltype2(
			HashMap<Integer, ArrayList<Billtype2VO>> hashCacheObj,
			String billType) {
		for (Iterator<ArrayList<Billtype2VO>> iter = hashCacheObj.values()
				.iterator(); iter.hasNext();) {
			ArrayList<Billtype2VO> alBilltype2VO = iter.next();
			for (Billtype2VO bt2VO : alBilltype2VO) {
				if (billType.equals(bt2VO.getPk_billtype()))
					return true;
			}
		}
		return false;
	}

	private static HashMap<Integer, ArrayList<Billtype2VO>> queryAllBilltype2()
			throws BusinessException {
		HashMap<Integer, ArrayList<Billtype2VO>> hm = new HashMap<Integer, ArrayList<Billtype2VO>>();
		// 数据量为150行以下，返回所有影响不大
		Collection<Billtype2VO> coAll = NCLocator.getInstance()
				.lookup(IUAPQueryBS.class).retrieveAll(Billtype2VO.class);
		for (Iterator<Billtype2VO> iterator = coAll.iterator(); iterator
				.hasNext();) {
			Billtype2VO bt2VO = iterator.next();
			ArrayList<Billtype2VO> alTemp = hm.get(bt2VO.getClasstype());
			if (alTemp == null) {
				alTemp = new ArrayList<Billtype2VO>();
				hm.put(bt2VO.getClasstype(), alTemp);
			}
			alTemp.add(bt2VO);
		}

		return hm;
	}

	/**
	 * 查找单据类型的某类型的注册类信息
	 * 
	 * @param billType
	 * @param classtype
	 * @param hashCacheObj
	 * @return
	 */
	private static ArrayList<Billtype2VO> findBilltype2ByBilltypeAndClasstype(
			String billType, int classtype,
			HashMap<Integer, ArrayList<Billtype2VO>> hashCacheObj) {
		ArrayList<Billtype2VO> alRet = new ArrayList<Billtype2VO>();
		ArrayList<Billtype2VO> alBilltype2VO = hashCacheObj.get(classtype);
		if (alBilltype2VO == null)
			return alRet;
		for (Iterator<Billtype2VO> iter = alBilltype2VO.iterator(); iter
				.hasNext();) {
			Billtype2VO bt2VO = iter.next();
			if (classtype == bt2VO.getClasstype()) {
				// FIXME::XX表示通用类型
				if (billType.equals(bt2VO.getPk_billtype())
						|| bt2VO.getPk_billtype().equals(
								MessageVO.NOT_BUSINESS_MSG)) {
					alRet.add(bt2VO);
				}
			}
		}
		return alRet;
	}

	/**
	 * zhouzhenga+ 从服务器缓存中 获取某个单据类型的下游单据信息
	 * 
	 * @param billType
	 *            单据类型，或交易类型
	 * @param classtype
	 *            注册类 的类型
	 * @return Billtype2VO数组
	 */

	public static ArrayList<ForwardBillTypeVO> getFwdBilltypeInfo(
			String pk_backBillType) {
		HashMap<String, ArrayList<ForwardBillTypeVO>> hashCacheObj = null;
		try {
			hashCacheObj = (HashMap<String, ArrayList<ForwardBillTypeVO>>) getVersionSensitiveCache()
					.get(STR_FORWARDBILLTYPE);

			if (hashCacheObj == null) {
				hashCacheObj = queryAllFwdBilltype();
				// 缓存所有单据类型VO
				getVersionSensitiveCache().put(STR_FORWARDBILLTYPE,
						hashCacheObj);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return findFwdBilltypeByBilltype(pk_backBillType, hashCacheObj);
	}

	/**
	 * 根据上游单据类型查找下游单据类型列表
	 * 
	 * @param pk_backBillType
	 *            上游单据类型
	 * @param hashCacheObj
	 * @return
	 */
	private static ArrayList<ForwardBillTypeVO> findFwdBilltypeByBilltype(
			String pk_backBillType,
			HashMap<String, ArrayList<ForwardBillTypeVO>> hashCacheObj) {
		ArrayList<ForwardBillTypeVO> alBilltype2VO = hashCacheObj
				.get(pk_backBillType);
		return alBilltype2VO;
	}

	/**
	 * 查找所有的下游单据类型
	 * 
	 * @return HashMap
	 */
	private static HashMap<String, ArrayList<ForwardBillTypeVO>> queryAllFwdBilltype()
			throws BusinessException {
		HashMap<String, ArrayList<ForwardBillTypeVO>> hm = new HashMap<String, ArrayList<ForwardBillTypeVO>>();
		Collection<ForwardBillTypeVO> coAll = NCLocator.getInstance()
				.lookup(IUAPQueryBS.class).retrieveAll(ForwardBillTypeVO.class);// 函数未被调用，先不管
		for (Iterator<ForwardBillTypeVO> iterator = coAll.iterator(); iterator
				.hasNext();) {
			ForwardBillTypeVO bt2VO = iterator.next();
			ArrayList<ForwardBillTypeVO> alTemp = hm.get(bt2VO
					.getPk_backbilltype());
			if (alTemp == null) {
				alTemp = new ArrayList<ForwardBillTypeVO>();
				hm.put(bt2VO.getPk_backbilltype(), alTemp);
			}
			alTemp.add(bt2VO);
		}
		return hm;
	}

	/**
	 * 从缓存中获取某个单据的所有单据函数信息
	 * 
	 * @param billtype
	 * @return
	 */
	public static ArrayList<FunctionVO> getFunctionsOfBilltype(String billtype) {
		String realBilltype = billtype;
		if (PfUtilBaseTools.isTranstype(billtype))
			realBilltype = PfUtilBaseTools.getRealBilltype(billtype);
		HashMap<String, List<FunctionVO>> hashCacheObj = (HashMap<String, List<FunctionVO>>) getVersionSensitiveCache()
				.get(STR_BILLTYPETOFUNCTION);
		if (hashCacheObj == null) {
			hashCacheObj = new HashMap<String, List<FunctionVO>>();
			setFunctionVOs(hashCacheObj, realBilltype);
			getVersionSensitiveCache()
					.put(STR_BILLTYPETOFUNCTION, hashCacheObj);
		} else if (!hashCacheObj.containsKey(realBilltype)) {
			setFunctionVOs(hashCacheObj, realBilltype);
		}

		return (ArrayList<FunctionVO>) hashCacheObj.get(realBilltype);
	}

	/**
	 * 从缓存中 获取所有工作流组件VO
	 * 
	 * @param billType
	 *            单据类型PK
	 */
	public static ArrayList<WorkflowgadgetVO> getWorkflowGadget(String billType) {
		HashMap<String, ArrayList<WorkflowgadgetVO>> hashCacheObj = null;
		try {
			hashCacheObj = (HashMap<String, ArrayList<WorkflowgadgetVO>>) getVersionSensitiveCache()
					.get(STR_WORKFLOWGADGETINFO);

			if (hashCacheObj == null) {
				hashCacheObj = new HashMap<String, ArrayList<WorkflowgadgetVO>>();
				setWorkflowgadgetVOs(hashCacheObj, billType);
				// 缓存所有工作流组件VO
				getVersionSensitiveCache().put(STR_WORKFLOWGADGETINFO,
						hashCacheObj);
			} else if (!hashCacheObj.containsKey(billType)) {
				// 如果找不到，则再次获取缓存（即更新之）
				setWorkflowgadgetVOs(hashCacheObj, billType);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

		return hashCacheObj.get(billType);
	}

	/**
	 * 获取所有工作流组件VO，并以PK-Object方式缓存
	 */
	private static void setWorkflowgadgetVOs(
			HashMap<String, ArrayList<WorkflowgadgetVO>> hasWorkflowgadgetInfo,
			String billtype) throws BusinessException {
		// 查询某单据类型下的所有工作流组件
		try {
			// 查询数据库
			WorkflowgadgetVO[] gadgetVOs = NCLocator.getInstance()
					.lookup(IWFModuleService.class).queryGadgets(billtype);
			if (gadgetVOs == null)
				gadgetVOs = new WorkflowgadgetVO[0];
			ArrayList<WorkflowgadgetVO> alRet = new ArrayList<WorkflowgadgetVO>();
			for (WorkflowgadgetVO gadgetVO : gadgetVOs) {
				alRet.add(gadgetVO);
			}
			hasWorkflowgadgetInfo.put(billtype, alRet);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

	}



	/**
	 * 根据pk_billtypecode取单据类型或当前集团下交易类型
	 * 
	 * @param billtype
	 * @return
	 */
	public static BilltypeVO getBillTypeInfo(String billtype) {
		return getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billtype)
				.buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
	}

	/**
	 * @param billtype
	 * @return
	 */
	public static BilltypeVO getBillTypeInfo(String pkGroup, String billtype) {
		return getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billtype)
				.buildPkGroup(pkGroup));
	}
	
	/**
	 * @param billType
	 * @return
	 */
	public static BilltypeVO getBillType(String billType) {
		return getBillTypeInfo(billType);
	}

	/**
	 * @param billType
	 * @return
	 */
	public static void removeBillType(String billType) {
		BillTypeCacheKey cacheKey = new BillTypeCacheKey().buildBilltype(billType)
				.buildPkGroup(InvocationInfoProxy.getInstance().getGroupId());

		if (cacheKey == null || StringUtil.isEmptyWithTrim(billType))
			return ;
		HashMap<String, BilltypeVO> hashCacheObj = (HashMap<String, BilltypeVO>) getVersionSensitiveCache()
				.get(STR_BILLTYPEINFO);
		if (hashCacheObj == null || !hashCacheObj.containsKey(cacheKey.getKey()) || 
				!hashCacheObj.containsKey(cacheKey.getBilltype())) {
			return;
		}
		hashCacheObj.remove(cacheKey.getKey());
		hashCacheObj.remove(cacheKey.getBilltype());
	}
	
	/**
	 * 从缓存中 获取某个单据类型VO
	 * BillTypeCacheKey中必定包含一个单据类型code(可能为单据类型也可能为交易类型)
	 * 可能包含一个pk_group，也可能不包含
	 * 
	 * 流程平台目前对于单据类型和交易类型的策略：
	 * 单据类型没有集团属性，其pk_group字段一律为空或global00000000000000
	 * 交易类型要有集团属性，pk_group为所部属集团
	 * 
	 * 
	 * 此方法中先使用pk_billtypecode+pk_group作为key查找
	 * 若找不到，那么转向setSingleBilltypeVOs方法
	 * （该方法中查找所有符合该pk_billtypecode的BilltypeVO，
	 * 对于单据类型，使用pk_billtypecode为key放入map
	 * 对于交易类型，使用pk_billtypecode+pk_group为key放入map）
	 * 
	 * 然后再根据pk_billtypecode+pk_group为key从map中取
	 * 若取不到，再根据pk_billtypecode从map中取
	 * 
	 * 
	 * @param billType
	 *            单据类型PK
	 */
	public static BilltypeVO getBillTypeInfo(BillTypeCacheKey cacheKey) {
		if (cacheKey == null
				|| StringUtil.isEmptyWithTrim(cacheKey.getBilltype()))
			return new BilltypeVO();
		HashMap<String, BilltypeVO> hashCacheObj = (HashMap<String, BilltypeVO>) getVersionSensitiveCache()
				.get(STR_BILLTYPEINFO);
		try {
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setBilltypeVOs(hashCacheObj);
				// 缓存所有单据类型VO
				getVersionSensitiveCache().put(STR_BILLTYPEINFO, hashCacheObj);
			} else if (!hashCacheObj.containsKey(cacheKey.getKey())
					&& !hashCacheObj.containsKey(cacheKey.getBilltype())) {
				// 如果找不到，则再次获取缓存（即更新之）
				setSingleBilltypeVOs(hashCacheObj, cacheKey.getKey(),
						cacheKey.getBilltype());
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

		BilltypeVO btVO = hashCacheObj.get(cacheKey.getKey());
		if (btVO == null)
			btVO = hashCacheObj.get(cacheKey.getBilltype());

		if (btVO != null && btVO.getIstransaction() != null
				&& btVO.getIstransaction().booleanValue()) {
			// XXX:如果为交易类型，还需把其所属单据类型的其他属性复制过来 leijun+2008-10
			String strBt = btVO.getParentbilltype();
			if (!StringUtil.isEmptyWithTrim(strBt)) {
				BilltypeVO pbt = getBillTypeInfo(new BillTypeCacheKey()
						.buildBilltype(strBt));
				if (pbt != null
						&& !StringUtil
								.isEmptyWithTrim(pbt.getPk_billtypecode()))
					copySomeFieldsToTranstype(btVO, pbt);
			}
		}
		return btVO;
	}

	/**
	 * 拷贝单据类型的一些属性到交易类型中
	 * 
	 * @param transtypeVO
	 *            交易类型VO
	 * @param pbt
	 *            单据类型VO
	 */
	private static void copySomeFieldsToTranstype(BilltypeVO transtypeVO,
			BilltypeVO pbt) {
		if (StringUtil.isEmptyWithTrim(transtypeVO.getAccountclass()))
			transtypeVO.setAccountclass(pbt.getAccountclass());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getCheckclassname()))
			transtypeVO.setCheckclassname(pbt.getCheckclassname());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getDatafinderclz()))
			transtypeVO.setDatafinderclz(pbt.getDatafinderclz());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getComponent()))
			transtypeVO.setComponent(pbt.getComponent());

		if (StringUtil.isEmptyWithTrim(transtypeVO.getDef1()))
			transtypeVO.setDef1(pbt.getDef1());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getDef2()))
			transtypeVO.setDef2(pbt.getDef2());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getDef3()))
			transtypeVO.setDef3(pbt.getDef3());

		if (StringUtil.isEmptyWithTrim(transtypeVO.getForwardbilltype()))
			transtypeVO.setForwardbilltype(pbt.getForwardbilltype());

		transtypeVO.setIsaccount(pbt.getIsaccount());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getReferclassname()))
			transtypeVO.setReferclassname(pbt.getReferclassname());
		if (StringUtil.isEmptyWithTrim(transtypeVO.getWherestring()))
			transtypeVO.setWherestring(pbt.getWherestring());
	}

	/**
	 * 从缓存中获取单据类型VO <li>为了兼容而保留该方法
	 * 
	 * @param billType
	 *            单据类型PK
	 */
	public static BilltypeVO getBillType(BillTypeCacheKey billType) {
		return getBillTypeInfo(billType);
	}

	/**
	 * 缓存所有的单据类型
	 * 
	 * @return
	 */
	public static HashMap<String, BilltypeVO> getBilltypes() {
		HashMap<String, BilltypeVO> hashCacheObj = (HashMap) getVersionSensitiveCache()
				.get(STR_BILLTYPEINFO);
		try {
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				setBilltypeVOs(hashCacheObj);
				// 缓存所有单据类型VO
				getVersionSensitiveCache().put(STR_BILLTYPEINFO, hashCacheObj);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return hashCacheObj;
	}
	
	/**
	 * 更新
	 * */
	private static void setSingleBillItfVOs(HashMap<String, BillItfDefVO[]> map,BillItfDefVO defVO) throws BusinessException{
		BillItfDefVO[] itfvos =NCLocator.getInstance().lookup(IPFBillItfDef.class).getBillItfDef(defVO);
		//wuxiaoliang 环境数据问题。把计划订单到采购订单的接口关系定义删掉了，但是平台也最好改一下上面的方法，否则会抛出空指针，用户看不明白
		if(itfvos!=null&&itfvos.length!=0)
		   map.put(fetchBillitfMapKey(defVO), itfvos);
	}

	private static void setSingleBilltypeVOs(
			HashMap<String, BilltypeVO> hasBillTypeInfo, String cacheKey,
			String billtype) throws BusinessException {
		
		String sql = "select " + BillTypeVOListProcessor.getFieldString().toLowerCase()
				+ " from bd_billtype where pk_billtypecode=? and pk_group<>?";
	
		SQLParameter param = new SQLParameter();
		param.addParam(billtype);
		param.addParam(PRESET_TRANSTYPE_GROUPID);
		
		Collection<BilltypeVO> coBilltypes = (Collection<BilltypeVO>) DBCacheQueryFacade.runQuery(sql, param,
				new BillTypeVOListProcessor());
			
		if (coBilltypes == null || coBilltypes.size() == 0) {
			hasBillTypeInfo.put(cacheKey, null);
		} else {
			for (Iterator<BilltypeVO> iter = coBilltypes.iterator(); iter
					.hasNext();) {
				BilltypeVO bt = iter.next();
				
				
				// 若为交易类型，那么使用pk_billtypecode+pk_group作为key放入map
				if (bt != null && bt.getIstransaction() != null && bt.getIstransaction().booleanValue()) {
					hasBillTypeInfo.put(
							new BillTypeCacheKey()
									.buildBilltype(bt.getPk_billtypecode())
									.buildPkGroup(bt.getPk_group()).getKey(), bt);
				} else {

					// 若为单据类型，那么使用pk_billtypecode作为key放入map
					hasBillTypeInfo.put(bt.getPk_billtypecode(), bt);
				}
			}
		}

	}
	
	/**
	 * 缓存所有的单据接口定义
	 * */
	private static void setBillItfVOS(HashMap<String, BillItfDefVO[]> map) throws BusinessException{
		Collection<BillItfDefVO> coBillitf;
		String pk_group =InvocationInfoProxy.getInstance().getGroupId();
		if(RuntimeEnv.getInstance().isRunningInServer()){
			coBillitf =NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(BillItfDefVO.class, "pk_group='"+pk_group+"'");
		}else{
			SQLParameter param = new SQLParameter();
			param.addParam(pk_group);
			String sql ="select * from pub_billitfdef where pk_group=?";
			coBillitf =(List<BillItfDefVO>) DBCacheQueryFacade.runQuery(sql,
					param, new BeanListProcessor(BillItfDefVO.class));
		}
		HashMap<String,ArrayList<BillItfDefVO>> temp =new HashMap<String,ArrayList<BillItfDefVO>>();
		for (Iterator<BillItfDefVO> iter = coBillitf.iterator(); iter.hasNext();){
			BillItfDefVO vo =iter.next();
			String key =fetchBillitfMapKey(vo);
			if(temp.get(key)==null){
				ArrayList<BillItfDefVO> defvos =new ArrayList<BillItfDefVO>();
				temp.put(key, defvos);
			}
			temp.get(key).add(vo);
		}	
		Set<String> keys =temp.keySet();
		
		for(String key :keys){
			map.put(key, temp.get(key).toArray(new BillItfDefVO[0]));
		}
	}
	

	/**
	 * 获取所有的单据类型VO，并以PK-Object方式缓存
	 */
	private static void setBilltypeVOs(
			HashMap<String, BilltypeVO> hasBillTypeInfo)
			throws BusinessException {
		Collection<BilltypeVO> coBilltypes;
		if (RuntimeEnv.getInstance().isRunningInServer())
			coBilltypes = NCLocator.getInstance().lookup(IUAPQueryBS.class)
					.retrieveByClause(BilltypeVO.class, " pk_group != '" + PRESET_TRANSTYPE_GROUPID + "'");
		else {
			// 使用很频繁，需要缓存
			SQLParameter param = new SQLParameter();
			param.addParam(PRESET_TRANSTYPE_GROUPID);
			// String fieldList =
			// "ACCOUNTCLASS,BILLSTYLE,BILLTYPENAME,BILLTYPENAME2,BILLTYPENAME3,BILLTYPENAME4,BILLTYPENAME5,BILLTYPENAME6,CANEXTENDTRANSACTION,"
			// +
			// "CHECKCLASSNAME,CLASSNAME,COMPONENT,DATAFINDERCLZ,DEF1,DEF2,DEF3,DR,FORWARDBILLTYPE,ISACCOUNT,ISAPPROVEBILL,ISBIZFLOWBILL,ISLOCK,ISROOT,"
			// +
			// "ISTRANSACTION,NODECODE,PARENTBILLTYPE,PK_BILLTYPECODE,PK_BILLTYPEID,PK_GROUP,PK_ORG,REFERCLASSNAME,SYSTEMCODE,TRANSTYPE_CLASS,TS,WEBNODECODE,WHERESTRING ";

			String fieldList = BillTypeVOListProcessor.getFieldString()
					.toLowerCase();
			String sql = "select " + fieldList
					+ " from bd_billtype where pk_group != ?";
			// coBilltypes = (List)DBCacheQueryFacade.runQuery(sql, param, new
			// BeanListProcessor(BilltypeVO.class));
			coBilltypes = (List<BilltypeVO>) DBCacheQueryFacade.runQuery(sql,
					param, new BillTypeVOListProcessor());
		}
		for (Iterator<BilltypeVO> iter = coBilltypes.iterator(); iter.hasNext();) {
			BilltypeVO billtype = iter.next();
			if (UFBoolean.TRUE.equals(billtype.getIstransaction()))
				hasBillTypeInfo.put(
						new BillTypeCacheKey()
								.buildBilltype(billtype.getPk_billtypecode())
								.buildPkGroup(billtype.getPk_group()).getKey(),
						billtype);
			else
				hasBillTypeInfo.put(
						new BillTypeCacheKey().buildBilltype(
								billtype.getPk_billtypecode()).getKey(),
						billtype);
		}
	}

	/**
	 * 获取单据类型和该集团下的交易类型
	 * 
	 * @return
	 */
	public static List<BilltypeVO> getBillTypeAndTranstypesInGrp(String pk_group) {
		HashMap<String, BilltypeVO> hashCacheObj = getBilltypes();
		if (hashCacheObj == null)
			return null;
		ArrayList<BilltypeVO> voarrs = new ArrayList<BilltypeVO>(
				hashCacheObj.values());
		if (voarrs == null || voarrs.size() == 0)
			return null;
		ArrayList<BilltypeVO> res = new ArrayList<BilltypeVO>();
		for (BilltypeVO vo : voarrs) {

			/*
			 * @modifier yanke1 2011-3-23 添加了判断vo是否为空的语句
			 */
			if (vo == null)
				continue;
			// 单据类型和该集团下的交易类型
			if ((UFBoolean.TRUE.equals(vo.getIstransaction()) && pk_group
					.equals(vo.getPk_group()))
					|| (vo.getIstransaction() == null || !vo.getIstransaction()
							.booleanValue()))
				res.add(vo);
		}
		return res;
	}

	/**
	 * 从对象缓存中获取某单据类型的所有单据动作
	 * 
	 * @param billtype
	 *            单据类型PK
	 * @return
	 */
	public static ArrayList<BillactionVO> getBillactionVOs(String billtype) {
		HashMap<String, ArrayList<BillactionVO>> hashCacheObj = null;
		hashCacheObj = (HashMap<String, ArrayList<BillactionVO>>) getVersionSensitiveCache()
				.get(STR_BILLACTION);

		if (hashCacheObj == null) {
			hashCacheObj = new HashMap<String, ArrayList<BillactionVO>>();
			getVersionSensitiveCache().put(STR_BILLACTION, hashCacheObj);
		}
		if (!hashCacheObj.containsKey(billtype)) {
			try {
				setBillactionVOs(hashCacheObj, billtype);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return hashCacheObj.get(billtype);
	}

	public static Map<String, BillactionVO> getBillactionVOMap(String billtype) {
		ArrayList<BillactionVO> billActionList = getBillactionVOs(billtype);
		if (billActionList != null && billActionList.size() > 0) {
			Map<String, BillactionVO> actionMap = new HashMap<String, BillactionVO>();
			for (BillactionVO billactionVO : billActionList) {
				actionMap.put(billactionVO.getActiontype(), billactionVO);
			}
			return actionMap;
		} else if (PfUtilBaseTools.isTranstype(billtype)) {
			String realBilltype = PfUtilBaseTools.getRealBilltype(billtype);
			return getBillactionVOMap(realBilltype);
		} else
			return null;
	}

	private static void setBillactionVOs(HashMap hasBillaction, String billtype)
			throws BusinessException {
		BilltypeVO vo = PfDataCache.getBillType(billtype);
		if(vo == null)
		{
			return;
		}
		String pk_billtypeid = vo.getPk_billtypeid();
		Collection<BillactionVO> coRet;
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			IUAPQueryBS uapQry = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			BillactionVO condVO = new BillactionVO();
			condVO.setPk_billtypeid(pk_billtypeid);
			coRet = uapQry.retrieve(condVO, true);
			if (UFBoolean.TRUE.equals(vo.getIstransaction())
					&& (coRet == null || coRet.size() == 0)) {
				BillactionVO condVO2 = new BillactionVO();
				condVO2.setPk_billtype(vo.getParentbilltype());
				coRet = uapQry.retrieve(condVO2, true);
			}
		} else {
			SQLParameter param = new SQLParameter();
			param.addParam(pk_billtypeid);
			coRet = (List) DBCacheQueryFacade.runQuery(
					"select * from pub_billaction where pk_billtypeid=?",
					param, new BeanListProcessor(BillactionVO.class));
			if (UFBoolean.TRUE.equals(vo.getIstransaction())
					&& (coRet == null || coRet.size() == 0)) {
				param.clearParams();
				param.addParam(vo.getParentbilltype());
				coRet = (List) DBCacheQueryFacade.runQuery(
						"select * from pub_billaction where pk_billtype=?",
						param, new BeanListProcessor(BillactionVO.class));
			}
		}
		hasBillaction.put(billtype, coRet);
	}

	private static void setFunctionVOs(
			HashMap<String, List<FunctionVO>> hmBillToFunction, String billtype) {
		try {
			FunctionVO condVO = new FunctionVO();
			condVO.setPk_billtype(billtype);
			Collection<FunctionVO> coRet = NCLocator.getInstance()
					.lookup(IUAPQueryBS.class).retrieve(condVO, true);
			ArrayList<FunctionVO> aryList = new ArrayList<FunctionVO>();
			for (Iterator<FunctionVO> iterator = coRet.iterator(); iterator
					.hasNext();) {
				FunctionVO functionVO = iterator.next();
				aryList.add(functionVO);
			}
			hmBillToFunction.put(billtype, aryList);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 从缓存中获取所有动态组织注册信息
	 * 
	 * @return
	 */
	public static LinkedHashMap getDynamicOrgReg() {
		LinkedHashMap hmCacheObj = null;
		try {
			hmCacheObj = (LinkedHashMap) getVersionSensitiveCache().get(
					DYNAMIC_ORG_REGINFO);
			if (hmCacheObj == null) {
				hmCacheObj = new LinkedHashMap();
				IUAPQueryBS uapBS = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				// 为前台参照返回，需要全部的组织，组织数据量不大，并且作为树的根节点，无法做懒加载，最好考虑为全局缓存，减少各部分自己缓存导致的浪费
				Collection<DynamicOrgRegInfo> co = uapBS
						.retrieveAll(DynamicOrgRegInfo.class);
				for (Iterator<DynamicOrgRegInfo> iter = co.iterator(); iter
						.hasNext();) {
					DynamicOrgRegInfo reg = iter.next();
					DynamicOrganizeUnitRegistry.register(reg.getOrgclass(),
							reg.getBuilderclass(), reg.getResolverclass(),
							hmCacheObj);
				}

				// 缓存
				getVersionSensitiveCache().put(DYNAMIC_ORG_REGINFO, hmCacheObj);
			}
		} catch (Exception ex) {
			Logger.warn("注册动态组织时出现错误，将无法使用动态组织=" + ex.getMessage(), ex);
		}
		return hmCacheObj;
	}

	/**
	 * 从缓存中 获取单据大类所对应的单据类型PK
	 */
	public static String getBillTypeByStyle(String billStyle) {
		HashMap<String, String> hashCacheObj = null;
		try {
			hashCacheObj = (HashMap<String, String>) getVersionSensitiveCache()
					.get(STR_BILLSTYLETOTYPE);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap<String, String>();
				setBilltypeOfStyle(hashCacheObj);
				// 缓存
				getVersionSensitiveCache().put(STR_BILLSTYLETOTYPE,
						hashCacheObj);
			} else if (!hashCacheObj.containsKey(billStyle)) {
				setBilltypeOfStyle(hashCacheObj);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return hashCacheObj.get(billStyle);
	}

	/**
	 * 获取单据大类与其单据类型的对应关系
	 * 
	 * @param hashCacheObj
	 */
	private static void setBilltypeOfStyle(HashMap<String, String> hashCacheObj) {
		HashMap<String, BilltypeVO> hashPkToBilltype = getBilltypes();
		Collection<BilltypeVO> coBilltypes = hashPkToBilltype.values();

		for (Iterator<BilltypeVO> iter = coBilltypes.iterator(); iter.hasNext();) {
			BilltypeVO billtype = iter.next();
			if (billtype.getIsroot() != null
					&& billtype.getIsroot().booleanValue()
					&& billtype.getBillstyle() != null) {
				hashCacheObj.put(billtype.getBillstyle().toString(),
						billtype.getPk_billtypecode());
			}
		}
	}

	/**
	 * 缓存所有的模块
	 * 
	 * @return
	 */
	public static ModuleVO getModuleVO(String systypeCode) {
		HashMap<String, ModuleVO> hashCacheObj = null;
		try {
			hashCacheObj = (HashMap<String, ModuleVO>) getVersionSensitiveCache()
					.get(STR_SYSTEMTYPEINFO);
			if (hashCacheObj == null) {
				getAllModules();
				hashCacheObj = (HashMap<String, ModuleVO>) getVersionSensitiveCache()
						.get(STR_SYSTEMTYPEINFO);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return hashCacheObj.get(systypeCode);
	}

	// public static ModuleVO getSystemtypeVOByModule(String moduleId) {
	// HashMap hashCacheObjWithModule = null;
	// try {
	// hashCacheObjWithModule = (HashMap)
	// getVersionSensitiveCache().get(STR_SYSTEMTYPEINFO_MODULE);
	// if (hashCacheObjWithModule == null) {
	// getAllModules();
	// hashCacheObjWithModule = (HashMap)
	// getVersionSensitiveCache().get(STR_SYSTEMTYPEINFO_MODULE);
	// }
	// } catch (Exception ex) {
	// Logger.error(ex.getMessage(), ex);
	// }
	// return (DapsystemVO) hashCacheObjWithModule.get(moduleId);
	// }

	public static ModuleVO[] getAllModules() {
		HashMap<String, ModuleVO> hashCacheObj = null;
		try {
			hashCacheObj = (HashMap<String, ModuleVO>) getVersionSensitiveCache()
					.get(STR_SYSTEMTYPEINFO);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap<String, ModuleVO>();
				getVersionSensitiveCache()
						.put(STR_SYSTEMTYPEINFO, hashCacheObj);
			}
			ModuleVO[] moduleVOs = NCLocator.getInstance()
					.lookup(IModuleQueryService.class).getAllModules();
			if (moduleVOs == null)
				return null;
			for (ModuleVO moduleVO : moduleVOs) {
				hashCacheObj.put(moduleVO.getSystypecode(), moduleVO);
			}
			return moduleVOs;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return null;
	}
	
	public static BillItfDefVO[] getBillItfDef(BillItfDefVO defVO) throws BusinessException{
		HashMap<String, BillItfDefVO[]> hashCacheObj = null;
		//
		hashCacheObj = (HashMap<String, BillItfDefVO[]>) getVersionSensitiveCache().get(STR_BILLITFDEF);
		String key =fetchBillitfMapKey(defVO);
		if(hashCacheObj==null){
			hashCacheObj =new HashMap<String, BillItfDefVO[]>();
			//缓存所有的单据接口定义
			setBillItfVOS(hashCacheObj);	
			getVersionSensitiveCache().put(STR_BILLITFDEF, hashCacheObj);
		}
		
		if(!hashCacheObj.containsKey(key)){
			//没有找到，则再次更新之
			setSingleBillItfVOs(hashCacheObj, defVO);
		}
		
		return hashCacheObj.get(key);
	}
	
	/**
	 * 
	 * */
	private static String fetchBillitfMapKey(BillItfDefVO defVO){
		String key="";
		String split="$$";
		if(!StringUtil.isEmptyWithTrim(defVO.getSrc_billtype())){
			key+=defVO.getSrc_billtype()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getSrc_transtype())){
			key+=defVO.getSrc_transtype()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getDest_billtype())){
			key+=defVO.getDest_billtype()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getDest_transtype())){
			key+=defVO.getDest_transtype()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getPk_group())){
			key+=defVO.getPk_group()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getPk_org())){
			key+=defVO.getPk_org()+split;
		}
		if(!StringUtil.isEmptyWithTrim(defVO.getPk_busitype())){
			key+=defVO.getPk_busitype()+split;
		}	
		return key;
	}
	
	public static BillItfDefVO[] getBillItfDef(String srcBilltype, String srcTranstype, String destBilltype, String destTranstype, String pk_group) throws BusinessException{
		BillItfDefVO itfvo =new BillItfDefVO();
		itfvo.setSrc_billtype(srcBilltype);
		itfvo.setSrc_transtype(srcTranstype);
		itfvo.setDest_billtype(destBilltype);
		itfvo.setDest_transtype(destTranstype);
		itfvo.setPk_group(pk_group);
		return getBillItfDef(itfvo);
	}
}