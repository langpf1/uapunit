package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
public class JobVO extends SuperVO {
	private static final long serialVersionUID = -1808056234250708842L;
	private String pk_job;
	private String lockowner;
	private String pk_execution;
	private String pk_processInstance;
	private String jobhandlertype;
	private String jobhandlerconfiguration;
	private String exceptionbytearrayId;
	private String exceptionmessage;
	private UFDateTime duedate;
	private UFDateTime lockexpirationtime;
	private UFBoolean isexclusive;
	private int retries;
	private String type;
	public UFBoolean getIsexclusive() {
		return isexclusive;
	}
	public void setIsexclusive(UFBoolean isexclusive) {
		this.isexclusive = isexclusive;
	}
	public String getPk_job() {
		return pk_job;
	}
	public void setPk_job(String pk_job) {
		this.pk_job = pk_job;
	}
	public String getLockowner() {
		return lockowner;
	}
	public void setLockowner(String lockowner) {
		this.lockowner = lockowner;
	}
	public String getPk_execution() {
		return pk_execution;
	}
	public void setPk_execution(String pk_execution) {
		this.pk_execution = pk_execution;
	}
	public String getPk_processInstance() {
		return pk_processInstance;
	}
	public void setPk_processInstance(String pk_processInstance) {
		this.pk_processInstance = pk_processInstance;
	}
	public String getJobhandlertype() {
		return jobhandlertype;
	}
	public void setJobhandlertype(String jobhandlertype) {
		this.jobhandlertype = jobhandlertype;
	}
	public String getJobhandlerconfiguration() {
		return jobhandlerconfiguration;
	}
	public void setJobhandlerconfiguration(String jobhandlerconfiguration) {
		this.jobhandlerconfiguration = jobhandlerconfiguration;
	}
	public String getExceptionbytearrayId() {
		return exceptionbytearrayId;
	}
	public void setExceptionbytearrayId(String exceptionbytearrayId) {
		this.exceptionbytearrayId = exceptionbytearrayId;
	}
	public String getExceptionmessage() {
		return exceptionmessage;
	}
	public void setExceptionmessage(String exceptionmessage) {
		this.exceptionmessage = exceptionmessage;
	}
	public UFDateTime getDuedate() {
		return duedate;
	}
	public void setDuedate(UFDateTime duedate) {
		this.duedate = duedate;
	}
	public UFDateTime getLockexpirationtime() {
		return lockexpirationtime;
	}
	public void setLockexpirationtime(UFDateTime lockexpirationtime) {
		this.lockexpirationtime = lockexpirationtime;
	}
	@Override
	public String getPKFieldName() {
		return "pk_job";
	}
	@Override
	public String getTableName() {
		return "wf_job";
	}
	public int getRetries() {
		return retries;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
