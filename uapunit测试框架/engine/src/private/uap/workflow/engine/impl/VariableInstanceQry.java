package uap.workflow.engine.impl;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IVariableInstanceQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.VariableInstanceVO;
public class VariableInstanceQry implements IVariableInstanceQry {
	@SuppressWarnings("unchecked")
	@Override
	public VariableInstanceVO getVariableInstanceByPk(String pk_variableInstance) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_variable where pk_variable='" + pk_variableInstance + "'";
			List<VariableInstanceVO> varInsVos = (List<VariableInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(VariableInstanceVO.class));
			if (varInsVos == null || varInsVos.size() == 0) {
				return null;
			} else {
				return varInsVos.get(0);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceVO> getVariableInstanceByProcessInstancePk(String processsInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_variable where pk_process_instance='" + processsInstancePk + "'";
			List<VariableInstanceVO> varInsVos = (List<VariableInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(VariableInstanceVO.class));
			if (varInsVos == null || varInsVos.size() == 0) {
				return null;
			}
			return varInsVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceVO> getVariableInstanceByActivityInstancePk(String activityInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_variable where pk_activity_instance='" + activityInstancePk + "'";
			List<VariableInstanceVO> varInsVos = (List<VariableInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(VariableInstanceVO.class));
			if (varInsVos == null || varInsVos.size() == 0) {
				return null;
			}
			return varInsVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceVO> getVariableInstanceByTaskInstancePk(String taskInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_variable where pk_task='" + taskInstancePk + "'";
			List<VariableInstanceVO> varInsVos = (List<VariableInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(VariableInstanceVO.class));
			if (varInsVos == null || varInsVos.size() == 0) {
				return null;
			}
			return varInsVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
