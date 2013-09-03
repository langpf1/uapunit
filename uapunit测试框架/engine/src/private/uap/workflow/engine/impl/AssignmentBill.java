package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IAssignmentBill;
import uap.workflow.engine.vos.AssignmentVO;
/* ÷∏≈…*/
public class AssignmentBill implements IAssignmentBill {
	@Override
	public void insert(AssignmentVO[] vos) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.insertVos(vos);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public boolean delete(String pk_assignment) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByClause(AssignmentVO.class, "pk_assignment='" + pk_assignment + "'");
			return true;
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void update(AssignmentVO[] vos) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.updateVos(vos);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void saveOrUpdate(AssignmentVO vo) {
		WfBaseDAO dao = new WfBaseDAO();
		if (vo.getPk_assignment() == null || vo.getPk_assignment().length() == 0) {
			try {
				dao.insertVo(vo);
			} catch (DAOException e) {
				throw new WorkflowRuntimeException(e);
			}
		} else {
			try {
				dao.updateVo(vo);
			} catch (DAOException e) {
				throw new WorkflowRuntimeException(e);
			}
		}
	}
	@Override
	public boolean delete(String proInsPk, String activityId, String userPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByClause(AssignmentVO.class, "pk_proins='" + proInsPk + "' and activity_id='" + activityId + "' and pk_user='" + userPk + "' and order_str='0'");
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		return false;
	}
}
