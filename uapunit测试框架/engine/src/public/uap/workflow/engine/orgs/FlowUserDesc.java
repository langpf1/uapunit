package uap.workflow.engine.orgs;
public class FlowUserDesc {
	private String pk_flowuser;
	private String name;
	private String pk_dept;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPk_dept() {
		return pk_dept;
	}
	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}
	public String getPk_flowuser() {
		return pk_flowuser;
	}
	public void setPk_flowuser(String pk_flowuser) {
		this.pk_flowuser = pk_flowuser;
	}
}
