package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IBeforeAddSignQry;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
public class BeforeAddSignQry implements IBeforeAddSignQry {
	@Override
	public BeforeAddSignVO[] getBeforeAddSignVoByTaskPk(String taskPk) {
		String where = "pk_task='" + taskPk + "'";
		BeforeAddSignVO[] vos = this.getBeforeAddSignVosByWhere(where);
		if (vos == null || vos.length == 0) {
			return null;
		}
		int length = vos.length;
		for (int i = 0; i < length; i++) {
			where = "pk_beforeaddsign='" + vos[i].getPk_beforeaddsign() + "'";
			BeforeAddSignUserVO[] addSignUserVos = this.getBeforeAddSignUserVosByWhere(where);
			vos[i].setBeforeAddSignUserVos(addSignUserVos);
		}
		return vos;
	}
	public BeforeAddSignUserVO[] getBeforeAddSignUserVosByWhere(String where) {
		BeforeAddSignUserVO[] vos = null;
		WfBaseDAO dao = new WfBaseDAO();
		try {
			vos = (BeforeAddSignUserVO[]) dao.queryByCondition(BeforeAddSignUserVO.class, where);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		return vos;
	}
	public BeforeAddSignVO[] getBeforeAddSignVosByWhere(String where) {
		BeforeAddSignVO[] vos = null;
		WfBaseDAO dao = new WfBaseDAO();
		try {
			vos = (BeforeAddSignVO[]) dao.queryByCondition(BeforeAddSignVO.class, where);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		return vos;
	}
	@Override
	public BeforeAddSignVO getBeforeAddSignVoByTaskPkAndTime(String taskPk, String time) {
		String where = "pk_task='" + taskPk + "' and times ='" + time + "'";
		BeforeAddSignVO[] vos = this.getBeforeAddSignVosByWhere(where);
		if (vos == null || vos.length == 0) {
			return null;
		}
		where = "pk_beforeaddsign='" + vos[0].getPk_beforeaddsign() + "'";
		BeforeAddSignUserVO[] addSignUserVos = this.getBeforeAddSignUserVosByWhere(where);
		vos[0].setBeforeAddSignUserVos(addSignUserVos);
		return vos[0];
	}
	@Override
	public BeforeAddSignVO getBeofreAddSingVoByTaskAndTimeAndOrder(String taskPk, String time, String order, boolean isUsered) {
		String where = "pk_task='" + taskPk + "' and times ='" + time + "'";
		BeforeAddSignVO[] vos = this.getBeforeAddSignVosByWhere(where);
		if (vos == null || vos.length == 0) {
			return null;
		}
		String flag = "N";
		if (isUsered) {
			flag = "Y";
		}
		where = "pk_beforeaddsign='" + vos[0].getPk_beforeaddsign() + "' and order_str='" + order + "' and isusered='" + flag + "'";
		BeforeAddSignUserVO[] userVos = this.getBeforeAddSignUserVosByWhere(where);
		vos[0].setBeforeAddSignUserVos(userVos);
		return vos[0];
	}
	@Override
	public BeforeAddSignVO getBeforeAddSingVoByAddSignPk(String addSignPk) {
		String where = "pk_addsign='" + addSignPk + "'";
		BeforeAddSignVO[] vos = this.getBeforeAddSignVosByWhere(where);
		if (vos == null || vos.length == 0) {
			return null;
		}
		BeforeAddSignUserVO[] addSignUserVos = this.getBeforeAddSignUserVosByWhere(where);
		vos[0].setBeforeAddSignUserVos(addSignUserVos);
		return vos[0];
	}
	@Override
	public String getMaxStateTimeByTaskPk(String taskPk) {
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "SELECT max(times) FROM wf_beforeaddsign WHERE pk_task = '" + taskPk + "'";
		try {
			String maxValue = (String) dao.executeQuery(sql, new ColumnProcessor());
			if (maxValue == null) {
				return String.valueOf(0);
			}
			return String.valueOf(maxValue);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
}
