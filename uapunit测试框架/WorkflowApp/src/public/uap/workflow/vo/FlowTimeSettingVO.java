package uap.workflow.vo;

import nc.vo.pub.SuperVO;

/**
 * 保存流程实例和流程环节的时限设置
 * @author yanke1
 *
 */
public class FlowTimeSettingVO extends SuperVO {

	public static final int TYPE_INSTANCE = 0;
	public static final int TYPE_ACTIVITY = 1;
	
	private String pk_wf_ist;
	
	
	/** 无论是主流程还是子流程，这里都保存一下主流程的pk */
	private String mainPk_wf_instance;
	
	/** 真正的流程pk */
	private String pk_wf_instance;
	
	/** timeSetting的类型，是流程期限还是工作项期限 */
	private Integer type;
	
	private String activitydefid;
	private Integer timelimit;
	private Integer timeremind;
	
	/** 非数据库字段 */
	private String activityname;
	
	
	public String getPk_wf_ist() {
		return pk_wf_ist;
	}

	public void setPk_wf_ist(String pk_wf_ist) {
		this.pk_wf_ist = pk_wf_ist;
	}

	public String getMainPk_wf_instance() {
		return mainPk_wf_instance;
	}

	public void setMainPk_wf_instance(String mainPk_wf_instance) {
		this.mainPk_wf_instance = mainPk_wf_instance;
	}

	public String getPk_wf_instance() {
		return pk_wf_instance;
	}

	public void setPk_wf_instance(String pk_wf_instance) {
		this.pk_wf_instance = pk_wf_instance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getActivitydefid() {
		return activitydefid;
	}

	public void setActivitydefid(String activitydefid) {
		this.activitydefid = activitydefid;
	}

	public Integer getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(Integer timelimit) {
		this.timelimit = timelimit;
	}

	public Integer getTimeremind() {
		return timeremind;
	}

	public void setTimeremind(Integer timeremind) {
		this.timeremind = timeremind;
	}

	@Override
	public String getPrimaryKey() {
		return pk_wf_ist;
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_wf_ist";
	}
	
	@Override
	public String getTableName() {
		return "pub_wf_ist";
	}
	
}
