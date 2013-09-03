package uap.workflow.pub.app.mobile.vo;

/**
 * UFMobile SDK 2.0的所需的配置信息
 * 
 * @author leijun 2006-9-11
 */
public class UFMobileConfig {
	/**
	 * UFMobile的短信发送URL
	 */
	public String url;

	/**
	 * UFMobile的短信发送公司ID
	 */
	public String companyid;

	/**
	 * UFMobile的短信发送用户
	 */
	public String user;

	/**
	 * UFMobile的短信发送用户密码
	 */
	public String password;

	/**
	 * 访问UFMobile所需的代理服务器
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
