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
 * ���ŷ��� ������
 * 
 * @author leijun 2006-2-14
 */
public class MobileHandler {

	private static MobileHandler instance = new MobileHandler();

	/**
	 * �����ļ�λ��
	 */
	static final String CONFIG_FILE = RuntimeEnv.getInstance().getNCHome()
			+ "/ierp/bin/mobileplugin.xml";

	//static final String CONFIG_FILE = "./mobileplugin.xml";

	/**
	 * ָ����ѡ��֮��ķָ���
	 */
	public static final String CMD_DELIMITER = "#";

	/**
	 * ѡ��֮��ķָ���
	 */
	final String OPTIONS_DELIMITER = " ";

	/**
	 * ���Ų����UFMobile����Ϣ������
	 */
	private static MobileConfig m_mobileConfig;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	private MobileHandler() {
		super();
	}

	public static MobileHandler getInstance() {
		return instance;
	}

	/**
	 * ������
	 * 
	 * @param args
	 * @deprecated �����ڲ���
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
		//mh.sendMessage("1234", "GZ#-u ���� -p #$500Ԫ 200602");
		//mh.sendMessage("1234", "GZ#-p 1234 200602");

		//CL
		//mh.mobileMsgReceived("124", "SP#HTID -p 111 N ��ͨ��", "sid");

	}

	/**
	 * �������ļ���������ҵ������
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
	 * ��������
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
	 * NC���յ����ź���õķ���
	 * <li>ʵ����ҵ����������ҵ����
	 * 
	 * @param phone �ֻ���
	 * @param msg ��ȷ����Ϣ��ʽӦ����"GZ#-p UserPassword [-u UserCode -c UnitCode -a
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
			// ������Ϣ��ʽ
			strCmd = checkOptions(msg, hmOptionValue);

			//XXX:ע������Դ����ǰ�߳�
			regDataSourceForServlet(hmOptionValue);

			// ����ҵ����������У��
			MobilePluginBase mobileplugin = null;

			mobileplugin = initCurrentPlugin(strCmd, hmOptionValue);

			// /�����û��͵�¼��˾
			checkUserAndCorpByPhone(phone, mobileplugin, hmOptionValue);

			// ҵ����������ţ�����з���ֵ�����ͷ�������
			String retStr = mobileplugin.dealMobileMsg();
			if (mobileplugin.shouldDeleteSid()) {
				//ɾ���Ự��Ϣ
				try {
					BaseDAO dao = new BaseDAO();
					dao.deleteByClause(MobileData.class, "mobile='" + phone + "' and pk_sid='" + sid + "'");
				} catch (DAOException e) {
					//����־�쳣
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
	 * NC���յ��ʼ�����õķ���
	 * <li>ʵ����ҵ����������ҵ����
	 * @param emailAddr
	 * @param subject
	 */
	public void emailMsgReceived(String emailAddr, String subject) {
		Logger.info("========================");
		Logger.info("����MobileHandler.emailMsgReceived");
		Logger.info("emailAddr��" + emailAddr);
		Logger.info("subject��"+subject);
		Logger.info("========================");
		
		HashMap hmOptionValue = new HashMap();
		String strCmd = "";
		try {
			// ������Ϣ��ʽ
			strCmd = checkOptions(subject, hmOptionValue);

			//XXX:ע������Դ����ǰ�߳�
			regDataSourceForServlet(hmOptionValue);

			// ����ҵ����������У��
			MobilePluginBase mobileplugin = null;

			mobileplugin = initCurrentPlugin(strCmd, hmOptionValue);

			// �����û��͵�¼��˾
			checkUserAndCorpByEmail(emailAddr, mobileplugin, hmOptionValue);

			// ҵ����������ţ�����з���ֵ�����ͷ�������
			String retStr = mobileplugin.dealMobileMsg();
			if (retStr != null) {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### ����NC����ƽ̨���ʼ�֪ͨ*/, retStr, new String[] { emailAddr }, null, null);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			try {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### ����NC����ƽ̨���ʼ�֪ͨ*/, e.getMessage(), new String[] { emailAddr },
						null, null);
			} catch (MailException ex) {
				//XXX:����
			}
		}
	}

	/**
	 * ���Ͷ���
	 * @param phone
	 * @param message
	 */
	private void sendMessage(String phone, String message) {
		ShortMessageService sms = WirelessManager.getSMS();
		if (sms == null) {
			Logger.error(">>�Ҳ�����Ķ��ŷ���ʵ���࣬�޷����ͺͽ��ն���");
			return;
		}
		if (!sms.initialize()) {
			//���ŷ����ʼ��ʧ��
			Logger.error(">>���ŷ����ʼ��ʧ��");
			return;
		}

		//����Ҫ�ỰSID
		sms.sendMessage(phone, message);
	}

	/**
	 * �����Ϣ��ʽ
	 *  
	 * @param strMsg
	 * @param hmOptionValues
	 * @return
	 * @throws MobileException
	 */
	private String checkOptions(String strMsg, HashMap hmOptionValues) throws MobileException {
		Logger.info("����MobileHandler.checkOptions strMsg: "+strMsg);
		int cmdIndex = strMsg.indexOf(CMD_DELIMITER);
		if (cmdIndex < 1)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000001", null, new String[]{strMsg})/*���󣺷Ƿ���Ϣ��ʽ={0}*/);
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
		Logger.info("����MobileHandler.checkOptions _options: "+_options);
		Logger.info("����MobileHandler.checkOptions tokens: " + tokens);
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
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000002")/*���󣺷Ƿ���Ϣ��ʽ*/);
		}

		return strCmd;
	}

	/**
	 * �����ʼ��û�����Ч�ԣ�����ֵ��ҵ����
	 * <li>1.�����ʼ��ҵ���Ա�����е�ҵ��Ա������������������ҵ����ҵ��Ա�����������ʼ�
	 * <li>2.����ҵ��Ա�������û�������ҵ�����û������������ʼ�
	 * 
	 * @param emailAddr �ʼ���ַ
	 * @param mpb ҵ��������
	 * @param hmOptionValue ��Ϣ��ֵ��
	 * @throws MobileException
	 */
	private void checkUserAndCorpByEmail(String emailAddr, MobilePluginBase mpb, HashMap hmOptionValue)
			throws MobileException {
		try {
			// ��¼��˾PK
			String unitCode = (String) hmOptionValue.get(MobileOptions.OPTION_UNITCODE);
			String pkCorp = null;
			if (unitCode != null) {
				//���ݹ�˾�����ѯ����˾VO������ȡ��PK
				Collection coCorps = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(
						CorpVO.class, "unitcode='" + unitCode + "'");
				if (coCorps.size() > 0) {
					CorpVO voCorp = (CorpVO) coCorps.iterator().next();
					pkCorp = voCorp.getPrimaryKey();
					mpb.setPkCorp(pkCorp);
				}
			}

			//�����ʼ��ҵ���Ա�����еĻ������� 
			Collection<PsndocVO> psnVOs = new BaseDAO().retrieveByClause(PsndocVO.class, "email ='" + emailAddr + "'");
			checkPsndoc(mpb, hmOptionValue, psnVOs.toArray(new PsndocVO[0]));
		} catch (MobileException e) {
			throw e;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*ϵͳ�쳣�����ҹ���Ա*/, e);
		}
	}

	private void checkPsndoc(MobilePluginBase mpb, HashMap hmOptionValue, PsndocVO[] psnVOs)
			throws MobileException, BusinessException {
		if (psnVOs == null || psnVOs.length == 0)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000004")/*�����Ҳ�����Ӧ����Ա�������������ҹ���Ա*/);

		ArrayList alFoundPsns = new ArrayList();
		ArrayList alFoundUsers = new ArrayList();
		String userCode = (String) hmOptionValue.get(MobileOptions.OPTION_USERCODE);
		if (psnVOs.length > 1) {
			// 1.a����ҵ�����������������ҵ�ÿ��ҵ��Ա�������û�
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
			// 1.b����ҵ�1���������������ҵ�ҵ��Ա�������û�
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
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000005")/*���󣺶�Ӧ��ҵ��Աû����NCϵͳ�й����û������ҹ���Ա*/);
		} else if (alFoundUsers.size() > 1) {
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000006")/*���󣺶�Ӧ��ҵ��Ա��NCϵͳ�й����˶���û������ҹ���Ա*/);
		} else {
			// 2.��֤�û�����
			UserVO uVO = (UserVO) alFoundUsers.get(0);
			if (mpb.isNeedValidatePwd()) {
				//XXX:�ɲ�������Ƿ���ҪУ������
				Encode rsa = new Encode();
				String pwd = (String) hmOptionValue.get(MobileOptions.OPTION_USERPASSWORD);

				if (StringUtil.isEmptyWithTrim(pwd))
					throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000007")/*����ҵ������ҪУ������*/);
				String rsaPwd = rsa.encode(pwd);
				if (!rsaPwd.equals(uVO.getUser_password()))
					throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000008")/*�������벻��ȷ*/);
			}

			// ��Ӧ����Ա����������������PK
			mpb.setPkPsndoc(((PsndocVO) alFoundPsns.get(0)).getPrimaryKey());
			// ��Ӧ���û�PK
			mpb.setPkUser(uVO.getPrimaryKey());
		}
	}

	/**
	 * �����ֻ��û�����Ч�ԣ�����ֵ��ҵ����
	 * <li>1.�����ֻ����ҵ���Ա�����е�ҵ��Ա������������������ҵ����ҵ��Ա�������������
	 * <li>2.����ҵ��Ա�������û�������ҵ�����û��������������
	 * 
	 * @param phone �ֻ���
	 * @param mpb ҵ��������
	 * @param hmOptionValue ��Ϣ��ֵ��
	 * @throws MobileException
	 */
	private void checkUserAndCorpByPhone(String phone, MobilePluginBase mpb, HashMap hmOptionValue)
			throws MobileException {
		try {
			// /��¼��˾PK
			String unitCode = (String) hmOptionValue.get(MobileOptions.OPTION_UNITCODE);
			String pkCorp = null;
			if (unitCode != null) {
				//���ݹ�˾�����ѯ����˾VO������ȡ��PK
				Collection coCorps = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(
						CorpVO.class, "unitcode='" + unitCode + "'");
				if (coCorps.size() > 0) {
					CorpVO voCorp = (CorpVO) coCorps.iterator().next();
					pkCorp = voCorp.getPrimaryKey();
					mpb.setPkCorp(pkCorp);
				}
			}

			//�����ֻ����ҵ���Ա�����еĻ�������
			Collection<PsndocVO> psnVOs = new BaseDAO().retrieveByClause(PsndocVO.class, "mobile ='" + phone + "'");
			checkPsndoc(mpb, hmOptionValue, psnVOs.toArray(new PsndocVO[0]));
		} catch (MobileException e) {
			throw e;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*ϵͳ�쳣�����ҹ���Ա*/, e);
		}
	}

	/**
	 * ÿ��Servlet����Ҫע������Դ��Ϣ�ſɵ���DMO
	 */
	private void regDataSourceForServlet(HashMap hmOptionValue) throws MobileException {
		String dsName = null;
		String accountCode = (String) hmOptionValue.get(MobileOptions.OPTION_ACCOUNT);
		if (accountCode != null) {
			// �������ױ����ҵ�����Դ��Ϣ
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
				// ����ץס�����쳣�������ܲ�ͷ��ܲ��쳣
				Logger.error(e.getMessage(), e);
			}

			if (retrAccount != null)
				dsName = retrAccount.getDataSourceName();
		}
		if (dsName == null) {
			MobileConfig mc = getMobileConfig();
			dsName = mc.getDatasource();
		}

		// 1.У��
		try {
			String[] dataSources = getAccountService().findDatasource();
			if (dataSources == null)
				throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*ϵͳ�쳣�����ҹ���Ա*/);
			boolean bValid = Arrays.asList(dataSources).contains(dsName);
			if (!bValid)
				throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000009")/*������Ч����Դ*/);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000003")/*ϵͳ�쳣�����ҹ���Ա*/);
		}

		// 2.ע��
		InvocationInfoProxy.getInstance().setUserDataSource(dsName);
	}

	/**
	 * ���ϵͳ��ܵ����׷���
	 * @return
	 */
	private IConfigFileService getAccountService() {
		IConfigFileService name = (IConfigFileService) NCLocator.getInstance().lookup(
				IConfigFileService.class.getName());
		return name;
	}

	/**
	 * ����ָ���ҵ���ȷ��ҵ������������ʼ��һЩ����
	 * 
	 * @param command ָ�
	 * @return ��ָ���Ӧ��ҵ������ʵ������
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
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000010", null, new String[]{command})/*�����Ҳ���ҵ����={0}*/);

		// ʵ����ҵ����
		MobilePluginBase mpb = null;

		try {
			mpb = (MobilePluginBase) NewObjectService.newInstance(selectedPluginVO.getModule(),
					selectedPluginVO.getClassname());
		} catch (Exception e) {
			//FIXME::���������ܲ����ܲ��쳣��
			Logger.error(e.getMessage(), e);
		}
		if (mpb == null)
			throw new MobileException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000011")/*���󣺲���ʵ����ҵ����*/);

		// /ָ��
		mpb.setCommand(command);
		// /��¼����
		mpb.setLoginDate((String) hmOptionValue.get(MobileOptions.OPTION_LOGINDATE));
		// /��������
		mpb.setParams((String[]) hmOptionValue.get(MobileOptions.ARGS));

		return mpb;
	}

}
