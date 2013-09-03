package uap.workflow.engine.impl;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.SuperVO;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.ActivityInstanceVO;
public class WorkflowInstanceQry implements IWorkflowInstanceQry {
	@Override
	public ActivityInstanceVO getActInsVoByPk(String actInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if (actInsPk == null) {
				actInsPk = "";
			}
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ActivityInstanceVO.class, "pk_actins='" + actInsPk + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ActivityInstanceVO) vos[0];
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public ActivityInstanceVO[] getSubActInsVoByPk(String actInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if (actInsPk == null) {
				actInsPk = "";
			}
			String sql = "select * from wf_actins where pk_parent='" + actInsPk + "'";
			List<ActivityInstanceVO> taskInstanceVos = (List<ActivityInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(ActivityInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos.toArray(new ActivityInstanceVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public ActivityInstanceVO[] getActInsVoByProInsPk(String proInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if (proInsPk == null) {
				proInsPk = "";
			}
			String sql = "select * from wf_actins where pk_proins='" + proInsPk + "'";
			List<ActivityInstanceVO> taskInstanceVos = (List<ActivityInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(ActivityInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			} else {
				return taskInstanceVos.toArray(new ActivityInstanceVO[0]);
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public ActivityInstanceVO getActivityInstanceVoByActivityID(String pk_ProcessInstance, String activityID) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if (activityID == null) {
				activityID = "";
			}
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ActivityInstanceVO.class, " pk_proins='" + pk_ProcessInstance + "' and port_id='"+ activityID +"'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ActivityInstanceVO) vos[0];
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
