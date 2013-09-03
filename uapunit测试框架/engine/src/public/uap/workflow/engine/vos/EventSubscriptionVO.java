package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
public class EventSubscriptionVO extends SuperVO {
	private static final long serialVersionUID = 4132905935803637616L;
	private String eventtype;
	private String eventname;
	private String pk_execution;
	private String pk_processInstance;
	private String activity_id;
	private String configuration;
	private UFDateTime created;
	private Integer dr = 0;
	private UFDateTime ts;
	private String pk_subscription;

	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
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
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getConfiguration() {
		return configuration;
	}
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
	public UFDateTime getCreated() {
		return created;
	}
	public void setCreated(UFDateTime created) {
		this.created = created;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getPk_subscription() {
		return pk_subscription;
	}
	public void setPk_subscription(String pk_subscription) {
		this.pk_subscription = pk_subscription;
	}
	@Override
	public String getPKFieldName() {
		return "pk_subscription";
	}
	@Override
	public String getTableName() {
		return "wf_subscription";
	}
}
