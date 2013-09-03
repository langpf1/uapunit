package uap.workflow.engine.itf;
import uap.workflow.engine.vos.TaskInstanceVO;
public interface ITaskInstanceBill {
	TaskInstanceVO asyn(TaskInstanceVO vo);
	void deleteTaskByPk(String taskId);
	void updateTaskVos(TaskInstanceVO[] vos);
}
