package uap.workflow.engine.vos;
import java.io.UnsupportedEncodingException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * 
 * @author tianchw
 * 
 */
public class ProcessDefinitionVO extends SuperVO {
	private static final long serialVersionUID = -3161435217395168965L;
	private java.lang.String prodef_id;
	private java.lang.String pk_prodef;
	private java.lang.String prodef_desc;
	private java.lang.String prodef_name;
	private java.lang.String serverclass;
	private java.lang.String prodef_version;
	private byte[] processxml;
	private byte[] diagramimg;
	private UFBoolean ispublic;
	private java.lang.String pk_group;
	private String pk_org;
	private String pk_bizobject;// 抽象于NC的单据类型
	private String pk_biztrans;// 抽象于NC的交易类型
	private java.lang.Integer dr;
	private nc.vo.pub.lang.UFDateTime ts;
	private java.lang.Integer validity;
	private String pk_prodefgroup;  //指定流程所属类型
	private String time;//流程创建时间，字符串
	public String getPk_prodefgroup() {
		return pk_prodefgroup;
	}
	
	public void setPk_prodefgroup(String pk_prodefgroup) {
		this.pk_prodefgroup = pk_prodefgroup;
	}
	public java.lang.String getProdef_id() {
		return prodef_id;
	}
	public void setProdef_id(java.lang.String prodef_id) {
		this.prodef_id = prodef_id;
	}
	public java.lang.String getPk_prodef() {
		return pk_prodef;
	}
	public void setPk_prodef(java.lang.String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}
	public java.lang.String getProdef_desc() {
		return prodef_desc;
	}
	public void setProdef_desc(java.lang.String prodef_desc) {
		this.prodef_desc = prodef_desc;
	}
	public java.lang.String getProdef_name() {
		return prodef_name;
	}
	public void setProdef_name(java.lang.String prodef_name) {
		this.prodef_name = prodef_name;
	}
	public java.lang.String getPk_group() {
		return pk_group;
	}
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public byte[] getProcessxml() {
		return processxml;
	}
	public void setProcessxml(byte[] processxml) {
		this.processxml = processxml;
	}
	public java.lang.String getServerclass() {
		return serverclass;
	}
	public void setServerclass(java.lang.String serverclass) {
		this.serverclass = serverclass;
	}
	public java.lang.String getProdef_version() {
		return prodef_version;
	}
	public void setProdef_version(java.lang.String prodef_version) {
		this.prodef_version = prodef_version;
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
	public byte[] getDiagramimg() {
		return diagramimg;
	}
	public void setDiagramimg(byte[] diagramimg) {
		this.diagramimg = diagramimg;
	}
	public String getProcessStr() {
		byte[] btyes = this.getProcessxml();
		if (btyes == null || btyes.length == 0) {
			return null;
		}
		try {
			return new String(getProcessxml(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	public void setProcessStr(String processxml) {
		if (processxml != null)
			setProcessxml(processxml.getBytes());
	}
	public String getPKFieldName() {
		return "pk_prodef";
	}
	public String getTableName() {
		return "wf_prodef";
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
	public java.lang.Integer getValidity() {
		return validity;
	}
	public void setValidity(java.lang.Integer validity) {
		this.validity = validity;
	}
	public UFBoolean getIspublic() {
		return ispublic;
	}
	public void setIspublic(UFBoolean ispublic) {
		this.ispublic = ispublic;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}
}
