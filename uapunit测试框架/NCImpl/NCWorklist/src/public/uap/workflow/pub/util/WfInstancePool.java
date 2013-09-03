package uap.workflow.pub.util;

import java.util.Hashtable;
import java.util.Vector;

import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.utils.ActivityInstanceUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;

import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uap.sf.excp.SystemFrameworkException;
import nc.jdbc.framework.exception.DbException;
import nc.vo.wfengine.engine.exception.EngineException;

/**
 * 实例池。
 * 
 * @author wzhy 2005-1-15
 * @modifier leijun 2009-7
 */
public class WfInstancePool {

	private static WfInstancePool instance;

	// 流程实例池
	private Hashtable<String, IProcessInstance> htProcessInstances = new Hashtable();

	// 活动实例池
	private Hashtable<String, IActivityInstance> htActivityInstances = new Hashtable();

	private WfInstancePool() {
	}

	public synchronized static WfInstancePool getInstance() {
		if (instance == null)
			instance = new WfInstancePool();
		return instance;
	}

	/**
	 * 根据流程实例PK，从实例池中获取流程实例；如果实例池中没有，则从数据库查
	 * @param procInstPK
	 * @return
	 */
	public IProcessInstance getProcessInstance(String procInstPK) {
		IProcessInstance instance = (IProcessInstance) htProcessInstances.get(procInstPK);
		if (instance == null) {
			// 实例池中没有或已经失效，则重新查询出流程实例
			instance = ProcessInstanceUtil.getProcessInstance(procInstPK);
			if (instance != null) {
				// 查出流程实例的定义对象
				// xry TODO:
				/*
				 * instance.setWfProcessDef(new
				 * EngineService().queryWfProcess(instance.getWfProcessDefPK(),
				 * procInstPK));
				 * 
				 * //为该实例中的活动实例查出活动对象 Vector activityInstances =
				 * instance.getActInstVector(); ActivityInstance
				 * activityInstance = null; for (int i = 0; i <
				 * activityInstances.size(); i++) { activityInstance =
				 * (ActivityInstance) activityInstances.get(i);
				 * activityInstance.
				 * setActivity(instance.getWfProcessDef().findActivityByID(
				 * activityInstance.getActivityID())); }
				 */
			}
			if (instance != null)
				putProcessInstance(procInstPK, instance);
		}

		return instance;
	}

	/**
	 * 根据流程实例PK，从实例池中获取流程实例；如果实例池中没有，则从数据库查
	 * @param procInstPK
	 * @return
	 */
	public IProcessInstance isExistedProcessInstance(String procInstPK) {
		return getProcessInstance(procInstPK);
	}

	public IActivityInstance getERPActivityInstance(String name) {
		IActivityInstance instance = (IActivityInstance) htActivityInstances.get(name);
		if (instance == null) {
			try {
				instance = ActivityInstanceUtil.getActInsByActInsPk(name);
				/*
				instance.setActivity(new EngineService()
						.queryWfProcess(instance.getWfProcessDefPK(), instance.getWfProcessInstancePK()).findActivityByID(
								instance.getActivityID()));
								*/
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
				throw new EngineException(ex.getMessage(), ex);
			}
			if (instance != null)
				putERPActivityInstance(name, instance);
		}

		return instance;

	}

	public void putERPActivityInstance(String actInstPK, IActivityInstance instance) {
		htActivityInstances.put(actInstPK, instance);
	}

	public void putProcessInstance(String procInstPK, IProcessInstance instance) {
		htProcessInstances.put(procInstPK, instance);
	}

	public void clear() {
		htProcessInstances.clear();
		htActivityInstances.clear();
	}

	/**
	 * 查询子流程
	 * 如果要在内存里查算法需要改进,或者更改数据结构(现在只有子到父的直接引用,考虑是否加入父到子的直接引用)
	 */
	public IProcessInstance findSubrocessInstance(String parentProcessInstancePK,
			String parentActivityInstancePK) {
		/*
		try {
			IProcessInstance subflowInstance = null;
			String subflowPK = new EnginePersistence().findSubrocessInstancePK(parentProcessInstancePK,
					parentActivityInstancePK);
			if (subflowPK != null) {
				subflowInstance = getProcessInstance(subflowPK);
			}
			return subflowInstance;
		} catch (DbException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new EngineException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000907" /*
																	 * 查询子流程实例出现数据库异常!
																	 * /));
		}
	   */
		
		 return getProcessInstance(parentProcessInstancePK);

	}

}