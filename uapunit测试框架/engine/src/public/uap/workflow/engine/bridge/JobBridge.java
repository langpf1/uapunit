package uap.workflow.engine.bridge;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.entity.MessageEntity;
import uap.workflow.engine.entity.TimerEntity;
import uap.workflow.engine.utils.DateUtil;
import uap.workflow.engine.vos.JobVO;
public class JobBridge implements IBridge<JobVO, JobEntity> {
	@Override
	public JobVO convertT2M(JobEntity object) {
		JobVO vo = new JobVO();
		vo.setDuedate(new UFDateTime(object.getDuedate()));
		vo.setJobhandlertype(object.getJobHandlerType());
		vo.setJobhandlerconfiguration(object.getJobHandlerConfiguration());
		if(object.getLockExpirationTime()!=null){
			vo.setLockexpirationtime(new UFDateTime(object.getLockExpirationTime()));
		}
		vo.setLockowner(object.getLockOwner());
		vo.setPk_execution(object.getExecutionId());
		vo.setPk_job(object.getId());
		vo.setPk_processInstance(object.getProcessInstanceId());
		vo.setRetries(object.getRetries());
		vo.setIsexclusive(new UFBoolean(object.isExclusive()));
		if (object instanceof TimerEntity) {
			vo.setType("timer");
		}
		if (object instanceof MessageEntity) {
			vo.setType("message");
		}
		return vo;
	}
	@Override
	public JobEntity convertM2T(JobVO object) {
		JobEntity entity = null;
		if (object.getType().equalsIgnoreCase("timer")) {
			entity = new TimerEntity();
		} else {
			entity = new MessageEntity();
		}
		entity.setDuedate(DateUtil.convert(object.getDuedate()));
		entity.setJobHandlerType(object.getJobhandlertype());
		entity.setJobHandlerConfiguration(object.getJobhandlerconfiguration());
		entity.setLockOwner(object.getLockowner());
		entity.setLockExpirationTime(DateUtil.convert(object.getLockexpirationtime()));
		entity.setProcessInstanceId(object.getPk_processInstance());
		entity.setExecutionId(object.getPk_execution());
		entity.setId(object.getPk_job());
		entity.setRetries(object.getRetries());
		entity.setExclusive(object.getIsexclusive().booleanValue());
		return entity;
	}
}
