package uap.workflow.pub.app.mobile.vo;

/**
 * UFMobile SDK 2.0�������������Ϣ
 * 
 * @author leijun 2006-9-11
 */
public class UFMobileConfig {
	/**
	 * UFMobile�Ķ��ŷ���URL
	 */
	public String url;

	/**
	 * UFMobile�Ķ��ŷ��͹�˾ID
	 */
	public String companyid;

	/**
	 * UFMobile�Ķ��ŷ����û�
	 */
	public String user;

	/**
	 * UFMobile�Ķ��ŷ����û�����
	 */
	public String password;

	/**
	 * ����UFMobile����Ĵ��������
	 */
	public UFMobileAgent agent;

	public UFMobileAgent getAgent() {
		return agent;
	}

	public void setAgent(UFMobileAgent agent) {
		this.agent = agent;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
