package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IEventSubscriptionBill;
import uap.workflow.engine.vos.EventSubscriptionVO;
public class EventSubscriptionBill implements IEventSubscriptionBill {
	@Override
	public void asyn(EventSubscriptionVO vo) {
		WfBaseDAO dao = new WfBaseDAO();
		if (vo.getPk_subscription() == null) {
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
	public void delete(String pk_sub) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.deleteByPk(EventSubscriptionVO.class, pk_sub);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
}
