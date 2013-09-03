package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IBeforeAddSignBill;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
public class BeforeAddSignBill implements IBeforeAddSignBill {
	@Override
	public void deleteBeforeAddSignVoByTaskPk(String taskPk) {
		WfBaseDAO dao = new WfBaseDAO();
		String sql1 = "delete from wfm_beforeaddsign where pk_beforeaddsign in( select pk_beforeaddsign from wfm_beforeaddsign where pk_task='" + taskPk + "') ";
		String sql2 = "delete wfm_beforeaddsign where pk_task='" + taskPk + "'";
		try {
			dao.executeUpdate(sql1);
			dao.executeUpdate(sql2);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	public void deleteBeforeAddSignUserVo(String pk_beforeaddsign, String userPk, String order_str) {
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "delete from wf_beforeaddsignuser where pk_beforeaddsign='" + pk_beforeaddsign + "' and ";
		sql = sql + " userPk='" + userPk + "' and order_str='" + 0 + "'";
		try {
			dao.executeUpdate(sql);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void saveBeforeAddSignVo(BeforeAddSignVO addSignVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.insertVo(addSignVo);
			BeforeAddSignUserVO[] addSignUserVos = addSignVo.getBeforeAddSignUserVos();
			for (int i = 0; i < addSignUserVos.length; i++) {
				addSignUserVos[i].setPk_beforeaddsign(addSignVo.getPk_beforeaddsign());
			}
			dao.insertVos(addSignUserVos);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	public void updateBeforeAddSignUserVo(BeforeAddSignUserVO userVo) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.updateVo(userVo);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
}
