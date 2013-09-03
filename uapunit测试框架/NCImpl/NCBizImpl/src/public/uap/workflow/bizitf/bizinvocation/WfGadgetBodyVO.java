package uap.workflow.bizitf.bizinvocation;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 工作流组件参数VO，与表pub_wfgadget_b对应
 * 
 * @author leijun 2009-5
 */
public class WfGadgetBodyVO extends SuperVO {

	private static final long serialVersionUID = -599473873868408301L;

	private String pk_wfgadget_b;

	private String pk_wfgadget;

	private String varcode;

	private String varname;

	private String vartype;

	private String refname;

	private String defaultvalue;

	private UFBoolean ismulti;

	//========非数据库字段========
	private String value;
	
	private String displayvalue;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPk_wfgadget_b() {
		return pk_wfgadget_b;
	}

	public void setPk_wfgadget_b(String pk_wfgadget_b) {
		this.pk_wfgadget_b = pk_wfgadget_b;
	}

	public String getPk_wfgadget() {
		return pk_wfgadget;
	}

	public void setPk_wfgadget(String pk_wfgadget) {
		this.pk_wfgadget = pk_wfgadget;
	}

	public String getVarcode() {
		return varcode;
	}

	public void setVarcode(String varcode) {
		this.varcode = varcode;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getVartype() {
		return vartype;
	}

	public void setVartype(String vartype) {
		this.vartype = vartype;
	}

	public String getRefname() {
		return refname;
	}

	public void setRefname(String refname) {
		this.refname = refname;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	@Override
	public String getPKFieldName() {
		return "pk_wfgadget_b";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "pub_wfgadget_b";
	}

	public void setIsmulti(UFBoolean ismulti) {
		this.ismulti = ismulti;
	}

	public UFBoolean getIsmulti() {
		return ismulti;
	}

	public void setDisplayvalue(String displayvalue) {
		this.displayvalue = displayvalue;
	}

	public String getDisplayvalue() {
		return displayvalue;
	}

}
