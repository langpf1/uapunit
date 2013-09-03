package uap.workflow.monitor;

import java.io.Serializable;
import java.util.ArrayList;

import uap.workflow.engine.core.IActivityInstance;

/**
 * 流程实例信息，用于联查审批流时构造流程定义图
 * 
 * @see EngineService.queryProcessRouteRecursively()
 * @modifier yanke1 2011-9-22 加入了流程定义版本信息
 */
public class ProcessRouteRes implements Serializable {
	/** 流程实例PK */
	private String procInstancePK = null;

	/** 某流程定义PK */
	private String processDefPK = null;

	/** 某流程定义的XPDL描述 */
	private Object xpdlString = null;

	/** 某流程定义的版本号 */
	private String processDefVersion = null;

	/**
	 * 流程状态
	 * 
	 */
	private int procStatus;

	/**
	 * 流程中所有触发过的活动实例
	 */
	private IActivityInstance[] activityInstance = null;

	private ProcessRouteRes[] subProcessRoute = null;

	/**
	 * 流程实例中所有活动的来源关系
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
