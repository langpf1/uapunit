package uap.workflow.pub.app.mobile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import uap.workflow.pub.app.mail.vo.DefaultSMTP;
import uap.workflow.pub.app.message.vo.SysMessageParam;
import uap.workflow.pub.app.mobile.vo.MobileConfig;
import uap.workflow.pub.app.mobile.vo.MobileImplInfo;
import uap.workflow.pub.app.mobile.vo.MobilePluginVO;
import uap.workflow.pub.app.mobile.vo.PubDevSmsConfig;
import uap.workflow.pub.app.mobile.vo.UFMobileAgent;
import uap.workflow.pub.app.mobile.vo.UFMobileConfig;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.message.config.MailConfigAccessor;
import nc.message.config.SMSConfigAccessor;
import nc.vo.framework.rsa.Encode;

import com.thoughtworks.xstream.XStream;

/**
 * ����Ӧ��ƽ̨������
 * 
 * @author �׾� 2003-10-15
 * @modifier leijun 2007-3-26 ��Ϊ��mobileplugin.xml�ж�ȡ����Ϣ�����ṩ���������Ϣ
 * @modifier leijun 2007-7-6 ���浽XML�ļ���ʱ��������m_smp�ÿ��Ա��ȡ���º��ֵ����������������
 */
public class WirelessManager {
	/**
	 * �ʼ�����������Ϣ
	 */
	private static String MESSAGE_4PF_CONF;//= "./ierp/bin/message4pf.xml";

	/**
	 * ���ŷ���������Ϣ
	 */
	private static String CONFIG_FILE;//= "./mobileplugin.xml";

	static {
		// lj2005-11-8 ʹ�þ�̬��ʼ��������ʼ����̬����
		MESSAGE_4PF_CONF = RuntimeEnv.getInstance().getNCHome() + "/ierp/bin/message4pf.xml";
		CONFIG_FILE = RuntimeEnv.getInstance().getNCHome() + "/ierp/bin/mobileplugin.xml";
	}

	// ϵͳ����������Ϣƽ̨�����ò���
	private static SysMessageParam m_smp = null;

	/**
	 * ��ǰʹ�õĶ���Ϣ���� ʵ����
	 */
	private static ShortMessageService _shortMsgService = null;

	/**
	 * ���Ų����UFMobile����Ϣ������
	 */
	private static MobileConfig m_mobileConfig;

	public WirelessManager() {
		super();
	}

	/**
	 * ��ȡϵͳ�ṩ����Ϣ�������ò���
	 */
	public synchronized static SysMessageParam fetchSysMsgParam() {
		if (m_smp != null)
			return m_smp;
		File msgFile = new File(MESSAGE_4PF_CONF);
		if (msgFile.exists()) {
			
			FileReader in = null;
			try {
				in = new FileReader(msgFile);
				XStream xstream = getAliasXstream();
				m_smp = (SysMessageParam) xstream.fromXML(in);
			} catch (FileNotFoundException e) {
				Logger.error(e.getMessage(), e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Logger.error(e.getMessage(), e);
					}
				}
				
				// yanke1 2012-3-7 ȡ��Ϣ���ĵ�����
				if (m_smp != null) {
					DefaultSMTP smtp = m_smp.getSmtp();
					
					smtp.setPop3(MailConfigAccessor.getPop3Server());
					smtp.setSmtp(MailConfigAccessor.getSMTPServer());
					smtp.setUser(MailConfigAccessor.getUser());
					smtp.setPassword(new Encode().encode(MailConfigAccessor.getPassword()));
					smtp.setSender(MailConfigAccessor.getMailFrom());
					smtp.setSenderName(MailConfigAccessor.getMailFrom());
				}
			}
		}
		return m_smp;
	}

	/**
	 * �������ļ���������ҵ������
	 * 
	 * @return
	 */
	public synchronized static MobileConfig getMobileConfig() {
		if (m_mobileConfig != null)
			return m_mobileConfig;

		XStream xs = getMobileAliasXstream();

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
			
			// yanke1 2012-3-7 ȡ��Ϣ���ĵ�����
			if (m_mobileConfig != null) {
				PubDevSmsConfig c = m_mobileConfig.getPubSms();
				c.setURL(SMSConfigAccessor.getSMSServer());
				c.setTimeout(SMSConfigAccessor.getTimeOut());
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
		XStream xs = getMobileAliasXstream();

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

	private static XStream getMobileAliasXstream() {
		XStream xs = new XStream();
		xs.alias("mobileconfig", MobileConfig.class);
		xs.alias("mobileplugin", MobilePluginVO.class);
		xs.alias("mobileplugins", MobilePluginVO[].class);
		xs.alias("mobileimplinfos", MobileImplInfo[].class);
		xs.alias("mobileimpl", MobileImplInfo.class);
		xs.alias("pubdev", PubDevSmsConfig.class);
		return xs;
	}

	private static XStream getAliasXstream() {
		XStream xstream = new XStream();
		xstream.alias("SysMessageParam", SysMessageParam.class);
		return xstream;
	}

	/**
	 * ��ȡ��ǰʹ�õĶ���Ϣ����
	 */
	public synchronized static ShortMessageService getSMS() {
		if (_shortMsgService != null)
			return _shortMsgService;

		//��ȡ�����ļ������õĶ���Ϣ����ʵ����
		MobileConfig mc = getMobileConfig();
		MobileImplInfo[] miis = mc.getMobileimplinfos();
		if (miis == null || miis.length == 0) {
			//Ĭ��ʹ��UfMobile�ķ���
			_shortMsgService = new UfmobileSmsImpl();
			return _shortMsgService;
		}

		for (int i = 0; i < miis.length; i++) {
			if (miis[i].isActive()) {
				try {
					_shortMsgService = (ShortMessageService) Class.forName(miis[i].getQualifiedclass())
							.newInstance();
					return _shortMsgService;
				} catch (Exception e) {
					//ʵ����������쳣
					Logger.error(e.getMessage(), e);
				}
			}
		}

		return _shortMsgService;
	}

	/**
	 * ���浽XML��
	 * 
	 * @return boolean
	 * @throws IOException 
	 */
	public static boolean saveSysMsgParam(SysMessageParam smp) throws IOException {
		if (smp == null)
			return false;

		FileWriter out = new FileWriter(MESSAGE_4PF_CONF);
		XStream xstream = getAliasXstream();
		xstream.toXML(smp, out);

		//XXX::����ɹ��󽫱�����գ��Ա����¶�ȡ�ļ�
		m_smp = null;
		return true;
	}

	/**
	 * ������
	 * 
	 * @param args
	 * @deprecated �����ڲ���
	 */
	public static void testMobileConfig(String[] args) {
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

		saveMobileConfig(mc);
		Object obj = getMobileConfig();
		Logger.debug(obj);

		// URL
		//mh.sendMessage("1234", "GZ#-u ���� -p #$500Ԫ 200602");
		//mh.sendMessage("1234", "GZ#-p 1234 200602");

		//CL
		//mh.mobileMsgReceived("124", "SP#HTID -p 111 N ��ͨ��", "sid");

	}

}