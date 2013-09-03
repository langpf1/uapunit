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
			throw new WorkflowRuntimeException("û��ǰ��ǩ���񣬲�����ǰ��ǩֹͣ");
		}
		if (TaskInstanceStatus.BeforeAddSignComplete == task.getStatus()) { // ������״̬Ϊ��ǩ��ɣ����ܻ�ǩֹͣ
			throw new WorkflowRuntimeException("������״̬Ϊǰ��ǩ��ɣ�����Ҫǰ��ǩֹͣ");
		}
		task.setVariableLocal("ext9", task.getStatus().toString());// ������������
		task.setStatus(TaskInstanceStatus.BeforeAddSignComplete);
		task.asyn();
		/**
		 * �����������״̬
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
						 * û��ִ�еļ�ǩ����ֱ������Ϊ��ǩ��ֹ
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
						 * �����ǩ�ټ�ǩ����������û�м�ǩ��ɣ������Լ����������״̬Ϊ��ǩ��ֹ
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
