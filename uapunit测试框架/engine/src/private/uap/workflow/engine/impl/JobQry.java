package uap.workflow.engine.impl;
import java.util.Date;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IJobQry;
import uap.workflow.engine.vos.JobVO;
public class JobQry implements IJobQry {
	@SuppressWarnings("unchecked")
	public JobVO[] selectNextJobsToExecute() {
		String sql = "";
		sql = "select * from wf_job ";
		sql = sql + "where (duedate is null or duedate<='" + new UFDateTime().toLocalString() + "') ";
		sql = sql + "and  (lockowner is null or lockexpirationtime <='" + new UFDateTime().toLocalString() + "') ";
		sql = sql + "and (retries>0)";
		WfBaseDAO dao = new WfBaseDAO();
		List<JobVO> jobVos = null;
		try {
			jobVos = (List<JobVO>) dao.executeQuery(sql, new BeanListProcessor(JobVO.class));
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		if (jobVos == null || jobVos.size() == 0) {
			return null;
		}
		return jobVos.toArray(new JobVO[0]);
	}
	@SuppressWarnings("unchecked")
	@Override
	public JobVO[] findUnlockedTimersByDuedate(Date duedate) {
		String sql = "";
		sql = "select * from wf_job  ";
		sql = sql + "where (type = 'timer') and (duedate is not null)  ";
		sql = sql + "and (duedate <= '" + new UFDateTime(duedate) + "') ";
		sql = sql + "and (lockowner is null or lockexpirationtime <= '" + new UFDateTime(duedate) + "') ";
		sql = sql + "and (retries  >= 0)  order by duedate ";
		WfBaseDAO dao = new WfBaseDAO();
		List<JobVO> jobVos = null;
		try {
			jobVos = (List<JobVO>) dao.executeQuery(sql, new BeanListProcessor(JobVO.class));
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		if (jobVos == null || jobVos.size() == 0) {
			return null;
		}
		return jobVos.toArray(new JobVO[0]);
	}
	@SuppressWarnings("unchecked")
	@Override
	public JobVO[] findExclusiveJobsToExecute(String processInstanceId) {
		String sql = "";
		sql = "select *  from wf_job  ";
		sql = sql + "where (retries >= 0) and (duedate is null or duedate < ='" + new UFDateTime() + "')";
		sql = sql + " and (lockowner is null or lockexpirationtime <='" + new UFDateTime() + "')";
		sql = sql + " and (isexclusive = 'Y') and (pk_processInstance = '" + processInstanceId + "')";
		WfBaseDAO dao = new WfBaseDAO();
		List<JobVO> jobVos = null;
		try {
			jobVos = (List<JobVO>) dao.executeQuery(sql, new BeanListProcessor(JobVO.class));
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		if (jobVos == null || jobVos.size() == 0) {
			return null;
		}
		return jobVos.toArray(new JobVO[0]);
	}
	@SuppressWarnings("unchecked")
	@Override
	public JobVO getJobByPk(String jobPk) {
		String sql = "";
		sql = "select * from wf_job where pk_job='" + jobPk + "'";
		WfBaseDAO dao = new WfBaseDAO();
		List<JobVO> jobVos = null;
		try {
			jobVos = (List<JobVO>) dao.executeQuery(sql, new BeanListProcessor(JobVO.class));
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
		if (jobVos == null || jobVos.size() == 0) {
			return null;
		}
		return jobVos.get(0);
	}
}
