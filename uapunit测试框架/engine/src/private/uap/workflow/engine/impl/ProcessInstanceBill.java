package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessInstanceBill;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class ProcessInstanceBill implements IProcessInstanceBill {
	@Override
	public ProcessInstanceVO asyn(ProcessInstanceVO proInsVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			proInsVo.setDr(0);
			if (proInsVo.getPk_proins() == null || proInsVo.getPk_proins().length() == 0) {
				dao.insertVo(proInsVo);
			} else {
				dao.updateVo(proInsVo);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
		return proInsVo;
	}
}
