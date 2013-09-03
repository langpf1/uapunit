package uap.workflow.engine.itf;
import java.util.Date;
import uap.workflow.engine.vos.JobVO;
public interface IJobQry {
	JobVO[] selectNextJobsToExecute();
	JobVO getJobByPk(String jobPk);
	JobVO[] findUnlockedTimersByDuedate(Date duedate);
	JobVO[] findExclusiveJobsToExecute(String processInstanceId);
}
