package uap.workflow.engine.service;
import nc.bs.framework.common.NCLocator;

import uap.workflow.app.participant.IParticipantService;
import uap.workflow.app.taskhandling.ITaskHandlingService;
import uap.workflow.engine.itf.IAssignmentBill;
import uap.workflow.engine.itf.IAssignmentQry;
import uap.workflow.engine.itf.IBeforeAddSignBill;
import uap.workflow.engine.itf.IBeforeAddSignQry;
import uap.workflow.engine.itf.IEventSubscriptionBill;
import uap.workflow.engine.itf.IEventSubscriptionQry;
import uap.workflow.engine.itf.IJobBill;
import uap.workflow.engine.itf.IJobQry;
import uap.workflow.engine.itf.IProcessDefGroupQry;
import uap.workflow.engine.itf.IVariableInstanceBill;
import uap.workflow.engine.itf.IVariableInstanceQry;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
public class WfmServiceFacility {
	public static TaskService getTaskService() {
		return BizProcessServer.getProcessEngine().getTaskService();
	}
	public static FormService getFormService() {
		return BizProcessServer.getProcessEngine().getFormService();
	}
	public static RuntimeService getRunTimeServie() {
		return BizProcessServer.getProcessEngine().getRuntimeService();
	}
	public static RepositoryService getRepositoryService() {
		return BizProcessServer.getProcessEngine().getRepositoryService();
	}
	public static HistoryService getHistoryService() {
		return BizProcessServer.getProcessEngine().getHistoryService();
	}
	public static ManagementService getManagementService() {
		return BizProcessServer.getProcessEngine().getManagementService();
	}
	public static IAssignmentQry getAssignmentQry() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IAssignmentQry.class);
	}
	public static IAssignmentBill getAssignmentBill() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IAssignmentBill.class);
	}
	public static IVariableInstanceQry getVariableInstanceQry() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IVariableInstanceQry.class);
	}
	public static IVariableInstanceBill getVariableInstanceBill() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IVariableInstanceBill.class);
	}
	public static IParticipantService getIParticipantService() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IParticipantService.class);
	}
	public static ITaskHandlingService getTaskHandlingService() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskHandlingService.class);
	}
	public static IBeforeAddSignQry getBeforeAddSignQry() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IBeforeAddSignQry.class);
	}
	public static IBeforeAddSignBill getBeforeAddSignBill() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IBeforeAddSignBill.class);
	}
	public static IEventSubscriptionBill getEventSubscriptionBill() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IEventSubscriptionBill.class);
	}
	public static IEventSubscriptionQry getEventSubscriptionQry() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IEventSubscriptionQry.class);
	}
	public static IJobBill getJobBill() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IJobBill.class);
	}
	public static IJobQry getJobQry() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IJobQry.class);
	}
	public static IProcessDefGroupQry getProcessDefGroup() {
		return NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefGroupQry.class);
	}
}
