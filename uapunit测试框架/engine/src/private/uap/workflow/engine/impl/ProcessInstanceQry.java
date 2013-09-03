package uap.workflow.engine.impl;
import java.sql.SQLException;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.vo.pub.SuperVO;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessInstanceQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class ProcessInstanceQry implements IProcessInstanceQry {
	public ProcessInstanceVO getProInsVo(String proInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessInstanceVO.class, "pk_proins='" + proInsPk + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessInstanceVO) vos[0];
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	public ProcessInstanceVO[] getProcessInstanceVOs(String form_ins_versionPK) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessInstanceVO.class, " pk_form_ins_version='" + form_ins_versionPK + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessInstanceVO[]) vos;
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public ProcessInstanceVO getProcessInstanceVO(String form_instance_versionPK, String bizObejectKey) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from WF_PROINS where pk_form_ins_version=? and state_proins!=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(form_instance_versionPK);
			parameter.addParam(TaskInstanceStatus.Inefficient.getIntValue());
			List<ProcessInstanceVO> vos = (List<ProcessInstanceVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(ProcessInstanceVO.class));
			if (vos == null || vos.size() == 0) {
				return null;
			}
			return vos.get(0);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessInstanceVO> getAllProcessInstanceVOs(String sqlCondition,PaginationInfo page) throws DAOException {
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "select * from wf_proins ";
		if(sqlCondition != null && sqlCondition.length() != 0){
			sql = sql + "where "+sqlCondition;
		}
		if(page != null){
			sqlCondition = dao.creatQuerySqlBypage(sql, page);//构造分页查找sql语句	
		}
		List<ProcessInstanceVO> vos = (List<ProcessInstanceVO>) dao.executeQuery(sqlCondition,new BeanListProcessor(ProcessInstanceVO.class));
		if (vos == null || vos.size() == 0) {
			return null;
		} 
		return vos;
		
	}
	@Override
	public int getAllProcessInsNumber(String table) {
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "select rowcnt from sysindexes where id=object_id('"+table+"')";
		Long proinsNumber = null;
		try {
			proinsNumber =  (Long) dao.executeQuery(sql, new ColumnProcessor(1));
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return proinsNumber.intValue();
	}
}

