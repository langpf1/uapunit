package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.ITaskInstanceBill;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.TaskInstanceVO;
public class TaskInstanceBill implements ITaskInstanceBill {
	@Override
	public TaskInstanceVO asyn(TaskInstanceVO vo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			vo.setDr(0);
			if (vo.getPk_task() == null || vo.getPk_task().length() == 0) {
				dao.insertVo(vo);
			} else {
				dao.updateVo(vo);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
		return vo;
	}
	@Override
	public void deleteTaskByPk(String taskId) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByPk(TaskInstanceVO.class, taskId);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public void updateTaskVos(TaskInstanceVO[] vos) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.updateVos(vos);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
