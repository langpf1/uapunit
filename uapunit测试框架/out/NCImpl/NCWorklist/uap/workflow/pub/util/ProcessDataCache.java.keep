package uap.workflow.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.cache.ext.CacheToMapAdapter;
import nc.vo.cache.ext.ElementVersionSensitiveMap;
import nc.vo.cache.ext.ICacheVersionMonitor;
import nc.vo.cache.ext.ObjectCacheVersionMonitor;
import nc.vo.cache.ext.VersionMonitorFactory;
import nc.vo.pf.pub.IPfPubConst;
import nc.vo.pub.BusinessException;
import nc.vo.wfengine.ext.ApproveFlowAdjustVO;
import uap.workflow.admin.IWFDefineService;
import uap.workflow.engine.core.IProcessDefinition;

/**
 * 平台缓存工具类 <li>前、后台均可用
 * 
 * @author fangj 2003-3
 * @modifier leijun 2005-10-7 使用NC50缓存机制，区分不同数据源
 * @modifier leijun 2006-3-30 使用版本敏感的缓存
 * @modifier leijun 2009-1 增加流程定义对象的缓存
 */
public class ProcessDataCache {
	
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
	 * 获取缓存中的流程定义对象
	 * 
	 * @param defPK
	 *            流程定义PK
	 * @return
	 * @throws BusinessException
	 * @throws XPDLParserException
	 * @since 5.5
	 */
	public static IProcessDefinition getWorkflowProcess(String defPK)
			throws BusinessException {
		HashMap<String, IProcessDefinition> hashCacheObj = (HashMap<String, IProcessDefinition>) getVersionSensitiveCache()
				.get(STR_WORKFLOWPROCESS);
		if (hashCacheObj == null) {
			// hashCacheObj = new HashMap<String, BasicWorkflowProcess>();
			hashCacheObj = queryAndCacheProcess(defPK, null);
			// 缓存之
			getVersionSensitiveCache().put(STR_WORKFLOWPROCESS, hashCacheObj);
		} else if (!hashCacheObj.containsKey(defPK)) {
			// 如果找不到，则再次获取缓存（即更新之）
			hashCacheObj = queryAndCacheProcess(defPK, null);
		}

		return hashCacheObj.get(defPK);
	}

	public static void synchronizeWorkflowProcess(String processInstancePK,
			IProcessDefinition wp) {
		if (processInstancePK == null) {
			return;
		} else if (wp == null) {
			HashMap<String, IProcessDefinition> hashCacheObj = (HashMap) getVersionSensitiveCache()
					.get(STR_WORKFLOWPROCESS);
			if (hashCacheObj != null)
				hashCacheObj.remove(processInstancePK);
		} else {
			HashMap<String, IProcessDefinition> hashCacheObj = (HashMap) getVersionSensitiveCache()
					.get(STR_WORKFLOWPROCESS);
			if (hashCacheObj == null) {
				hashCacheObj = new HashMap();
				// 缓存之
				getVersionSensitiveCache().put(STR_WORKFLOWPROCESS,
						hashCacheObj);
			}
			hashCacheObj.put(processInstancePK, wp);
		}
	}

	/**
	 * 查询出流程定义对象，并缓存
	 * 
	 * @param hashCacheObj
	 * @param defPK
	 * @throws BusinessException
	 * @throws XPDLParserException
	 */
	private static HashMap<String, IProcessDefinition> queryAndCacheProcess(
			String defPK, String procInstPk) throws BusinessException{
		// 获取当前流程实例PK
		IWFDefineService defQry = NCLocator.getInstance().lookup(IWFDefineService.class);
		HashMap<String, IProcessDefinition> hashCacheObj = defQry
				.findDefinitionByDefPkAndInstPk(defPK, procInstPk);
		return hashCacheObj;
	}

	public static IProcessDefinition getWorkflowProcess(String defPK,
			String processInstPK) throws BusinessException {
		HashMap<String, IProcessDefinition> hashCacheObj = null;

		hashCacheObj = (HashMap<String, IProcessDefinition>) getVersionSensitiveCache()
				.get(STR_WORKFLOWPROCESS);

		if (hashCacheObj == null) {
			// hashCacheObj = new HashMap<String, BasicWorkflowProcess>();
			hashCacheObj = queryAndCacheProcess(defPK, processInstPK);
			// 缓存之
			getVersionSensitiveCache().put(STR_WORKFLOWPROCESS, hashCacheObj);
		} else if (!hashCacheObj.containsKey(defPK)) {
			// 如果找不到，则再次获取缓存（即更新之）
			hashCacheObj = queryAndCacheProcess(defPK, processInstPK);
		} else if (processInstPK != null
				&& !hashCacheObj.containsKey(processInstPK)) {
			// 再试图查一次加签表
			queryAndCacheProcessInAdjust(hashCacheObj, defPK, processInstPK);
		}
		if (hashCacheObj.containsKey(processInstPK))
			return hashCacheObj.get(processInstPK);
		else
			return hashCacheObj.get(defPK);
	}

	private static void queryAndCacheProcessInAdjust(
			HashMap<String, IProcessDefinition> hashCacheObj, String defPK,
			String processInstPK) throws BusinessException {
		IProcessDefinition bwp = null;
		if (processInstPK != null) {
			String sql1 = "select content from pub_wf_def_adjust where pk_wf_instance=?";
			SQLParameter para1 = new SQLParameter();
			para1.addParam(processInstPK);
			ArrayList<ApproveFlowAdjustVO> adjustVOs = (ArrayList<ApproveFlowAdjustVO>) NCLocator
					.getInstance()
					.lookup(IUAPQueryBS.class)
					.executeQuery(sql1, para1,
							new BeanListProcessor(ApproveFlowAdjustVO.class));
			if (adjustVOs != null && adjustVOs.size() > 0) {
				ApproveFlowAdjustVO adjustVO = adjustVOs.get(0);
				/* todo:
				bwp = UfXPDLParser.getInstance().parseProcess(
						adjustVO.getContent());
						*/
				bwp.setProDefPk(defPK);
				hashCacheObj.put(processInstPK, bwp);
			}
		}
	}

}