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
 * �����߲��ԵĹ�����
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
	// ��ȡ��Ĳ�����ִ�з�Χ
	public String[] getActorsRange(IBusinessKey formVo, IActivity humAct, ITask pTask, String pk_user) {
		try {
			return this.getPrepNoramlActos(formVo, humAct, pTask, pk_user);
		} finally {
			if (pTask != null) {
				WfCacheManager.getSessionCache().remove(pTask.getTaskPk());
			}
		}
	}
	// ��ȡ�û�ָ���Ĳ�����
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
	
	// ����Ԥ���ʱ��Ĳ����ߣ�ֻ���ڱ���ڵ�ʱ������ʱ�ļ�����һ���ģ�������Ԥ�������Χ��Ȼ���ٲ���ʱ��ָ��
	private String[] getPrepNoramlActos(IBusinessKey formVo, IActivity activity, ITask task,String pk_user) {
		TaskEntity taskIns = (TaskEntity) task;
		if (task == null) {// �������Ϊ�գ�˵��activityΪ��ʼ��ĵ�һ����ڵ㣬��ʱ��Ĳ�����Ϊ���̵ķ�����
			return new String[] { pk_user };
		} // �������Ļ����һ����Ļ�ڵ���һ���ģ�˵���������У�������ǰ��ǩ
		IActivity cntActivity = task.getExecution().getActivity();
		if (cntActivity.getId().equalsIgnoreCase(activity.getId())) {// ����ڵ�ǰ��ӵ㣬˵�����������ߵ���Ϣ��Ҫ�ڿ��в�ѯ
			return getRuntimeLocalActors(activity, taskIns);
		}
		// ���ʱ��������Ҫ�ƽ�����ô�������Ǵ����̶����м������
		List<String> users = getPrepNextActors(formVo, activity);
		return users.toArray(new String[0]);
	}
	// ����ʱ�Ĳ����ߣ����ܼ�ǩ����ָ�ɶ����ؿ���еĲ�ѯ����ó�
	private String[] getRuntimeNormalActors(IBusinessKey formVo, IActivity activity, ITask task) {
		if (activity == null) {
			throw new WorkflowRuntimeException("���ṩ��һ����Ļ�ڵ㣬�ſɼ�����һ���Ĳ�����");
		}
		TaskEntity taskIns = (TaskEntity) task;
		if (task == null) {// ��������ʱ����һ����ڵ�Ĳ�����
			return getRuntimeCommitActor(formVo, activity);
		}
		// �������Ļ����һ����Ļ�ڵ���һ���ģ�˵���������У�������ǰ��ǩ
		if (task.getExecution().getActivity() == activity) {
			return getRuntimeLocalActors(activity, taskIns);
		}
		// ˵��������Ҫ��ǰ�ƽ�
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
		// ˵���������
		throw new WorkflowRuntimeException(activity.getId() + "�Ĳ����߼������");
	}
	// ��ȡԤ�������һ���Ĳ�������Ϣ
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
	// ��ȡ����ʱ�ύ�Ĳ�������Ϣ
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
	// ��ȡ����ʱ�����Ĳ�������Ϣ
	private String[] getRuntimeLocalActors(IActivity activity, TaskEntity taskIns) {
		if (taskIns.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign)) { // �����ǰ����ǰ��ǩ������ô...
			// �����ǰ����ǰ��ǩ������ô
			String maxTimes = WfmServiceFacility.getBeforeAddSignQry().getMaxStateTimeByTaskPk(taskIns.getSuperTask().getTaskPk());
			BeforeAddSignVO beforeAddSignVo = WfmServiceFacility.getBeforeAddSignQry().getBeforeAddSignVoByTaskPkAndTime(taskIns.getSuperTask().getTaskPk(), maxTimes);
			boolean flag = false;
			if (beforeAddSignVo.getLogic().equalsIgnoreCase(Logic.Sequence.toString())) {// ���ǰ��ǩ�����Ǵ���
				flag = true;
			} else {
				flag = false;
			}
			if (flag) {// ���ǰ��ǩ�����Ǵ���
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
			// // ���ǰ��ǩ�����ǲ���
			// BeforeAddSignUserVO[] addSignUserVos =
			// beforeAddSignVo.getBeforeAddSignUserVos();
			// List<String> list = new ArrayList<String>();
			// for (int i = 0; i < addSignUserVos.length; i++) {
			// list.add(addSignUserVos[i].getPk_user());
			// }
			// return list.toArray(new String[0]);
		}
		// ������
		String proInsPk = taskIns.getExecution().getProcessInstance().getProInsPk();
		String activityId = activity.getId();
		String userPk = this.getAssignSequenceActors(proInsPk, activityId);
		if(userPk == null)
			return null;
		return new String[] {userPk};
	}
	
	// ��ȡ����ʱ��һ���Ĳ�������Ϣ
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
	 * ��ȡƽ��ָ�ɵ���Ϣ
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
	 * ��ȡ����ָ�ɵ���Ϣ
	 */
	private boolean isAssignSequence(String proInsPk, String activityId) {
		AssignmentVO assignmentVo = WfmServiceFacility.getAssignmentQry().getAssignmentVo(proInsPk, activityId, "0");
		if(assignmentVo==null)
		    return false;
		return assignmentVo.getSequence().booleanValue();
	}


	/**
	 * ��ȡ����ָ�ɵ���Ϣ
	 */
	private String getAssignSequenceActors(String proInsPk, String activityId) {
		AssignmentVO assignmentVos = WfmServiceFacility.getAssignmentQry().getAssignmentVo(proInsPk, activityId, "0");
		if(assignmentVos==null)
		    return null;
		return assignmentVos.getPk_user();
	}
	/**
	 * ��ȡ���������
	 */
	protected String[] getPrepMajorActors(IBusinessKey formVo, IActivity humAct, ITask task,String pk_user) {
		return this.getPrepNoramlActos(formVo, humAct, task, pk_user);
	}
	/**
	 * ��ȡЭ�������
	 */
	protected String[] getPrepAssistActors(IBusinessKey formVo, IActivity humAct, ITask task) {
		return null;
	}
	
}
