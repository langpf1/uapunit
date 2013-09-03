package uap.workflow.engine.impl;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.db2.jcc.sqlj.e;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.vo.pub.SuperVO;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.restlet.application.Pagination;
public class TaskInstanceQry implements ITaskInstanceQry {
	private Logger logger = Logger.getLogger(this.getClass());
	public List<TaskInstanceVO> getTaskByProcessInstancePk(String processInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where PK_PROCESS_INSTANCE='" + processInstancePk + "' order by ts";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	
	public TaskInstanceVO getTaskInsVoByPk(String taskInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			SuperVO[] taskInsVos = dao.queryByCondition(TaskInstanceVO.class, "pk_task='" + taskInsPk + "'");
			if (taskInsVos == null || taskInsVos.length == 0) {
				WorkflowLogger.error("����pk_task=" + taskInsPk + "δ�������ݿ��в�����Ӧ������ʵ��");
				throw new WorkflowRuntimeException("����pk_task=" + taskInsPk + "δ�������ݿ��в�����Ӧ������ʵ��");
			}
			return (TaskInstanceVO) taskInsVos[0];
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public List<TaskInstanceVO> getTaskByActivityInstancePk(String activityInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where PK_ACTIVITY_INSTANCE='" + activityInstancePk + "' order by ts";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskInstanceVO> getSubTaskInsByTaskPk(String taskInsPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_parent='" + taskInsPk + "'";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public List<TaskInstanceVO> getSubTaskInsBySuperPk(String superTaskPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_super='" + superTaskPk + "'";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskInstanceVO> getTasksByActivitiPk(String activitiPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_activity_instance='" + activitiPk + "'";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskInstanceVO> getTasksByFormInstancePk(String formInstancePk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_form_ins_version='" + formInstancePk + "'";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	/*
	 * ���Ƶ�����Ѿ���ɻ
	 */
	public List<TaskInstanceVO> getFinishedTasks(String billType, String billId, String checkman) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_form_ins_version='" + billId + "' and (pk_bizobject ='" + billType + "' or pk_biztrans='" + billType + "')"
					+ (checkman == null ? "" : (" and pk_executer='" + checkman + "'")) + " and state_task=" + TaskInstanceStatus.Finished.getIntValue() 
					+ " and create_type!=" + TaskInstanceCreateType.Makebill.getIntValue() + " order by finishdate desc, begindate desc";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskInstanceVO> getSubTaskInsBySuperPkAndMaxBeforeAddSignTime(String superPk, String times) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_task where pk_super='" + superPk + "' and beforeaddsign_times='" + times + "'";
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}

	/**
	 * ��ѯ����
	 * @param taskState ����״̬:(����,�Ѱ�,���|����,����,�ı�,ȫ����״̬(-1))
	 * @param keyWord �ؼ���(title,a.activity_name,form_noģ����ѯ)
	 * @param bizObject ��������
	 * @param pk_user   pk_agenter����pk_user
	 * @param wherePart where����
	 * @param isGetMyStartTask �����ҷ��������

	 * @return
	 */
	public List<TaskInstanceVO> getTasks(int taskState,String keyWord, String bizObject, String pk_user, String wherePart, boolean isGetMyStartTask,Pagination page){
		String sql = "";
		if(TaskInstanceStatus.End.getIntValue() == taskState){
			sql = "select a.* from wf_task a inner join wf_proins b on a.pk_process_instance = b.pk_proins where (b.state_proins = "+ProcessInstanceStatus.Started.getIntValue()+ " or b.state_proins = "+ProcessInstanceStatus.Suspended.getIntValue()+" or b.state_proins = "+ProcessInstanceStatus.Finished.getIntValue()+ ") and ((a.pk_owner='"+ pk_user +"'  and (a.pk_agenter = '~' or a.pk_agenter is null)) or(a.pk_agenter='"+ pk_user +"' and a.pk_owner<> '"+ pk_user +"')) ";
		}else if(TaskInstanceStatus.Finished.getIntValue() == taskState){
			sql = "select a.* from wf_task a inner join wf_proins b on a.pk_process_instance = b.pk_proins  where (b.state_proins = "+ProcessInstanceStatus.Finished.getIntValue()+ " or b.state_proins = "+ProcessInstanceStatus.Inefficient.getIntValue()+ ") and ((a.pk_owner='"+ pk_user +"'  and (a.pk_agenter = '~' or a.pk_agenter is null)) or(a.pk_agenter='"+ pk_user +"' and a.pk_owner<> '"+ pk_user +"')) ";
		}
		else
			sql = "select a.* from wf_task a inner join wf_proins b on a.pk_process_instance = b.pk_proins where ((a.pk_owner='"+ pk_user +"'  and (a.pk_agenter = '~' or a.pk_agenter is null)) or(a.pk_agenter='"+ pk_user +"' and a.pk_owner<> '"+ pk_user +"')) ";
	 
		if (TaskInstanceStatus.Finished.getIntValue() == taskState) {
			sql += " and (a.state_task =" + 
			TaskInstanceStatus.BeforeAddSignStop.getIntValue() + " or a.state_task =" + 
			TaskInstanceStatus.End.getIntValue() + " or a.state_task =" + 
			TaskInstanceStatus.Inefficient.getIntValue() + ") ";
			// ��������ǰ��ǩ�������������ڰ�������
		} else if (TaskInstanceStatus.Wait.getIntValue() == taskState) {
			sql += " and (a.state_task = " + 
			TaskInstanceStatus.Wait.getIntValue() + " or a.state_task = " + 
			TaskInstanceStatus.Run.getIntValue() + " or a.state_task = " + 
			//TaskInstanceStatus.BeforeAddSignPlmnt +  " or a.state_task = " + 
			TaskInstanceStatus.BeforeAddSignSend.getIntValue() + " or a.state_task = " +
			TaskInstanceStatus.BeforeAddSignComplete.getIntValue() + " or a.state_task = " + 
			TaskInstanceStatus.Suspended.getIntValue() + ")";
			//sql += " and priority='0' ";
		} else if(TaskInstanceStatus.All.getIntValue()==taskState){
			/**�������е�*/
		}else{
				sql += " and (a.state_task =" + taskState + ") ";
			}
			
		
		//�Ѱ������
		if(null != bizObject){
			sql += " and a.pk_bizobject = '"+ bizObject +"'";
		}
		if(null != keyWord){
			sql += " and (a.activity_name like '%" + keyWord + "%'" + " or a.title like '%" + keyWord + "%')";
			sql += " and (a.activity_name like '%" + keyWord + "%'" 
		        + " or a.title like '%" + keyWord + "%'" 
		        //+ " or a.flowtypename like '%" + keyWord + "%'" 
		        + " or a.form_no like '%" + keyWord + "%')";
		}
		if(null != wherePart){
			sql += wherePart;
		}
		if(isGetMyStartTask)
		{
			sql += " and b.pk_starter = '" + pk_user +"'";
		}
//		sql += "  order by a.startdate desc";
		WfBaseDAO dao = new WfBaseDAO();
		try {
			if(null != page && page.getPageSize()>0){
				PaginationInfo pageinfo = new PaginationInfo();//�����̨��ҳ���ҵ�"ҳ"
				pageinfo.setPageSize(page.getPageSize());
				pageinfo.setPageIndex(page.getPageNumber()-1);
				String sqlrecords = "select count(*) from ("+sql+")temp";
				Object TotalRecords =  dao.executeQuery(sqlrecords , new ColumnProcessor(1));
				page.setTotalRecords(Long.parseLong( TotalRecords.toString()));
				sql = dao.creatQuerySqlBypage(sql, pageinfo);
			}
			logger.info("��ѯtaskvo��sql ��䣺"+sql);
			List<TaskInstanceVO> taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
			if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
				return null;
			}
			return taskInstanceVos;
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
		
	}
	/**
	 * ������������sql������taskInstanceVo
	 * @author zhailzh
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstanceVO> getTaskInsVoBySql(String sqlCondition, PaginationInfo page)  {
		WfBaseDAO dao = new WfBaseDAO();
		List<TaskInstanceVO> taskInstanceVos = null;
		String sql = dao.creatQuerySqlBypage(sqlCondition, page);//�����ҳ����sql���
		try {
			taskInstanceVos = (List<TaskInstanceVO>) dao.executeQuery(sql, new BeanListProcessor(TaskInstanceVO.class));
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (taskInstanceVos == null || taskInstanceVos.size() == 0) {
			return null;
		}
		return taskInstanceVos;
	}	
}
