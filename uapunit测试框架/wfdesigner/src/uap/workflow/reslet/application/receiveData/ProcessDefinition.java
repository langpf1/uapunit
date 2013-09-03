package uap.workflow.reslet.application.receiveData;

public class ProcessDefinition {

	/*流程的pk*/
	private String pk_prodef ;
	/*流程创建时间*/
	private String ts ;
	/*流程的状态*/
	private int status ;
	/*流程的id*/
	private String prodef_id ;
	/*流程所关联的单据的类型，例如客户申请单就是 客户申请单的编码"10KH"*/
	private String pk_bizobject;
	/*流程的版本*/
	private String prodef_version;
	/*流程的名称*/
	private String prodef_name;
	public String getPk_prodef() {
		return pk_prodef;
	}
	public void setPk_prodef(String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getProdef_id() {
		return prodef_id;
	}
	public void setProdef_id(String prodef_id) {
		this.prodef_id = prodef_id;
	}
	public String getPk_bizobject() {
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public String getProdef_version() {
		return prodef_version;
	}
	public void setProdef_version(String prodef_version) {
		this.prodef_version = prodef_version;
	}
	public String getProdef_name() {
		return prodef_name;
	}
	public void setProdef_name(String prodef_name) {
		this.prodef_name = prodef_name;
	}
}
