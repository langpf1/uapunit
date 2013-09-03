package uap.workflow.pub.app.mobile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.excp.MailException;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.IConfigFileService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.CorpVO;
import nc.vo.pub.BusinessException;

import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import uap.workflow.pub.app.mobile.vo.MobileConfig;
import uap.workflow.pub.app.mobile.vo.MobileData;
import uap.workflow.pub.app.mobile.vo.MobileException;
import uap.workflow.pub.app.mobile.vo.MobileImplInfo;
import uap.workflow.pub.app.mobile.vo.MobileOptions;
import uap.workflow.pub.app.mobile.vo.MobilePluginVO;
import uap.workflow.pub.app.mobile.vo.UFMobileAgent;
import uap.workflow.pub.app.mobile.vo.UFMobileConfig;

import com.thoughtworks.xstream.XStream;

/**
 * 短信服务 管理器
 * 
 * @author leijun 2006-2-14
 */
public class MobileHandler {

	private static MobileHandler instance = new MobileHandler();

	/**
	 * 配置文件位置
	 */
	static final String CONFIG_FILE = RuntimeEnv.getInstance().getNCHome()
			+ "/ierp/bin/mobileplugin.xml";

	//static final String CONFIG_FILE = "./mobileplugin.xml";

	/**
	 * 指令与选项之间的分隔符
	 */
	public static final String CMD_DELIMITER = "#";

	/**
	 * 选项之间的分隔符
	 */
	final String OPTIONS_DELIMITER = " ";

	/**
	 * 短信插件、UFMobile等信息的配置
	 */
	private static MobileConfig m_mobileConfig;

	/**
	 * 默认构造方法
	 */
	private MobileHandler() {
		super();
	}

	public static MobileHandler getInstance() {
		return instance;
	}

	/**
	 * 测试用
	 * 
	 * @param args
	 * @deprecated 仅用于测试
	 */
	public static void main(String[] args) {
		Logger.debug("main called");

		MobilePluginVO mp1 = new MobilePluginVO();
		mp1.setClassname("cn.test.asdfasd");
		mp1.setCommands(new String[] { "GZ", "SP" });
		mp1.setModule("gl");

		MobilePluginVO mp2 = new MobilePluginVO();

		mp2.setClassname("cn.test.123451");
		mp2.setCommands(new String[] { "FI", "HI" });
		mp2.setModule("fi");

		MobileImplInfo mii1 = new MobileImplInfo();
		mii1.setActive(true);
		mii1.setQualifiedclass("nc.bs.pub.mobile.UfmobileSmsImpl");
		MobileImplInfo mii2 = new MobileImplInfo();
		mii2.setActive(false);
		mii2.setQualifiedclass("nc.uap.mobile.impl.Bclass");

		MobileConfig mc = new MobileConfig();
		UFMobileConfig umc = new UFMobileConfig();
		UFMobileAgent uma = new UFMobileAgent();
		uma.setAgentpwd("agentpwd");
		uma.setAgentuser("agentuser");
		uma.setIp("127.0.0.1");
		uma.setNeeded(false);
		uma.setPort("808");

		umc.setAgent(uma);
		umc.setCompanyid("495");
		umc.setPassword("ufmobile123456");
		umc.setUrl("http://portal.ufmobile.com.cn/sdkweb/service.asp");
		umc.setUser("admin");

		mc.setUfmobile(umc);
		mc.setDatasource("design");

		mc.setMobileimplinfos(new MobileImplInfo[] { mii1, mii2 });
		mc.setMobilePlugins(new MobilePluginVO[] { mp1, mp2 });

		MobileHandler.saveMobileConfig(mc);
		Object obj = MobileHandler.getMobileConfig();
		Logger.debug(obj);

		// URL
		//mh.sendMessage("1234", "GZ#-u 张宗 -p #$500元 200602");
		//mh.sendMessage("1234", "GZ#-p 1234 200602");

		//CL
		//mh.mobileMsgReceived("124", "SP#HTID -p 111 N 不通过", "sid");

	}

