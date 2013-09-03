package uap.workflow.engine.vos;

import nc.vo.pub.SuperVO;

public class ProcessDefGroupVO extends SuperVO {
	
	private static final long serialVersionUID = -4699841991903715136L;

	private String pk_prodefgroup;
	private String pk_parentgroup;
	private String code;
	private String name;
	private java.lang.Integer dr;
	private nc.vo.pub.lang.UFDateTime ts;

	public String getPk_prodefgroup() {
		return pk_prodefgroup;
	}
	public void setPk_prodefgroup(String pk_prodefgroup) {
		this.pk_prodefgroup = pk_prodefgroup;
	}
	public String getPk_parentgroup() {
		return pk_parentgroup;
	}
	public void setPk_parentgroup(String pk_parentgroup) {
		this.pk_parentgroup = pk_parentgroup;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public java.lang.Integer getDr() {
		return dr;
	}
	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}
	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}
	public String getPKFieldName() {
		return "pk_prodefgroup";
	}
	public String getTableName() {
		return "wf_prodefgroup";
	}
}
