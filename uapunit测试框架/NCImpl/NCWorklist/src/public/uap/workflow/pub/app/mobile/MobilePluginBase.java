package uap.workflow.pub.app.mobile;

/**
 * 短信业务处理基类
 * <li>由各业务组扩展，并注册到文件/ierp/bin/mobileplugin.xml中
 * 
 * @author leijun 2006-2-14
 */
public abstract class MobilePluginBase {
	// 数据源信息直接绑定到Servlet线程
	// private String datasource;
	/**
	 * 是否可删除会话SID
	 */
	private boolean deleteSid = false;

	/**
	 * 指令串
	 */
	private String command;

	/**
	 * 手机号对应的人员档案（基本档案）PK，即业务员PK
	 */
	private String pkPsndoc;

	/**
	 * 业务员对应的用户PK
	 */
	private String pkUser;

	/**
	 * 登录的公司PK
	 */
	private String pkCorp;

	/**
	 * 登录日期
	 */
	private String loginDate;

	/**
	 * 参数数组
	 */
	private String[] params;

	/**
	 * 获得手机用户输入的业务处理指令
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * 获得手机用户输入的业务处理日期，格式由业务插件决定
	 * @return
	 */
	public String getLoginDate() {
		return loginDate;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param loginDate
	 */
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * 获得手机用户输入的业务处理公司PK
	 * @return
	 */
	public String getPkCorp() {
		return pkCorp;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param pkCorp
	 */
	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	/**
	 * 获得手机用户对应的NC系统人员档案（管理档案）PK
	 * @return
	 */
	public String getPkPsndoc() {
		return pkPsndoc;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param pkPsndoc
	 */
	public void setPkPsndoc(String pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}

	/**
	 * 获得手机用户对应的NC用户PK
	 * @return
	 */
	public String getPkUser() {
		return pkUser;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param pkUser
	 */
	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	/**
	 * 获得手机用户输入的业务处理参数
	 * @return 参数串数组
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * 短信平台预置，非产品组调用
	 * @param params
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * 该插件是否需要校验用户输入的密码，默认为需要
	 * <li>各个业务插件可重载决定是否需要，比如查询密码的插件就不需要
	 * 
	 * @return
	 */
	public boolean isNeedValidatePwd() {
		return true;
	}

	/**
	 * 是否应该清除与UFMobile的会话SID
	 * <li>如果处理完毕，由插件决定是否清除pub_mobiledata表中的SID
	 * @return
	 */
	public boolean shouldDeleteSid() {
		return false;
	}

	/**
	 * 业务插件处理接收到的短信
	 * <li>由业务插件扩展实现
	 * <li>业务插件可通过该类的get方法来获取手机用户的各种信息
	 * 
	 * @return 如果非空，则反馈给手机；否则不发反馈短信
	 */
	public abstract String dealMobileMsg();

	public boolean isDeleteSid() {
		return deleteSid;
	}

	public void setDeleteSid(boolean deleteSid) {
		this.deleteSid = deleteSid;
	}
}
