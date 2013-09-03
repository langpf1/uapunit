package uap.workflow.engine.command;
import java.util.Iterator;
import java.util.List;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
public class StopBeforeAddSignTaskCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		ITask task = WorkflowContext.getCurrentBpmnSession().getTask();
		List<ITask> children = TaskUtil.getSubTaskBySuperTaskPk(task.getTaskPk());
		if (children == null || children.size() == 0) {
			throw new WorkflowRuntimeException("没有前加签任务，不需做前加签停止");
		}
		if (TaskInstanceStatus.BeforeAddSignComplete == task.getStatus()) { // 该任务状态为会签完成，不能会签停止
			throw new WorkflowRuntimeException("该任务状态为前加签完成，不需要前加签停止");
		}
		task.setVariableLocal("ext9", task.getStatus().toString());// 先做变量保存
		task.setStatus(TaskInstanceStatus.BeforeAddSignComplete);
		task.asyn();
		/**
		 * 更新子任务的状态
		 */
		this.update(task, TaskUtil.getMaxBeforeAddSignTimes(task.getTaskPk()));
		return null;
	}
	private void update(ITask task, String maxAddStateTimes) {
		List<ITask> children = TaskUtil.getSubTaskBySuperTaskPk(task.getTaskPk());
		if (children != null) {
			Iterator<ITask> iter = children.iterator();
			ITask tmpTask = null;
			List<ITask> tmpChildren = null;
			try {
				while (iter.hasNext()) {
					tmpTask = iter.next();
					tmpChildren = TaskUtil.getSubTaskBySuperTaskPk(tmpTask.getTaskPk());
					if (tmpChildren == null || tmpChildren.size() == 0) {
						/**
						 * 没有执行的加签任务直接设置为加签终止
						 */
						if (TaskInstanceCreateType.BeforeAddSign == tmpTask.getCreateType()) {
							if (!tmpTask.isExe()) {
								if (maxAddStateTimes != null) {
									if (tmpTask.getBeforeaddsign_times().equalsIgnoreCase(maxAddStateTimes)) {
										this.stopAddSignTask(tmpTask);
									}
								} else {
									this.stopAddSignTask(tmpTask);
								}
							}
						}
					} else {
						/**
						 * 如果加签再加签出来的任务没有加签完成，更新自己和子任务的状态为加签终止
						 */
						if (TaskInstanceCreateType.BeforeAddSign == tmpTask.getCreateType()) {
							if (!tmpTask.isExe()) {
								this.stopAddSignTask(tmpTask);
								this.update(tmpTask, null);
							}
						}
					}
				}
			} catch (WorkflowRuntimeException e) {
				throw new WorkflowRuntimeException(e.getMessage());
			}
		}
	}
	private void stopAddSignTask(ITask task) {
		task.setVariableLocal("ext9", task.getStatus().toString());
		task.setStatus(TaskInstanceStatus.BeforeAddSignStop);
		task.asyn();
	}
}
