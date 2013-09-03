package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IVariableInstanceBill;
import uap.workflow.engine.vos.VariableInstanceVO;
public class VariableInstanceBill implements IVariableInstanceBill {
	@Override
	public void inser(VariableInstanceVO variableInstanceVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.insertVo(variableInstanceVo);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void update(VariableInstanceVO variableInstanceVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.updateVo(variableInstanceVo);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void delete(String pk_variableInstance) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByPk(VariableInstanceVO.class, pk_variableInstance);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public VariableInstanceVO asyn(VariableInstanceVO vo) {
		if (vo.getPk_variable() == null) {
			this.inser(vo);
		} else {
			this.update(vo);
		}
		return vo;
	}
}