	/**
	 * 从配置文件读出短信业务配置
	 * 
	 * @return
	 */
	public synchronized static MobileConfig getMobileConfig() {
		if (m_mobileConfig != null)
			return m_mobileConfig;

		XStream xs = getAliasXstream();

		FileReader fr = null;
		try {
			fr = new FileReader(CONFIG_FILE);
			m_mobileConfig = (MobileConfig) xs.fromXML(fr);

		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return m_mobileConfig;
	}

	/**
	 * 保存配置
	 * 
	 * @param mc
	 */
	public static void saveMobileConfig(MobileConfig mc) {
		XStream xs = getAliasXstream();

		
		FileWriter fw = null;
		try {
			fw = new FileWriter(CONFIG_FILE);
			xs.toXML(mc, fw);

		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private static XStream getAliasXstream() {
		XStream xs = new XStream();
		xs.alias("mobileconfig", MobileConfig.class);
		xs.alias("mobileplugin", MobilePluginVO.class);
		xs.alias("mobileplugins", MobilePluginVO[].class);
		xs.alias("mobileimplinfos", MobileImplInfo[].class);
		xs.alias("mobileimpl", MobileImplInfo.class);
		return xs;
	}

	/**
	 * NC接收到短信后调用的方法
	 * <li>实例化业务插件，进行业务处理
	 * 
	 * @param phone 手机号
	 * @param msg 正确的消息格式应该是"GZ#-p UserPassword [-u UserCode -c UnitCode -a
	 *          AccountCode -d LoginDate] [Param1 Param2...]"
	 */
	public void mobileMsgReceived(String phone, String msg, String sid) {
		Logger.debug(">>>MobileHandler.mobileMsgReceived(...) called");
		Logger.debug("*********************************************");
		Logger.debug("* phone=" + phone);
		Logger.debug("* msg=" + msg);
		Logger.debug("* pk_sid=" + sid);
		Logger.debug("*********************************************");
		if (phone == null || msg == null)
			return;

		HashMap hmOptionValue = new HashMap();
		String strCmd = "";
		try {
			// 交验消息格式
			strCmd = checkOptions(msg, hmOptionValue);

			//XXX:注册数据源到当前线程
			regDataSourceForServlet(hmOptionValue);

			// 查找业务插件，并做校验
			MobilePluginBase mobileplugin = null;

			mobileplugin = initCurrentPlugin(strCmd, hmOptionValue);

			// /交验用户和登录公司
			checkUserAndCorpByPhone(phone, mobileplugin, hmOptionValue);

			// 业务插件处理短信，如果有返回值，则发送反馈短信
			String retStr = mobileplugin.dealMobileMsg();
			if (mobileplugin.shouldDeleteSid()) {
				//删除会话信息
				try {
					BaseDAO dao = new BaseDAO();
					dao.deleteByClause(MobileData.class, "mobile='" + phone + "' and pk_sid='" + sid + "'");
				} catch (DAOException e) {
					//仅日志异常
					Logger.error(e.getMessage(), e);
				}
			}

			if (retStr != null) {
				sendMessage(phone, retStr);
			}
		} catch (MobileException e) {
			Logger.error(e.getMessage(), e);
			sendMessage(phone, e.getMessage());
			return;
		}

	}

	/**
	 * NC接收到邮件后调用的方法
	 * <li>实例化业务插件，进行业务处理
	 * @param emailAddr
	 * @param subject
	 */
	public void emailMsgReceived(String emailAddr, String subject) {
		Logger.info("========================");
		Logger.info("进入MobileHandler.emailMsgReceived");
		Logger.info("emailAddr：" + emailAddr);
		Logger.info("subject："+subject);
		Logger.info("========================");
		
		HashMap hmOptionValue = new HashMap();
		String strCmd = "";
		try {
			// 交验消息格式
			strCmd = checkOptions(subject, hmOptionValue);

			//XXX:注册数据源到当前线程
			regDataSourceForServlet(hmOptionValue);

			// 查找业务插件，并做校验
			MobilePluginBase mobileplugin = null;

			mobileplugin = initCurrentPlugin(strCmd, hmOptionValue);

			// 交验用户和登录公司
			checkUserAndCorpByEmail(emailAddr, mobileplugin, hmOptionValue);

			// 业务插件处理短信，如果有返回值，则发送反馈短信
			String retStr = mobileplugin.dealMobileMsg();
			if (retStr != null) {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### 来自NC流程平台的邮件通知*/, retStr, new String[] { emailAddr }, null, null);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			try {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### 来自NC流程平台的邮件通知*/, e.getMessage(), new String[] { emailAddr },
						null, null);
			} catch (MailException ex) {
				//XXX:忽略
			}
		}
	}

	/**
	 * 发送短信
	 * @param phone
	 * @param message
	 */
	private void sendMessage(String phone, String message) {
		ShortMessageService sms = WirelessManager.getSMS();
		if (sms == null) {
			Logger.error(">>找不到活动的短信服务实现类，无法发送和接收短信");
			return;
		}
		if (!sms.initialize()) {
			//短信服务初始化失败
			Logger.error(">>短信服务初始化失败");
			return;
		}

		//不需要会话SID
		sms.sendMessage(phone, message);
	}

	/**
	 * 检查消息格式
	 *  
	 * @param strMsg
	 * @param hmOptionValues
	 * @return
	 * @throws MobileException
	 */
	private String checkOptions(String strMsg, HashMap hmOptionValues) throws MobileException {
		Logger.info("进入MobileHandler.checkOptions strMsg: "+strMsg);
		int cmdIndex = strMsg.indexOf(CMD_DELIMITER);
		if (cmdIndex < 1)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000001", null, new String[]{strMsg})/*错误：非法消息格式={0}*/);
		String strCmd = strMsg.substring(0, cmdIndex);
		String strOptions = strMsg.substring(cmdIndex + 1);
		StringTokenizer st = new StringTokenizer(strOptions, OPTIONS_DELIMITER);
		String[] tokens = new String[st.countTokens()];
		int j = 0;
		while (st.hasMoreTokens()) {
			tokens[j] = st.nextToken();
			j++;
		}

		Options _options = new Options().addOption(MobileOptions.OPTION_USERCODE, true, "usercode")
				.addOption(MobileOptions.OPTION_USERPASSWORD, true, "password").addOption(
						MobileOptions.OPTION_ACCOUNT, true, "account").addOption(MobileOptions.OPTION_UNITCODE,
						true, "unitcode").addOption(MobileOptions.OPTION_LOGINDATE, true, "logindate");
		CommandLineParser _parser = new PosixParser();
		Logger.info("进入MobileHandler.checkOptions _options: "+_options);
		Logger.info("进入MobileHandler.checkOptions tokens: " + tokens);
		try {
			CommandLine cl = _parser.parse(_options, tokens);
			hmOptionValues.put(MobileOptions.OPTION_USERPASSWORD, cl
					.getOptionValue(MobileOptions.OPTION_USERPASSWORD));
			hmOptionValues.put(MobileOptions.OPTION_USERCODE, cl
					.getOptionValue(MobileOptions.OPTION_USERCODE));
			hmOptionValues.put(MobileOptions.OPTION_ACCOUNT, cl
					.getOptionValue(MobileOptions.OPTION_ACCOUNT));
			hmOptionValues.put(MobileOptions.OPTION_LOGINDATE, cl
					.getOptionValue(MobileOptions.OPTION_LOGINDATE));
			hmOptionValues.put(MobileOptions.OPTION_UNITCODE, cl
					.getOptionValue(MobileOptions.OPTION_UNITCODE));
			hmOptionValues.put(MobileOptions.ARGS, cl.getArgs());
		} catch (ParseException e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000002")/*错误：非法消息格式*/);
		}

		return strCmd;
	}

	/**
	 * 交验邮件用户的有效性，并赋值给业务插件
	 * <li>1.根据邮件找到人员档案中的业务员（基本档案）；如果找到多个业务员，则反馈警告邮件
	 * <li>2.查找业务员关联的用户；如果找到多个用户，则反馈警告邮件
	 * 
	 * @param emailAddr 邮件地址
	 * @param mpb 业务插件对象
	 * @param hmOptionValue 消息串值对
	 * @throws MobileException
	 */
	private void checkUserAndCorpByEmail(String emailAddr, MobilePluginBase mpb, HashMap hmOptionValue)
			throws MobileException {
		try {
			// 登录公司PK
			String unitCode = (String) hmOptionValue.get(MobileOptions.OPTION_UNITCODE);
			String pkCorp = null;
			if (unitCode != null) {
				//根据公司编码查询出公司VO进而获取其PK
				Collection coCorps = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(
						CorpVO.class, "unitcode='" + unitCode + "'");
				if (coCorps.size() > 0) {
					CorpVO voCorp = (CorpVO) coCorps.iterator().next();
					pkCorp = voCorp.getPrimaryKey();
					mpb.setPkCorp(pkCorp);
				}
			}

			//根据邮件找到人员档案中的基本档案 
			Collection<PsndocVO> psnVOs = new BaseDAO().retrieveByClause(PsndocVO.class, "email ='" + emailAddr + "'");
			checkPsndoc(mpb, hmOptionValue, psnVOs.toArray(new PsndocVO[0]));
		} catch (MobileException e) {
			throw e;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*系统异常，请找管理员*/, e);
		}
	}

	private void checkPsndoc(MobilePluginBase mpb, HashMap hmOptionValue, PsndocVO[] psnVOs)
			throws MobileException, BusinessException {
		if (psnVOs == null || psnVOs.length == 0)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000004")/*错误：找不到对应的人员基本档案，请找管理员*/);

		ArrayList alFoundPsns = new ArrayList();
		ArrayList alFoundUsers = new ArrayList();
		String userCode = (String) hmOptionValue.get(MobileOptions.OPTION_USERCODE);
		if (psnVOs.length > 1) {
			// 1.a如果找到多个基本档案，则找到每个业务员关联的用户
			for (int i = 0; i < psnVOs.length; i++) {
				PsndocVO docVO = psnVOs[i];
				Collection<UserVO> colUser = new BaseDAO().retrieveByClause(UserVO.class, "pk_base_doc ='" + docVO.getPrimaryKey() + "'");
				if (colUser == null) {
					continue;
				}
				for (Iterator<UserVO> iterator = colUser.iterator(); iterator.hasNext();) {
					UserVO userVO = iterator.next();
					if (userVO.getUser_code().equals(userCode) || userCode == null) {
						alFoundUsers.add(userVO);
						if (!alFoundPsns.contains(docVO))
							alFoundPsns.add(docVO);
					}
				}
			}
		} else {
			// 1.b如果找到1个基本档案，则找到业务员关联的用户
			PsndocVO docVO = psnVOs[0];
			alFoundPsns.add(docVO);

			Collection<UserVO> colUser = new BaseDAO().retrieveByClause(UserVO.class, "pk_base_doc ='" + docVO.getPrimaryKey() + "'");
			if (colUser != null) {
				for (Iterator<UserVO> iterator = colUser.iterator(); iterator.hasNext();) {
					UserVO userVO = iterator.next();
					if (userVO.getUser_code().equals(userCode) || userCode == null) {
						alFoundUsers.add(userVO);
					}
				}
			}
		}

		if (alFoundUsers.size() == 0) {
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000005")/*错误：对应的业务员没有在NC系统中关联用户，请找管理员*/);
		} else if (alFoundUsers.size() > 1) {
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000006")/*错误：对应的业务员在NC系统中关联了多个用户，请找管理员*/);
		} else {
			// 2.验证用户密码
			UserVO uVO = (UserVO) alFoundUsers.get(0);
			if (mpb.isNeedValidatePwd()) {
				//XXX:由插件决定是否需要校验密码
				Encode rsa = new Encode();
				String pwd = (String) hmOptionValue.get(MobileOptions.OPTION_USERPASSWORD);

				if (StringUtil.isEmptyWithTrim(pwd))
					throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000007")/*错误：业务插件需要校验密码*/);
				String rsaPwd = rsa.encode(pwd);
				if (!rsaPwd.equals(uVO.getUser_password()))
					throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000008")/*错误：密码不正确*/);
			}

			// 对应的人员档案（基本档案）PK
			mpb.setPkPsndoc(((PsndocVO) alFoundPsns.get(0)).getPrimaryKey());
			// 对应的用户PK
			mpb.setPkUser(uVO.getPrimaryKey());
		}
	}

	/**
	 * 交验手机用户的有效性，并赋值给业务插件
	 * <li>1.根据手机号找到人员档案中的业务员（基本档案）；如果找到多个业务员，则反馈警告短信
	 * <li>2.查找业务员关联的用户；如果找到多个用户，则反馈警告短信
	 * 
	 * @param phone 手机号
	 * @param mpb 业务插件对象
	 * @param hmOptionValue 消息串值对
	 * @throws MobileException
	 */
	private void checkUserAndCorpByPhone(String phone, MobilePluginBase mpb, HashMap hmOptionValue)
			throws MobileException {
		try {
			// /登录公司PK
			String unitCode = (String) hmOptionValue.get(MobileOptions.OPTION_UNITCODE);
			String pkCorp = null;
			if (unitCode != null) {
				//根据公司编码查询出公司VO进而获取其PK
				Collection coCorps = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(
						CorpVO.class, "unitcode='" + unitCode + "'");
				if (coCorps.size() > 0) {
					CorpVO voCorp = (CorpVO) coCorps.iterator().next();
					pkCorp = voCorp.getPrimaryKey();
					mpb.setPkCorp(pkCorp);
				}
			}

			//根据手机号找到人员档案中的基本档案
			Collection<PsndocVO> psnVOs = new BaseDAO().retrieveByClause(PsndocVO.class, "mobile ='" + phone + "'");
			checkPsndoc(mpb, hmOptionValue, psnVOs.toArray(new PsndocVO[0]));
		} catch (MobileException e) {
			throw e;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*系统异常，请找管理员*/, e);
		}
	}

