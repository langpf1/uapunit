package uap.workflow.engine.itf;
import uap.workflow.engine.vos.JobVO;
public interface IJobBill {
	JobVO asyn(JobVO jobVo);
	void delete(String pk_job);
}
