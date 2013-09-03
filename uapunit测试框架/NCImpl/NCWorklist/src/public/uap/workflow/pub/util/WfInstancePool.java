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
 * ʵ���ء�
 * 
 * @author wzhy 2005-1-15
 * @modifier leijun 2009-7
 */
public class WfInstancePool {

	private static WfInstancePool instance;

	// ����ʵ����
	private Hashtable<String, IProcessInstance> htProcessInstances = new Hashtable();

	// �ʵ����
	private Hashtable<String, IActivityInstance> htActivityInstances = new Hashtable();

	private WfInstancePool() {
	}

	public synchronized static WfInstancePool getInstance() {
		if (instance == null)
			instance = new WfInstancePool();
		return instance;
	}

	/**
	 * ��������ʵ��PK����ʵ�����л�ȡ����ʵ�������ʵ������û�У�������ݿ��
	 * @param procInstPK
	 * @return
	 */
	public IProcessInstance getProcessInstance(String procInstPK) {
		IProcessInstance instance = (IProcessInstance) htProcessInstances.get(procInstPK);
		if (instance == null) {
			// ʵ������û�л��Ѿ�ʧЧ�������²�ѯ������ʵ��
			instance = ProcessInstanceUtil.getProcessInstance(procInstPK);
			if (instance != null) {
				// �������ʵ���Ķ������
				// xry TODO:
				/*
				 * instance.setWfProcessDef(new
				 * EngineService().queryWfProcess(instance.getWfProcessDefPK(),
				 * procInstPK));
				 * 
				 * //Ϊ��ʵ���еĻʵ���������� Vector activityInstances =
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
	 * ��������ʵ��PK����ʵ�����л�ȡ����ʵ�������ʵ������û�У�������ݿ��
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
	 * ��ѯ������
	 * ���Ҫ���ڴ�����㷨��Ҫ�Ľ�,���߸������ݽṹ(����ֻ���ӵ�����ֱ������,�����Ƿ���븸���ӵ�ֱ������)
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
																	 * ��ѯ������ʵ���������ݿ��쳣!
																	 * /));
		}
	   */
		
		 return getProcessInstance(parentProcessInstancePK);

	}

}