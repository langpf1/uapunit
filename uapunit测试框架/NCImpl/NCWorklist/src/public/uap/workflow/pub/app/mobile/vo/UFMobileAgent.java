package uap.workflow.pub.app.mobile.vo;

/**
 * ����UFMobile��Ҫ�Ĵ��������
 * 
 * @author leijun 2006-9-11
 */
public class UFMobileAgent {
	/**
	 * �Ƿ���Ҫʹ�ô���
	 */
	public boolean isNeeded;

	/**
	 * ���������IP
	 */
	public String ip;

	/**
	 * ����������˿�
	 */
	public String port;

	/**
	 * �û�
	 */
	public String agentuser;

	/**
	 * ����
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
