package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



import nc.bs.framework.common.NCLocator;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.cmpltsgy.CompleteSgyManager;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessElement;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.itf.IDeployService;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.vos.ProcessDefinitionVO;
public class ProcessDefinitionUtil {
	public static IProcessDefinition getProDefByProDefPk(String proDefPk) {
		return WfmServiceFacility.getRepositoryService().getProcessDefinitionByPk(proDefPk);
	}

	public static IProcessDefinition getProDefByProDefId(String proDefId) {
		return WfmServiceFacility.getRepositoryService().getProcessDefinitionById(proDefId);
	}
	
	public static List<TaskDefinition> getTaskDefinition(String taskPK, String proDefPK,
			IBusinessKey bizObject, String operator) {
		ITask task =null;
		if(taskPK != null)
		{
			task = TaskUtil.getTaskByTaskPk(taskPK);
		}
		List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
		IProcessDefinition processDef = WfmServiceFacility.getRepositoryService().getProcessDefinitionByPk(proDefPK);
		List<IActivity> nextActivitys = new ArrayList<IActivity>();
		IActivity activity = null;

		if (task == null) {// 流程没有开始，直接获取开始节点的信息
			activity = processDef.getInitial();
			NextUserTaskInfoUtil.getNextUserTaskActivity(null, nextActivitys, null, activity.getOutgoingTransitions().toArray(new IProcessElement[0]));
			/**
			 * 应该判断是否是制单节点，是制单节点的话，要找制单节点的后面节点，因为制单节点是自动推送的。
			 */
			if(nextActivitys.size() == 1){
				IActivity firstActivity = nextActivitys.get(0);
				nextActivitys = new ArrayList<IActivity>();
				NextUserTaskInfoUtil.setCommitProInsCtx(bizObject, operator);
				NextUserTaskInfoUtil.getNextUserTaskActivity(null, nextActivitys, bizObject, firstActivity.getOutgoingTransitions().toArray(new IProcessElement[0]));
			}
		} else {
			activity = task.getExecution().getActivity();
			boolean flag = false;
			TaskEntity taskIns = (TaskEntity) task;
			if (taskIns.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign)) {
				flag = false;
			} else {
				flag = CompleteSgyManager.getInstance().isComplete((ActivityInstanceEntity) task.getExecution(), null);
			}
			if (flag) {// 如果当前活动节点已经完成的话，直接获取下一步信息
				NextUserTaskInfoUtil.setNextTaskInsCtx(bizObject, operator, true);
				NextUserTaskInfoUtil.getNextUserTaskActivity(task, nextActivitys, bizObject, activity.getOutgoingTransitions().toArray(new IProcessElement[0]));
			}
		}
		for (IActivity act : nextActivitys) {
			ActivityBehavior activityBehavior = act.getActivityBehavior();
			if(activityBehavior instanceof UserTaskActivityBehavior)
			{
				taskDefinitions.add(((UserTaskActivityBehavior)activityBehavior).getTaskDefinition());
			}
		}
		return taskDefinitions;
	}
	
	public static IProcessDefinition getProDefByProDefPkAndId(String proDefPk, String proDefId) {
		return null;
	}
	public static IProcessDefinition[] getProcessDefVOAccordingBiz(String pk_group, String bizObjectKey) {
		List<IProcessDefinition>  proDefs = new ArrayList<IProcessDefinition>();
		IProcessDefinitionQry proDefQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO[] proDefVOs = proDefQry.getProcessDefVOAccordingBiz(pk_group, bizObjectKey);
		if (proDefVOs == null || proDefVOs.length == 0) {
			return proDefs.toArray(new IProcessDefinition[0]);
		}
		for (ProcessDefinitionVO proDefVO : proDefVOs) {
			
			proDefs.add(ProcessDefinitionUtil.getProDefByProDefPk(proDefVO.getPk_prodef()));
		}
		return proDefs.toArray(new IProcessDefinition[0]);
	}
	
	public static void setProcessDefinitionStatus(String pk_processDef, ProcessDefinitionStatusEnum status) {
		IDeployService service = NCLocator.getInstance().lookup(IDeployService.class);
		service.setProcessDefinitionStatus(pk_processDef, status);
	}
		
	
	public static Properties getProp() {
		Properties props = new Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:8413/ServiceDispatcherServlet");
		props.setProperty(NCLocator.TARGET_MODULE, "workflow");
		return null;
	}
}
