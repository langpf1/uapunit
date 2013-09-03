package uap.workflow.pub.app.mobile;

/**
 * ����ҵ�������
 * <li>�ɸ�ҵ������չ����ע�ᵽ�ļ�/ierp/bin/mobileplugin.xml��
 * 
 * @author leijun 2006-2-14
 */
public abstract class MobilePluginBase {
	// ����Դ��Ϣֱ�Ӱ󶨵�Servlet�߳�
	// private String datasource;
	/**
	 * �Ƿ��ɾ���ỰSID
	 */
	private boolean deleteSid = false;

	/**
	 * ָ�
	 */
	private String command;

	/**
	 * �ֻ��Ŷ�Ӧ����Ա����������������PK����ҵ��ԱPK
	 */
	private String pkPsndoc;

	/**
	 * ҵ��Ա��Ӧ���û�PK
	 */
	private String pkUser;

	/**
	 * ��¼�Ĺ�˾PK
	 */
	private String pkCorp;

	/**
	 * ��¼����
	 */
	private String loginDate;

	/**
	 * ��������
	 */
	private String[] params;

	/**
	 * ����ֻ��û������ҵ����ָ��
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * ����ֻ��û������ҵ�������ڣ���ʽ��ҵ��������
	 * @return
	 */
	public String getLoginDate() {
		return loginDate;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param loginDate
	 */
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * ����ֻ��û������ҵ����˾PK
	 * @return
	 */
	public String getPkCorp() {
		return pkCorp;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param pkCorp
	 */
	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	/**
	 * ����ֻ��û���Ӧ��NCϵͳ��Ա��������������PK
	 * @return
	 */
	public String getPkPsndoc() {
		return pkPsndoc;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param pkPsndoc
	 */
	public void setPkPsndoc(String pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}

	/**
	 * ����ֻ��û���Ӧ��NC�û�PK
	 * @return
	 */
	public String getPkUser() {
		return pkUser;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param pkUser
	 */
	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	/**
	 * ����ֻ��û������ҵ�������
	 * @return ����������
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * ����ƽ̨Ԥ�ã��ǲ�Ʒ�����
	 * @param params
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * �ò���Ƿ���ҪУ���û���������룬Ĭ��Ϊ��Ҫ
	 * <li>����ҵ���������ؾ����Ƿ���Ҫ�������ѯ����Ĳ���Ͳ���Ҫ
	 * 
	 * @return
	 */
	public boolean isNeedValidatePwd() {
		return true;
	}

	/**
	 * �Ƿ�Ӧ�������UFMobile�ĻỰSID
	 * <li>���������ϣ��ɲ�������Ƿ����pub_mobiledata���е�SID
	 * @return
	 */
	public boolean shouldDeleteSid() {
		return false;
	}

	/**
	 * ҵ����������յ��Ķ���
	 * <li>��ҵ������չʵ��
	 * <li>ҵ������ͨ�������get��������ȡ�ֻ��û��ĸ�����Ϣ
	 * 
	 * @return ����ǿգ��������ֻ������򲻷���������
	 */
	public abstract String dealMobileMsg();

	public boolean isDeleteSid() {
		return deleteSid;
	}

	public void setDeleteSid(boolean deleteSid) {
		this.deleteSid = deleteSid;
	}
}
