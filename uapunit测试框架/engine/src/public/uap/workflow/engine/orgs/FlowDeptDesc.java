package uap.workflow.engine.orgs;
public class FlowDeptDesc {
	private String pk_flowdept;
	private String name;
	private String pk_floworg;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPk_flowdept() {
		return pk_flowdept;
	}
	public void setPk_flowdept(String pk_flowdept) {
		this.pk_flowdept = pk_flowdept;
	}
	public String getPk_floworg() {
		return pk_floworg;
	}
	public void setPk_floworg(String pk_floworg) {
		this.pk_floworg = pk_floworg;
	}
}
