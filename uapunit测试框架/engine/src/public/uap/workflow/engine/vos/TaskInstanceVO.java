package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/**
 * 
 * @author tianchw
 * 
 */
public class TaskInstanceVO extends SuperVO {
	private static final long serialVersionUID = -9003023274085367732L;
	/*任务的主键*/
	private String pk_task;

	/*上一个任务的pk*/
	private String pk_parent;
	
	/*当前任务所关联的流程实例pk*/
	private String pk_process_instance;
	
	/*当前任务所关联的流程定义pk*/
	private String pk_process_def;
	
	/*当前任务所关联的流程定义id*/
	private String process_def_id;
	
	/*当前任务所关联的流程定义name*/	
	private String process_def_name;
	
	/*活动实例的pk*/
	private String pk_activity_instance;
	
	/*当前活动的id*/
	private String activity_id;
	
	/*当前活动的上一级活动的pk*/
	private String pk_super;
	/**
	 * 前加签次数
	 */
	private String beforeaddsign_times;
	/*
	 * 活动的name
	 * */
	private String activity_name;
	/**
	 * 任务创建人
	 */
	private String pk_creater;
	/**
	 * 任务责任人
	 */
	private String pk_owner;
	/**
	 * 任务责任人部门
	 */
	private String pk_ownerdept;
	/**
	 * 任务实际执行人
	 */
	private String pk_executer;
	/**
	 * 任务代理人
	 */
	private String pk_agenter;
	/**
	 * 任务相关的签证
	 */
	private String pk_myvisa;
	/*是否执行*/
	private UFBoolean isexec;
	/*任务审批是否通过*/
	private UFBoolean ispass;
	private int state_task;
	/**
	 * 办件情况：待办件，待阅件，已阅件，已办件，退回件，共用TaskInstanceStatus枚举
	 */
	private int handlepiece;
	/**
	 * 完成情况
	 */
	private String finish;
	/**
	 * 完成类型
	 */
	private int finish_type;
	/**
	 * 创建类型
	 */
	private int create_type;
	/**
	 * 动作类型
	 */
	private int action_type;
	/**
	 * 流程的启动时间
	 */
	private UFDateTime startdate;
	/**
	 * 任务创建时间
	 */
	private UFDateTime begindate;
	/**
	 * 任务打开的时间
	 */
	private UFDateTime signdate;
	/**
	 * 任务完成时间
	 */
	private UFDateTime finishdate;
	/**
	 * 任务预计完成时间
	 */
	private UFDateTime dutedate;
	/**
	 * 任务从创建到完成的持续时间
	 */
	private String standingdate;
	private String opinion;
	/**
	 * 传纸条，悄悄话，就是不是正式的意见
	 */
	private String scratch;
	private String pk_group;
	private String pk_org;
	private String ext0;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private String ext8;
	private String ext9;
	/**
	 * 抽象于NC的单据ID
	 */
	private String pk_form_ins;
	/**
	 * 抽象于NC的单据版本ID
	 */
	private String pk_form_ins_version;
	/**
	 * 抽象于NC的单据号
	 */
	private String form_no;
	private String title;
	/**
	 * 抽象于NC的单据类型
	 */
	private String pk_bizobject;
	/**
	 * 抽象于NC的交易类型
	 */
	private String pk_biztrans;
	/**
	 * 动作类型
	 */
	private String actiontype;
	/**
	 * 待办任务打开方式
	 */
	private String openUIStyle;
	private String openURI;
	/**
	 * 任务执行结果
	 */
	private String result;
	
