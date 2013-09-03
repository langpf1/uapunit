package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IWorkflowInstanceBill;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.ActivityInstanceVO;
public class WorkflowInstanceBill implements IWorkflowInstanceBill {
	@Override
	public ActivityInstanceVO asyn(ActivityInstanceVO actInsVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if (actInsVo.getPk_actins() == null || actInsVo.getPk_actins().length() == 0) {
				dao.insertVo(actInsVo);
			} else {
				dao.updateVo(actInsVo);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
		return actInsVo;
	}
	@Override
	public void update(ActivityInstanceVO[] actInsVos) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.updateVos(actInsVos);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
