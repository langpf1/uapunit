package uap.workflow.pub.app.message.vo;

/**
 * �û������ɫ����
 * 
 * @author ������ 2001-6-22 9:01:08
 * @modifier leijun 2006-3-28 NC50�����飬��Ϊ��ɫ
 * @modifier leijun 2007-9-12 ���ڽ�ɫ���Է�������θ���˾�����Ա�����Ϲ�˾��Ϣ
 */
public class UserNameObject implements java.io.Serializable {

	/** �û������ɫ�� */
	private String name = null;

	/** �û������ɫ���� */
	private String pk = null;

	/** �û������ɫ���� */
	private String code = null;

	/** �û������ɫ���Ƿ���Ч */
	private boolean isAvailable = true;

	/** �Ƿ�Ϊ��ɫ */
	private boolean isRole = false;

	/** �û����ɫ�Ĺ�˾ */
	private String pkcorp;

	/**
	 * UserNameObject ������ע�⡣
	 */
	public UserNameObject(String userName) {
		super();
		this.name = userName;
	}

	/**
	 * UserNameObject ������ע�⡣
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
	 * �����û��� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserCode() {
		return code;
	}

	/**
	 * �����û��� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserName() {
		return name;
	}

	/**
	 * �����û������� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserPK() {
		return pk;
	}

	/**
	 * �����û����Ƿ���Ч �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @return boolean
	 */
	public boolean isAvaiable() {
		return isAvailable;
	}

	/**
	 * �Ƿ�Ϊ��ɫ �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @deprecated since V5. replaced by isRole
	 * @return boolean
	 */
	public boolean isUserGroup() {
		return isRole;
	}

	/**
	 * �����û����Ƿ���Ч �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @param newIsAvaiable boolean
	 */
	public void setAvaiable(boolean newIsAvaiable) {
		isAvailable = newIsAvaiable;
	}

	/**
	 * �����û����� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @param newUserCode java.lang.String
	 */
	public void setUserCode(java.lang.String newUserCode) {
		code = newUserCode;
	}

	/**
	 * �Ƿ�Ϊ��ɫ
	 * 
	 * @param newIsUserGroup
	 * @deprecated NC50 ����֧���û��飿
	 * @deprecated since V5. replaced by isRole
	 */
	public void setUserGroup(boolean newIsUserGroup) {
		isRole = newIsUserGroup;
	}

	/**
	 * �����û��� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @param newUserName java.lang.String
	 */
	public void setUserName(java.lang.String newUserName) {
		name = newUserName;
	}

	/**
	 * �����û����� �������ڣ�(2001-6-22 9:03:27)
	 * 
	 * @param newUserPK java.lang.String
	 */
	public void setUserPK(java.lang.String newUserPK) {
		pk = newUserPK;
	}

	/**
	 * �����û��� �������ڣ�(2001-6-22 9:29:24)
	 * 
	 * @return java.lang.String
	 */
	public String toString() {
		if (isRole())
			return getUserName();
		else
			return getUserCode() + "  " + getUserName();
	}

	// ----����Ϊ�µĽӿ�
	/**
	 * �����Ƿ�Ϊ��ɫ
	 */
	public boolean isRole() {
		return isRole;
	}

	/**
	 * �Ƿ�Ϊ��ɫ
	 */
	public void setRole(boolean isrole) {
		isRole = isrole;
	}

}