	/** 
	 * 用户对象 
	 */
	private String userobject;
	private Integer dr = 0;
	private UFDateTime ts;
	public String getPk_task() {
		return pk_task;
	}
	public void setPk_task(String pk_task) {
		this.pk_task = pk_task;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getProcess_def_id() {
		return process_def_id;
	}
	public void setProcess_def_id(String prodef_id) {
		this.process_def_id = prodef_id;
	}
	public String getProcess_def_name() {
		return process_def_name;
	}
	public void setProcess_def_name(String prodef_name) {
		this.process_def_name = prodef_name;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getPk_creater() {
		return pk_creater;
	}
	public void setPk_creater(String pk_creater) {
		this.pk_creater = pk_creater;
	}
	public String getPk_owner() {
		return pk_owner;
	}
	public void setPk_owner(String pk_owner) {
		this.pk_owner = pk_owner;
	}
	public String getPk_ownerdept() {
		return pk_ownerdept;
	}
	public void setPk_ownerdept(String pk_ownerdept) {
		this.pk_ownerdept = pk_ownerdept;
	}
	public String getPk_executer() {
		return pk_executer;
	}
	public void setPk_executer(String pk_executer) {
		this.pk_executer = pk_executer;
	}
	public String getPk_agenter() {
		return pk_agenter;
	}
	public void setPk_agenter(String pk_agenter) {
		this.pk_agenter = pk_agenter;
	}
	public String getPk_form_ins() {
		return pk_form_ins;
	}
	public void setPk_form_ins(String pk_formins) {
		this.pk_form_ins = pk_formins;
	}
	public String getPk_form_ins_version() {
		return pk_form_ins_version;
	}
	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}
	public String getPk_process_instance() {
		return pk_process_instance;
	}
	public void setPk_process_instance(String pk_process_instance) {
		this.pk_process_instance = pk_process_instance;
	}
	public String getPk_process_def() {
		return pk_process_def;
	}
	public void setPk_process_def(String pk_process_def) {
		this.pk_process_def = pk_process_def;
	}
	public String getPk_activity_instance() {
		return pk_activity_instance;
	}
	public void setPk_activity_instance(String pk_activity_instance) {
		this.pk_activity_instance = pk_activity_instance;
	}
	public String getForm_no() {
		return form_no;
	}
	public void setForm_no(String form_no) {
		this.form_no = form_no;
	}
	public String getPk_myvisa() {
		return pk_myvisa;
	}
	public void setPk_myvisa(String pk_myvisa) {
		this.pk_myvisa = pk_myvisa;
	}
	public UFBoolean getIsexec() {
		return isexec;
	}
	public void setIsexec(UFBoolean isexec) {
		this.isexec = isexec;
	}
	public UFBoolean getIspass() {
		return ispass;
	}
	public void setIspass(UFBoolean ispass) {
		this.ispass = ispass;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getScratch() {
		return scratch;
	}
	public void setScratch(String scratch) {
		this.scratch = scratch;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getExt0() {
		return ext0;
	}
	public void setExt0(String ext0) {
		this.ext0 = ext0;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt5() {
		return ext5;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	public String getExt6() {
		return ext6;
	}
	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}
	public String getExt7() {
		return ext7;
	}
	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}
	public String getExt8() {
		return ext8;
	}
	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}
	public String getExt9() {
		return ext9;
	}
	public void setExt9(String ext9) {
		this.ext9 = ext9;
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
	@Override
	public String getPKFieldName() {
		return "pk_task";
	}
	@Override
	public String getTableName() {
		return "wf_task";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public UFDateTime getStartdate() {
		return startdate;
	}
	public void setStartdate(UFDateTime startdate) {
		this.startdate = startdate;
	}
	public UFDateTime getBegindate() {
		return begindate;
	}
	public void setBegindate(UFDateTime begindate) {
		this.begindate = begindate;
	}
	public UFDateTime getSigndate() {
		return signdate;
	}
	public void setSigndate(UFDateTime signdate) {
		this.signdate = signdate;
	}
	public UFDateTime getFinishdate() {
		return finishdate;
	}
	public void setFinishdate(UFDateTime finishdate) {
		this.finishdate = finishdate;
	}
	public UFDateTime getDutedate() {
		return dutedate;
	}
	public void setDutedate(UFDateTime dutedate) {
		this.dutedate = dutedate;
	}
	public String getStandingdate() {
		return standingdate;
	}
	public void setStandingdate(String standingdate) {
		this.standingdate = standingdate;
	}
	public int getState_task() {
		return state_task;
	}
	public void setState_task(int state_task) {
		this.state_task = state_task;
	}
	public int getHandlepiece() {
		return handlepiece;
	}
	public void setHandlepiece(int handlepiece) {
		this.handlepiece = handlepiece;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public int getFinish_type() {
		return finish_type;
	}
	public void setFinish_type(int finish_type) {
		this.finish_type = finish_type;
	}
	public int getCreate_type() {
		return create_type;
	}
	public void setCreate_type(int create_type) {
		this.create_type = create_type;
	}
	public int getAction_type() {
		return action_type;
	}
	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getPk_bizobject() {
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public String getPk_biztrans() {
		return pk_biztrans;
	}
	public void setPk_biztrans(String pk_biztrans) {
		this.pk_biztrans = pk_biztrans;
	}
	public String getPk_super() {
		return pk_super;
	}
	public void setPk_super(String pk_super) {
		this.pk_super = pk_super;
	}
	public String getBeforeaddsign_times() {
		return beforeaddsign_times;
	}
	public void setBeforeaddsign_times(String beforeaddsign_times) {
		this.beforeaddsign_times = beforeaddsign_times;
	}
	public String getUserobject() {
		return userobject;
	}
	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}
	public String getOpenUIStyle() {
		return openUIStyle;
	}
	public void setOpenUIStyle(String openUIStyle) {
		this.openUIStyle = openUIStyle;
	}
	public String getOpenURI() {
		return openURI;
	}
	public void setOpenURI(String openURI) {
		this.openURI = openURI;
	}
}