	/**
	 * 每个Servlet都需要注册数据源信息才可调用DMO
	 */
	private void regDataSourceForServlet(HashMap hmOptionValue) throws MobileException {
		String dsName = null;
		String accountCode = (String) hmOptionValue.get(MobileOptions.OPTION_ACCOUNT);
		if (accountCode != null) {
			// 根据帐套编码找到数据源信息
			Account retrAccount = null;
			ConfigParameter config = null;

			try {
				config = getAccountService().getAccountConfigPara();
				Account[] accounts = config.getAryAccounts();
				for (int i = 0; i < (accounts == null ? 0 : accounts.length); i++) {
					if (accountCode.equals(accounts[i].getAccountCode())) {
						retrAccount = accounts[i];
						break;
					}
				}
			} catch (Exception e) {
				// 这里抓住所有异常：包括受查和非受查异常
				Logger.error(e.getMessage(), e);
			}

			if (retrAccount != null)
				dsName = retrAccount.getDataSourceName();
		}
		if (dsName == null) {
			MobileConfig mc = getMobileConfig();
			dsName = mc.getDatasource();
		}

		// 1.校验
		try {
			String[] dataSources = getAccountService().findDatasource();
			if (dataSources == null)
				throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*系统异常，请找管理员*/);
			boolean bValid = Arrays.asList(dataSources).contains(dsName);
			if (!bValid)
				throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000009")/*错误：无效数据源*/);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*系统异常，请找管理员*/);
		}

