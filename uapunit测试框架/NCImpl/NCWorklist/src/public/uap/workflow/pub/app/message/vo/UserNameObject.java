package uap.workflow.pub.app.message.vo;

/**
 * 用户名或角色对象
 * 
 * @author 陈新宇 2001-6-22 9:01:08
 * @modifier leijun 2006-3-28 NC50废弃组，改为角色
 * @modifier leijun 2007-9-12 由于角色可以分配或信任给公司，所以必须带上公司信息
 */
public class UserNameObject implements java.io.Serializable {

	/** 用户名或角色名 */
	private String name = null;

	/** 用户名或角色主键 */
	private String pk = null;

	/** 用户名或角色编码 */
	private String code = null;

	/** 用户名或角色名是否有效 */
	private boolean isAvailable = true;

	/** 是否为角色 */
	private boolean isRole = false;

	/** 用户或角色的公司 */
	private String pkcorp;

	/**
	 * UserNameObject 构造子注解。
	 */
	public UserNameObject(String userName) {
		super();
		this.name = userName;
	}

	/**
	 * UserNameObject 构造子注解。
	 */
	public UserNameObject(String userName, boolean isAvailable) {
		super();
		this.name = userName;
		this.isAvailable = isAvailable;
	}

	public String getPkcorp() {
		return pkcorp;
	}

	public void setPkcorp(String pkcorp) {
		this.pkcorp = pkcorp;
	}

	/**
	 * 返回用户组 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserCode() {
		return code;
	}

	/**
	 * 返回用户名 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserName() {
		return name;
	}

	/**
	 * 返回用户组主键 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserPK() {
		return pk;
	}

	/**
	 * 返回用户名是否有效 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @return boolean
	 */
	public boolean isAvaiable() {
		return isAvailable;
	}

	/**
	 * 是否为角色 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @deprecated since V5. replaced by isRole
	 * @return boolean
	 */
	public boolean isUserGroup() {
		return isRole;
	}

	/**
	 * 设置用户名是否有效 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @param newIsAvaiable boolean
	 */
	public void setAvaiable(boolean newIsAvaiable) {
		isAvailable = newIsAvaiable;
	}

	/**
	 * 设置用户编码 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @param newUserCode java.lang.String
	 */
	public void setUserCode(java.lang.String newUserCode) {
		code = newUserCode;
	}

	/**
	 * 是否为角色
	 * 
	 * @param newIsUserGroup
	 * @deprecated NC50 不再支持用户组？
	 * @deprecated since V5. replaced by isRole
	 */
	public void setUserGroup(boolean newIsUserGroup) {
		isRole = newIsUserGroup;
	}

	/**
	 * 设置用户名 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @param newUserName java.lang.String
	 */
	public void setUserName(java.lang.String newUserName) {
		name = newUserName;
	}

	/**
	 * 设置用户主键 创建日期：(2001-6-22 9:03:27)
	 * 
	 * @param newUserPK java.lang.String
	 */
	public void setUserPK(java.lang.String newUserPK) {
		pk = newUserPK;
	}

	/**
	 * 返回用户名 创建日期：(2001-6-22 9:29:24)
	 * 
	 * @return java.lang.String
	 */
	public String toString() {
		if (isRole())
			return getUserName();
		else
			return getUserCode() + "  " + getUserName();
	}

	// ----以下为新的接口
	/**
	 * 设置是否为角色
	 */
	public boolean isRole() {
		return isRole;
	}

	/**
	 * 是否为角色
	 */
	public void setRole(boolean isrole) {
		isRole = isrole;
	}

}