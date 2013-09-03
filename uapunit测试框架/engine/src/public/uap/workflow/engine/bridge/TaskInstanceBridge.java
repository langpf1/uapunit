package uap.workflow.engine.bridge;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;


import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.utils.DateUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
public class TaskInstanceBridge implements IBridge<TaskInstanceVO, ITask> {
	@Override
	public TaskInstanceVO convertT2M(ITask object) {
		TaskEntity taskEntity = (TaskEntity) object;
		TaskInstanceVO taskInsVo = new TaskInstanceVO();
		taskInsVo.setPk_task(object.getTaskPk());
		taskInsVo.setPk_owner(object.getOwner());
		taskInsVo.setPk_process_def(object.getProcessDefinition().getProDefPk());
		taskInsVo.setProcess_def_id(object.getProcessDefinition().getProDefId());
		taskInsVo.setProcess_def_name(object.getProcessDefinition().getName());
		taskInsVo.setPk_activity_instance(object.getExecution().getActInsPk());
		taskInsVo.setActivity_id(object.getExecution().getActivity().getId());
		taskInsVo.setIsexec(new UFBoolean(taskEntity.isExe()));
		taskInsVo.setIspass(new UFBoolean(taskEntity.isPass()));
		taskInsVo.setOpinion(taskEntity.getOpinion());
		taskInsVo.setActivity_name((String) object.getExecution().getActivity().getProperty("name"));
		taskInsVo.setPk_creater(object.getCreater());
		taskInsVo.setPk_owner(object.getOwner());
		taskInsVo.setPk_agenter(object.getAgenter());
		taskInsVo.setPk_executer(object.getExecuter());
		taskInsVo.setCreate_type(taskEntity.getCreateType().getIntValue());
		taskInsVo.setBeforeaddsign_times(taskEntity.getBeforeaddsign_times());
		taskInsVo.setPk_form_ins(taskEntity.getPk_form_ins());
		taskInsVo.setPk_form_ins_version(taskEntity.getPk_form_ins_version());
		taskInsVo.setPk_org(taskEntity.getPk_org());
		taskInsVo.setPk_group(taskEntity.getPk_group());
		taskInsVo.setForm_no(taskEntity.getForm_no());
		taskInsVo.setPk_bizobject(taskEntity.getPk_bizobject());
		taskInsVo.setPk_biztrans(taskEntity.getPk_biztrans());
		taskInsVo.setTitle(taskEntity.getTitle());
		taskInsVo.setUserobject(taskEntity.getUserobject());
		taskInsVo.setOpenUIStyle(taskEntity.getOpenUIStyle());
		taskInsVo.setOpenURI(taskEntity.getOpenURI());
		if (taskEntity.getSuperTask() != null) {
			taskInsVo.setPk_super(taskEntity.getSuperTask().getTaskPk());
		}
		if (taskEntity.getFinishType() != null) {
			taskInsVo.setFinish_type(taskEntity.getFinishType().getIntValue());
		}
		taskInsVo.setState_task(taskEntity.getStatus().getIntValue());
		if (object.getCreateTime() != null) {
			taskInsVo.setBegindate(new UFDateTime(object.getCreateTime()));
		}
		if (taskEntity.getFinishTime() != null) {
			taskInsVo.setFinishdate(new UFDateTime(taskEntity.getFinishTime()));
		}
		if (object.getDueDate() != null) {
			taskInsVo.setDutedate(new UFDateTime(object.getDueDate()));
		}
		if (object.getParentTask() != null) {
			taskInsVo.setPk_parent(object.getParentTask().getTaskPk());
		}
		if (taskEntity.getProcessInstance() != null) {
			taskInsVo.setPk_process_instance(taskEntity.getProcessInstance().getProInsPk());
		}
		ProcessInstanceEntity proInstity = (ProcessInstanceEntity) taskEntity.getExecution().getProcessInstance();
		taskInsVo.setStartdate(proInstity.getStartdatetime());
		return taskInsVo;
	}
	@Override
	public ITask convertM2T(TaskInstanceVO object) {
		TaskEntity task = new TaskEntity();
		task.setTaskInsVo(object);
		task.setTaskPk(object.getPk_task());
		task.setOwner(object.getPk_owner());
		task.setCreateTime(DateUtil.convert(object.getBegindate()));
		task.setFinishTime(DateUtil.convert(object.getFinishdate()));
		task.setDueDate(DateUtil.convert(object.getDutedate()));
		task.setOpenUIStyle(object.getOpenUIStyle());
		task.setOpenURI(object.getOpenURI());
		task.setOpinion(object.getOpinion());
		
		ActivityBehavior activityBehavior = task.getExecution().getActivity().getActivityBehavior();
		if(activityBehavior instanceof UserTaskActivityBehavior)
		{
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior)activityBehavior;
			task.setTaskDefinition(userTaskActivityBehavior.getTaskDefinition());
		}
		return task;
	}
}
