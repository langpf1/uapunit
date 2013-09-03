package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IJobBill;
import uap.workflow.engine.vos.JobVO;
public class JobBill implements IJobBill {
	@Override
	public JobVO asyn(JobVO jobVo) {
		WfBaseDAO dao = new WfBaseDAO();
		if (jobVo.getPk_job() == null) {
			try {
				dao.insertVo(jobVo);
			} catch (DAOException e) {
				throw new WorkflowRuntimeException(e);
			}
		} else {
			try {
				dao.updateVo(jobVo);
			} catch (DAOException e) {
				throw new WorkflowRuntimeException(e);
			}
		}
		return jobVo;
	}
	@Override
	public void delete(String pk_job) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByPk(JobVO.class, pk_job);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
}
