package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
/**
 * 
 * @author tianchw
 * 
 */
public class ProcessInstanceVO extends SuperVO {
	private static final long serialVersionUID = -4949806430145594423L;
	/**
	 * 流程定义Pk
	 */
	private String pk_prodef;
	/**
	 * 流程定义ID
	 */
	private String prodef_id;
	/**
	 * 流程实例Pk
	 */
	private String pk_proins;
	/**
	 * 父流程实例
	 */
	private String pk_parent;
	/**
	 * 流程实例状态
	 */
	private int state_proins;
	/**
	 * 启动当前子流程实例的上级流程中对应的execution
	 */
	private String pk_super;
	
	/**
	 * 启动流程实例的任务
	 */
	private String pk_starttask;
	
	/**
	 * 流程启动者
	 */
	private String pk_starter;
	/**
	 * 启动时间
	 */
	private UFDateTime startdate;
	/**
	 * 结束时间
	 */
	private UFDateTime enddate;
	/**
	 * 应完成时间
	 */
	private UFDateTime duedate;
	/**
	 * 集团
	 */
	private String pk_group;
	/**
	 * 组织
	 */
	private String pk_org;
	/**
	 * 标题
	 */
	private String title;
	private byte[] processxml;
	private byte[] diagramimg;
	/**
	 * 单据Pk
	 */
	private String pk_form_ins;
	/**
	 * 单据版本Pk
	 */
	private String pk_form_ins_version;
	private String pk_bizobject;// 抽象于NC的单据类型
	private String pk_biztrans;// 抽象于NC的交易类型
	public String supervisor;//流程监控人
	public Integer type;//ProcessInsSupervisorType

	public byte[] getDiagramimg() {
		return diagramimg;
	}
	
	public void setDiagramimg(byte[] diagramimg) {
		this.diagramimg = diagramimg;
	}
	public byte[] getProcessxml() {
		return processxml;
	}
	public void setProcessxml(byte[] processxml) {
		this.processxml = processxml;
	}
	public String getProcessStr() {
		byte[] btyes = this.getProcessxml();
		if (btyes == null || btyes.length == 0) {
			return null;
		}
		return new String(getProcessxml());
	}
	public void setProcessStr(String processxml) {
		if (processxml != null)
			setProcessxml(processxml.getBytes());
	}
	private Integer dr = 0;
	private UFDateTime ts;
	public String getPk_prodef() {
		return pk_prodef;
	}
	public void setPk_prodef(String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}
	public String getProdef_id() {
		return prodef_id;
	}
	public void setProdef_id(String prodef_id) {
		this.prodef_id = prodef_id;
	}
	public String getPk_proins() {
		return pk_proins;
	}
	public void setPk_proins(String pk_proins) {
		this.pk_proins = pk_proins;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public int getState_proins() {
		return state_proins;
	}
	public void setState_proins(int state_proins) {
		this.state_proins = state_proins;
	}
	public String getPk_super() {
		return pk_super;
	}
	public void setPk_super(String pk_super) {
		this.pk_super = pk_super;
	}
	public String getPk_starttask() {
		return pk_starttask;
	}
	public void setPk_starttask(String pk_starttask) {
		this.pk_starttask = pk_starttask;
	}
	public String getPk_starter() {
		return pk_starter;
	}
	public void setPk_starter(String pk_starter) {
		this.pk_starter = pk_starter;
	}
	public String getPk_form_ins_version() {
		return pk_form_ins_version;
	}
	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}
	public UFDateTime getStartdate() {
		return startdate;
	}
	public void setStartdate(UFDateTime startdate) {
		this.startdate = startdate;
	}
	public UFDateTime getEnddate() {
		return enddate;
	}
	public void setEnddate(UFDateTime enddate) {
		this.enddate = enddate;
	}
	public UFDateTime getDuedate() {
		return duedate;
	}
	public void setDuedate(UFDateTime duedate) {
		this.duedate = duedate;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		return "pk_proins";
	}
	@Override
	public String getTableName() {
		return "wf_proins";
	}
	public String getPk_form_ins() {
		return pk_form_ins;
	}
	public void setPk_form_ins(String pk_form_ins) {
		this.pk_form_ins = pk_form_ins;
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

	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