		// 2.注册
		InvocationInfoProxy.getInstance().setUserDataSource(dsName);
	}

	/**
	 * 获得系统框架的帐套服务
	 * @return
	 */
	private IConfigFileService getAccountService() {
		IConfigFileService name = (IConfigFileService) NCLocator.getInstance().lookup(
				IConfigFileService.class.getName());
		return name;
	}

	/**
	 * 根据指令找到正确的业务处理插件，并初始化一些参数
	 * 
	 * @param command 指令串
	 * @return 与指令对应的业务处理插件实例对象
	 * @throws MobileException
	 */
	private MobilePluginBase initCurrentPlugin(String command, HashMap hmOptionValue)
			throws MobileException {
		MobileConfig mc = getMobileConfig();

		MobilePluginVO selectedPluginVO = null;
		MobilePluginVO[] mps = mc.getMobilePlugins();
		for (int i = 0; i < mps.length; i++) {
			List lComms = Arrays.asList(mps[i].getCommands());
			if (lComms.contains(command)) {
				selectedPluginVO = mps[i];
				break;
			}
		}

		if (selectedPluginVO == null)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000010", null, new String[]{command})/*错误：找不到业务插件={0}*/);

		// 实例化业务插件
		MobilePluginBase mpb = null;

		try {
			mpb = (MobilePluginBase) NewObjectService.newInstance(selectedPluginVO.getModule(),
					selectedPluginVO.getClassname());
		} catch (Exception e) {
			//FIXME::捕获到所有受查或非受查异常？
			Logger.error(e.getMessage(), e);
		}
		if (mpb == null)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000011")/*错误：不能实例化业务插件*/);

		// /指令
		mpb.setCommand(command);
		// /登录日期
		mpb.setLoginDate((String) hmOptionValue.get(MobileOptions.OPTION_LOGINDATE));
		// /参数数组
		mpb.setParams((String[]) hmOptionValue.get(MobileOptions.ARGS));

		return mpb;
	}

}
