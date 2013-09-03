package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessDefinitionBill;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.ProcessDefinitionVO;
public class ProcessDefinitionBill implements IProcessDefinitionBill {
	@Override
	public ProcessDefinitionVO saveProDefVo(ProcessDefinitionVO proDefVo) {
		WfBaseDAO dao = new WfBaseDAO();
		proDefVo.setDr(0);
		try {
			if (proDefVo.getPk_prodef() == null || proDefVo.getPk_prodef().length() == 0) {
				dao.insertVo(proDefVo);
			}else{
				dao.updateVo(proDefVo);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
		return proDefVo;
	}
}
