package uap.workflow.engine.itf;
import java.sql.SQLException;
import java.util.List;

import nc.jdbc.framework.exception.DbException;
import nc.uap.lfw.core.data.PaginationInfo;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.restlet.application.Pagination;
public interface ITaskInstanceQry {
	TaskInstanceVO getTaskInsVoByPk(String taskInsPk);
	List<TaskInstanceVO> getTaskByProcessInstancePk(String processInstancePk);
	List<TaskInstanceVO> getTaskByActivityInstancePk(String activityInstancePk);
	List<TaskInstanceVO> getSubTaskInsByTaskPk(String taskInsPk);
	List<TaskInstanceVO> getTasksByActivitiPk(String activitiPk);
	List<TaskInstanceVO> getTasksByFormInstancePk(String formInstancePk);
	List<TaskInstanceVO> getFinishedTasks(String billType, String billId, String checkman);
	List<TaskInstanceVO> getSubTaskInsBySuperPk(String superTaskPk);
	List<TaskInstanceVO> getSubTaskInsBySuperPkAndMaxBeforeAddSignTime(String superPk, String times);
	List<TaskInstanceVO> getTasks(int taskState,String keyWord, String bizObject, String pk_user, String wherePart, boolean isGetMyStartTask, Pagination page);
	List<TaskInstanceVO> getTaskInsVoBySql(String sql, PaginationInfo page);
}
