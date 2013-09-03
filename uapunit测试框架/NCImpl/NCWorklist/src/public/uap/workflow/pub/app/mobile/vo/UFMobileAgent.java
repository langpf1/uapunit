package uap.workflow.pub.app.mobile.vo;

/**
 * 访问UFMobile需要的代理服务器
 * 
 * @author leijun 2006-9-11
 */
public class UFMobileAgent {
	/**
	 * 是否需要使用代理
	 */
	public boolean isNeeded;

	/**
	 * 代理服务器IP
	 */
	public String ip;

	/**
	 * 代理服务器端口
	 */
	public String port;

	/**
	 * 用户
	 */
	public String agentuser;

	/**
	 * 密码
	 */
	public String agentpwd;

	public String getAgentpwd() {
		return agentpwd;
	}

	public void setAgentpwd(String agentpwd) {
		this.agentpwd = agentpwd;
	}

	public String getAgentuser() {
		return agentuser;
	}

	public void setAgentuser(String agentuser) {
		this.agentuser = agentuser;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isNeeded() {
		return isNeeded;
	}

	public void setNeeded(boolean isNeeded) {
		this.isNeeded = isNeeded;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
