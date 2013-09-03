package uap.workflow.monitor;

import java.io.Serializable;
import java.util.ArrayList;

import uap.workflow.engine.core.IActivityInstance;

/**
 * ����ʵ����Ϣ����������������ʱ�������̶���ͼ
 * 
 * @see EngineService.queryProcessRouteRecursively()
 * @modifier yanke1 2011-9-22 ���������̶���汾��Ϣ
 */
public class ProcessRouteRes implements Serializable {
	/** ����ʵ��PK */
	private String procInstancePK = null;

	/** ĳ���̶���PK */
	private String processDefPK = null;

	/** ĳ���̶����XPDL���� */
	private Object xpdlString = null;

	/** ĳ���̶���İ汾�� */
	private String processDefVersion = null;

	/**
	 * ����״̬
	 * 
	 */
	private int procStatus;

	/**
	 * ���������д������Ļʵ��
	 */
	private IActivityInstance[] activityInstance = null;

	private ProcessRouteRes[] subProcessRoute = null;

	/**
	 * ����ʵ�������л����Դ��ϵ
	 */
	private ArrayList<String[]> activityRelations = null;

	public ArrayList<String[]> getActivityRelations() {
		return activityRelations;
	}

	public void setActivityRelations(ArrayList<String[]> activityRelations) {
		this.activityRelations = activityRelations;
	}

	public int getProcStatus() {
		return procStatus;
	}

	public void setProcStatus(int procStatus) {
		this.procStatus = procStatus;
	}

	public String getProcInstancePK() {
		return procInstancePK;
	}

	public void setProcInstancePK(String procInstancePK) {
		this.procInstancePK = procInstancePK;
	}

	public String getProcessDefPK() {
		return processDefPK;
	}

	public void setProcessDefPK(String processDefPK) {
		this.processDefPK = processDefPK;
	}

	public Object getXpdlString() {
		return xpdlString;
	}

	public void setXpdlString(Object xpdlString) {
		this.xpdlString = xpdlString;
	}

	public IActivityInstance[] getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(IActivityInstance[] activityInstance) {
		this.activityInstance = activityInstance;
	}

	public ProcessRouteRes[] getSubProcessRoute() {
		return subProcessRoute;
	}

	public void setSubProcessRoute(ProcessRouteRes[] subProcessRoute) {
		this.subProcessRoute = subProcessRoute;
	}

	public String getProcessDefVersion() {
		return processDefVersion;
	}

	public void setProcessDefVersion(String processDefVersion) {
		this.processDefVersion = processDefVersion;
	}

}
