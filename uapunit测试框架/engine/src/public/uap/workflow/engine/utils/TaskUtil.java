package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.cache.WfCacheManager;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.itf.IBeforeAddSignQry;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.restlet.application.Pagination;
public class TaskUtil {
	public static ITask getTaskByTaskPk(String taskPk) {
		if (taskPk == null) {
			return null;
		}
		ITask task = (ITask) WfCacheManager.getSessionCache().get(taskPk);
		if (task == null) {
			ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
			TaskInstanceVO taskInsVo = taskQry.getTaskInsVoByPk(taskPk);
			task = new TaskInstanceBridge().convertM2T(taskInsVo);
			WfCacheManager.getSessionCache().put(taskPk, task);
		}
		return task;
	}

	public static List<TaskInstanceVO> getTaskByProcessInstancePk(String processInstancePk) {
		if (processInstancePk == null || processInstancePk.equals("")) {
			return null;
		}
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskQry.getTaskByProcessInstancePk(processInstancePk);
		return taskInsVos;
	}

	public static List<TaskInstanceVO> getTaskByActivityInstancePk(String activityInstancePk) {
		if (activityInstancePk == null || activityInstancePk.equals("")) {
			return null;
		}
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskQry.getTaskByActivityInstancePk(activityInstancePk);
		return taskInsVos;
	}

	public static BeforeAddSignVO getBeforeAddSignInfo(String taskPk) {
		String strMaxtTime = WfmServiceFacility.getBeforeAddSignQry().getMaxStateTimeByTaskPk(taskPk);
		BeforeAddSignVO beforeAddSignVo = WfmServiceFacility.getBeforeAddSignQry().getBeforeAddSignVoByTaskPkAndTime(taskPk, strMaxtTime);
		return beforeAddSignVo;
	}
	public static List<ITask> getSubTasksByTaskPk(String taskPk) {
		List<ITask> list = new ArrayList<ITask>();
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskQry.getSubTaskInsByTaskPk(taskPk);
		if (taskInsVos == null || taskInsVos.size() == 0) {
			return null;
		}
		for (int i = 0; i < taskInsVos.size(); i++) {
			list.add(new TaskInstanceBridge().convertM2T((TaskInstanceVO) taskInsVos.get(i)));
		}
		return list;
	}
	public static List<ITask> getSubTaskBySuperTaskPk(String superTaskPk) {
		List<ITask> list = new ArrayList<ITask>();
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskQry.getSubTaskInsBySuperPkAndMaxBeforeAddSignTime(superTaskPk, TaskUtil.getMaxBeforeAddSignTimes(superTaskPk));
		if (taskInsVos == null || taskInsVos.size() == 0) {
			return null;
		}
		for (int i = 0; i < taskInsVos.size(); i++) {
			list.add(new TaskInstanceBridge().convertM2T((TaskInstanceVO) taskInsVos.get(i)));
		}
		return list;
	}
	public static boolean isCmpletBeforeAddSignTask(String taskPk) {
		String strMaxtTime = WfmServiceFacility.getBeforeAddSignQry().getMaxStateTimeByTaskPk(taskPk);
		BeforeAddSignVO beforeAddSignVo = WfmServiceFacility.getBeforeAddSignQry().getBeforeAddSignVoByTaskPkAndTime(taskPk, strMaxtTime);
		BeforeAddSignUserVO[] userVos = beforeAddSignVo.getBeforeAddSignUserVos();
		Logic logic = Logic.getLogicByString(beforeAddSignVo.getLogic());
		int beforeAddsignCount = 0;
		int finishAddSigncount = 0;
		if (logic == Logic.Sequence) {
			for (int i = 0; i < userVos.length; i++) {
				beforeAddsignCount++;
				if (userVos[i].getIsusered().booleanValue()) {
					finishAddSigncount++;
				}
			}
		} else {
			List<TaskInstanceVO> taskInsVos = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getSubTaskInsBySuperPkAndMaxBeforeAddSignTime(taskPk, strMaxtTime);
			for (int i = 0; i < taskInsVos.size(); i++) {
				beforeAddsignCount++;
				TaskInstanceVO taskInsVo = taskInsVos.get(i);
				if (taskInsVo.getIsexec().booleanValue()) {
					finishAddSigncount++;
				}
			}
		}
		if (beforeAddsignCount == finishAddSigncount) {
			return true;
		} else {
			return false;
		}
	}
	public static String getMaxBeforeAddSignTimes(String taskPk) {
		return NCLocator.getInstance().lookup(IBeforeAddSignQry.class).getMaxStateTimeByTaskPk(taskPk);
	}
	public static ITask initTask(ITask task) {
		TaskEntity newTask = (TaskEntity) task;
		newTask.setTaskPk(null);
		newTask.setFinishType(null);
		newTask.setExecuter(null);
		newTask.setAgenter(null);
		newTask.setOwner(null);
		newTask.setSignTime(null);
		newTask.setFinishTime(null);
		newTask.setFinishType(null);
		return newTask;
	}

	public static List<TaskInstanceVO> getTasks(int taskState,String keyWord, String bizObject, String pk_user, String wherePart, boolean isGetMyStartTask, Pagination page){
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskQry.getTasks(taskState, keyWord, bizObject, pk_user, wherePart, isGetMyStartTask,page);
		return taskInsVos;
	}
}
