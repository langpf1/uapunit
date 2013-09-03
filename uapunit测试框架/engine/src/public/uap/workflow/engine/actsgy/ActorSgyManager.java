package uap.workflow.engine.actsgy;
import java.util.List;


import nc.bs.framework.common.InvocationInfoProxy;

import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.cache.WfCacheManager;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.vos.AssignmentVO;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
/**
 * 参与者策略的管理器
 * 
 * @author tianchw
 * 
 */
public class ActorSgyManager implements IActorSgy {
	private static ActorSgyManager instance = null;
	private ActorSgyManager() {};
	synchronized public static ActorSgyManager getInstance() {
		if (instance != null)
			return instance;
		else {
			instance = new ActorSgyManager();
		}
		return instance;
	}
	// 获取活动的参与者执行范围
	public String[] getActorsRange(IBusinessKey formVo, IActivity humAct, ITask pTask, String pk_user) {
		try {
			return this.getPrepNoramlActos(formVo, humAct, pTask, pk_user);
		} finally {
			if (pTask != null) {
				WfCacheManager.getSessionCache().remove(pTask.getTaskPk());
			}
		}
	}
	// 获取用户指定的参与者
	public String[] getRuntimeActors(IBusinessKey formVo, IActivity humAct, ITask task) {
		try {
			return this.getRuntimeNormalActors(formVo, humAct, task);
		} finally {
			if (task != null) {
				WfCacheManager.getSessionCache().remove(task.getTaskPk());
			}
		}
	}

	public String[] getActivityActors(IBusinessKey formVo, String activityId, String processKey) {
		IProcessDefinition processDef = ProcessDefinitionUtil.getProDefByProDefPk(processKey);
		IActivity activity = processDef.findActivity(activityId);
		List<String> users = this.getPrepNextActors(formVo,activity);
		return users.toArray(new String[0]);
	}
	
