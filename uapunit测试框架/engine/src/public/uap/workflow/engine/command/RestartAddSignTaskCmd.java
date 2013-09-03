package uap.workflow.engine.command;
import java.util.Iterator;
import java.util.List;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
public class RestartAddSignTaskCmd implements ICommand<Void> {
	public RestartAddSignTaskCmd() {
		super();
	}
	public Void execute() {
		ITask task = WorkflowContext.getCurrentBpmnSession().getTask();
		List<ITask> children = TaskUtil.getSubTaskBySuperTaskPk(task.getTaskPk());
		if (children == null || children.size() == 0) {
			throw new WorkflowRuntimeException("û�м�ǩ����,����Ҫ��ǩ����");
		}
		if (TaskInstanceStatus.BeforeAddSignComplete == task.getStatus()) {
			if (TaskUtil.isCmpletBeforeAddSignTask(task.getTaskPk())) {
				throw new WorkflowRuntimeException("��ǩ�Ѿ���ɣ�����Ҫ��ǩ����");
			}
		} else {
			throw new WorkflowRuntimeException("�Ѿ����ڼ�ǩ״̬�������ǩҪ����");
		}
		this.restartAddSignTask(task);
		this.update(task, TaskUtil.getMaxBeforeAddSignTimes(task.getTaskPk()));
		return null;
	}
	private void restartAddSignTask(ITask task) {
		task.setStatus(TaskInstanceStatus.fromIntValue(Integer.parseInt((String) task.getVariableLocal("ext9"))));
		task.removeVariableLocal("ext9");
		task.asyn();
	}
	private void update(ITask task, String maxAddSignTimes) {
		List<ITask> children = TaskUtil.getSubTaskBySuperTaskPk(task.getTaskPk());
		if (children == null || children.size() == 0) {
			return;
		}
		ITask tmpTask = null;
		try {
			Iterator<ITask> iter = children.iterator();
			while (iter.hasNext()) {
				tmpTask = iter.next();
				if (!tmpTask.isExe()) {
					if (maxAddSignTimes != null) {
						if (tmpTask.getBeforeaddsign_times().equalsIgnoreCase(maxAddSignTimes)) {
							this.restartAddSignTask(tmpTask);
						}
					} else {
						this.restartAddSignTask(tmpTask);
					}
					List<ITask> tmpChildren = TaskUtil.getSubTaskBySuperTaskPk(tmpTask.getTaskPk());
					if (tmpChildren != null && tmpChildren.size() != 0) {
						this.update(tmpTask, null);
					}
				}
			}
		} catch (WorkflowRuntimeException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
