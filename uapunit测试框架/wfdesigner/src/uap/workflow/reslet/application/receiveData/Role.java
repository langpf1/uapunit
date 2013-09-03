package uap.workflow.reslet.application.receiveData;

public class Role {
	/**
	 * 角色id
	 */
	private String roleID;	
	/**
	 * 任务被执行的动作
	 */
	private String name;

	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