	// 计算预算的时候的参与者，只有在本活动节点时和运行时的计算是一样的，否则都是预计算出防围，然后再参与时候指派
	private String[] getPrepNoramlActos(IBusinessKey formVo, IActivity activity, ITask task,String pk_user) {
		TaskEntity taskIns = (TaskEntity) task;
		if (task == null) {// 如果任务为空，说明activity为开始后的第一个活动节点，这时候的参与者为流程的发起者
			return new String[] { pk_user };
		} // 如果任务的活动和下一步骤的活动节点是一样的，说明是任务串行，或者是前加签
		IActivity cntActivity = task.getExecution().getActivity();
		if (cntActivity.getId().equalsIgnoreCase(activity.getId())) {// 如果在当前活动接点，说明本步参与者的信息需要在库中查询
			return getRuntimeLocalActors(activity, taskIns);
		}
		// 这个时候流程需要推进，那么参与者是从流程定义中计算得来
		List<String> users = getPrepNextActors(formVo, activity);
		return users.toArray(new String[0]);
	}
	// 运行时的参与者，不管加签还是指派都是重库表中的查询计算得出
	private String[] getRuntimeNormalActors(IBusinessKey formVo, IActivity activity, ITask task) {
		if (activity == null) {
			throw new WorkflowRuntimeException("请提供下一步骤的活动节点，才可计算下一步的参与者");
		}
		TaskEntity taskIns = (TaskEntity) task;
		if (task == null) {// 流程启动时，第一个活动节点的参与者
			return getRuntimeCommitActor(formVo, activity);
		}
		// 如果任务的活动和下一步骤的活动节点是一样的，说明是任务串行，或者是前加签
		if (task.getExecution().getActivity() == activity) {
			return getRuntimeLocalActors(activity, taskIns);
		}
		// 说明流程需要往前推进
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		if (flowInfoCtx instanceof NextTaskInsCtx) {
			String[] users = getRuntimeNextActors(formVo, activity, task);
			if(users != null){
				for(String user:users){
					if(user!=null) return users;
				}
			}
			return getRuntimeCommitActor(formVo, activity);
		}
		if (flowInfoCtx instanceof RejectTaskInsCtx) {
			return getRuntimeCommitActor(formVo, activity);
		}
		// 说明处理出错
		throw new WorkflowRuntimeException(activity.getId() + "的参与者计算出错");
	}
	// 获取预计算的下一步的参与者信息
	private List<String> getPrepNextActors(IBusinessKey formVo, IActivity activity) {
		ActivityBehavior behavior = activity.getActivityBehavior();
		TaskDefinition taskDefinition = null;
		if (behavior instanceof UserTaskActivityBehavior) {
			UserTaskActivityBehavior userTaskBehaviro = (UserTaskActivityBehavior) behavior;
			taskDefinition = userTaskBehaviro.getTaskDefinition();
		}
		ParticipantContext pc = new ParticipantContext();
		pc.setBillEntity(formVo);
		pc.setParticipants(taskDefinition.getParticipants());
		List<String> users = WfmServiceFacility.getIParticipantService().getUsers(pc);
		return users;
	}
	// 获取运行时提交的参与者信息
	private String[] getRuntimeCommitActor(IBusinessKey formVo, IActivity activity) {
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		if (flowInfoCtx instanceof CommitProInsCtx) {
			String proInsPk = WorkflowContext.getCurrentBpmnSession().getProcessInstance().getProInsPk();
			CommitProInsCtx commitProInsCtx = (CommitProInsCtx) flowInfoCtx;
			UserTaskRunTimeCtx[] userTaskCtx = commitProInsCtx.getNextInfo();
			UserTaskRunTimeCtx taskCtx = null;
			for (int i = 0; i < userTaskCtx.length; i++) {
				taskCtx = userTaskCtx[i];
				if (taskCtx.getActivityId().equalsIgnoreCase(activity.getId())) {
					break;
				}
			}
			if (taskCtx.isSequence()) {
				return new String[] { this.getAssignSequenceActors(proInsPk, activity.getId()) };
			} else {
				return this.getAssignParallelActors(formVo, activity, null);
			}
		}
		if (flowInfoCtx instanceof NextTaskInsCtx) {
			List<String> users = this.getPrepNextActors(formVo, activity);
			return users.toArray(new String[0]);
		}
		if(flowInfoCtx instanceof NextReceiveTaskCtx){
			List<String> users = this.getPrepNextActors(formVo, activity);
			return users.toArray(new String[0]);
		}
		if(flowInfoCtx instanceof RejectTaskInsCtx){
			List<String> users = this.getPrepNextActors(formVo, activity);
			return users.toArray(new String[0]);
		}
		return null;
	}
	// 获取运行时本步的参与者信息
	private String[] getRuntimeLocalActors(IActivity activity, TaskEntity taskIns) {
		if (taskIns.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign)) { // 如果当前任务前加签任务，那么...
			// 如果当前任务前加签任务，那么
			String maxTimes = WfmServiceFacility.getBeforeAddSignQry().getMaxStateTimeByTaskPk(taskIns.getSuperTask().getTaskPk());
			BeforeAddSignVO beforeAddSignVo = WfmServiceFacility.getBeforeAddSignQry().getBeforeAddSignVoByTaskPkAndTime(taskIns.getSuperTask().getTaskPk(), maxTimes);
			boolean flag = false;
			if (beforeAddSignVo.getLogic().equalsIgnoreCase(Logic.Sequence.toString())) {// 如果前加签任务是串行
				flag = true;
			} else {
				flag = false;
			}
			if (flag) {// 如果前加签任务是串行
				beforeAddSignVo = WfmServiceFacility.getBeforeAddSignQry().getBeofreAddSingVoByTaskAndTimeAndOrder(taskIns.getSuperTask().getTaskPk(), maxTimes, "0", false);
				BeforeAddSignUserVO[] userVos = beforeAddSignVo.getBeforeAddSignUserVos();
				if (userVos != null && userVos.length != 0) {
					return new String[] { userVos[0].getPk_user() };
				} else {
					TaskEntity superTask = (TaskEntity) taskIns.getSuperTask();
					return new String[] { superTask.getOwner() };
				}
			} else {
				TaskEntity superTask = (TaskEntity) taskIns.getSuperTask();
				return new String[] { superTask.getOwner() };
			}
			// // 如果前加签任务是并行
			// BeforeAddSignUserVO[] addSignUserVos =
			// beforeAddSignVo.getBeforeAddSignUserVos();
			// List<String> list = new ArrayList<String>();
			// for (int i = 0; i < addSignUserVos.length; i++) {
			// list.add(addSignUserVos[i].getPk_user());
			// }
			// return list.toArray(new String[0]);
		}
		// 任务串行
		String proInsPk = taskIns.getExecution().getProcessInstance().getProInsPk();
		String activityId = activity.getId();
		String userPk = this.getAssignSequenceActors(proInsPk, activityId);
		if(userPk == null)
			return null;
		return new String[] {userPk};
	}
	
	// 获取运行时下一步的参与者信息
	private String[] getRuntimeNextActors(IBusinessKey formVo, IActivity activity, ITask task){
		boolean isSequence = false;
		boolean isAfterSign = activity.isAfterSign();

		if (isAfterSign) {
			isSequence = activity.isSequence();
		}
		if(!isAfterSign){
			isSequence = isAssignSequence(task.getExecution().getProcessInstance().getProInsPk(), activity.getId());
		}

		if (isSequence) {
			return new String[] { this.getAssignSequenceActors(task.getExecution().getProcessInstance().getProInsPk(), activity.getId()) };
		} else {
			return this.getAssignParallelActors(formVo, activity, task);
		}
	}
	
	/**
	 * 获取平行指派的信息
	 */
	private String[] getAssignParallelActors(IBusinessKey formVo, IActivity actitity, ITask task) {
		String proInsPk = null;
		if (task == null) {
			proInsPk = WorkflowContext.getCurrentBpmnSession().getProcessInstance().getProInsPk();
		} else {
			proInsPk = task.getExecution().getProcessInstance().getProInsPk();
		}
		AssignmentVO[] assignmentVos = WfmServiceFacility.getAssignmentQry().getAssignmentVos(proInsPk, actitity.getId());
		if(assignmentVos == null)
		{
			return null;
		}
		String[] returnValue = new String[assignmentVos.length];
		for (int i = 0; i < assignmentVos.length; i++) {
			WfmServiceFacility.getAssignmentBill().delete(assignmentVos[i].getPk_assignment());
			returnValue[i] = assignmentVos[i].getPk_user();
		}
		return returnValue;
	}
	
	/**
	 * 获取串行指派的信息
	 */
	private boolean isAssignSequence(String proInsPk, String activityId) {
		AssignmentVO assignmentVo = WfmServiceFacility.getAssignmentQry().getAssignmentVo(proInsPk, activityId, "0");
		if(assignmentVo==null)
		    return false;
		return assignmentVo.getSequence().booleanValue();
	}


	/**
	 * 获取串行指派的信息
	 */
	private String getAssignSequenceActors(String proInsPk, String activityId) {
		AssignmentVO assignmentVos = WfmServiceFacility.getAssignmentQry().getAssignmentVo(proInsPk, activityId, "0");
		if(assignmentVos==null)
		    return null;
		return assignmentVos.getPk_user();
	}
	/**
	 * 获取主办参与者
	 */
	protected String[] getPrepMajorActors(IBusinessKey formVo, IActivity humAct, ITask task,String pk_user) {
		return this.getPrepNoramlActos(formVo, humAct, task, pk_user);
	}
	/**
	 * 获取协办参与者
	 */
	protected String[] getPrepAssistActors(IBusinessKey formVo, IActivity humAct, ITask task) {
		return null;
	}
	
}
