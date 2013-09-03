package uap.workflow.engine.context;

import java.io.Serializable;

public class AddSignUserInfoCtx implements Serializable{

	private static final long serialVersionUID = -7963741201724161885L;
	private String userPk;
	private String deptPk;
	private String orgPk;
	private int order;
	
	public String getOrgPk() {
		return orgPk;
	}
	public void setOrgPk(String orgPk) {
		this.orgPk = orgPk;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getUserPk() {
		return userPk;
	}
	public void setUserPk(String userPk) {
		this.userPk = userPk;
	}
	public String getDeptPk() {
		return deptPk;
	}
	public void setDeptPk(String deptPk) {
		this.deptPk = deptPk;
	}
}
