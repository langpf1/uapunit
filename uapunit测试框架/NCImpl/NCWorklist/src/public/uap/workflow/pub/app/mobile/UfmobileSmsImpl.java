package uap.workflow.pub.app.mobile;

import java.util.HashMap;

import uap.workflow.pub.app.mobile.vo.MobileConfig;
import uap.workflow.pub.app.mobile.vo.ReceivedSmsVO;
import uap.workflow.pub.app.mobile.vo.UFMobileAgent;
import uap.workflow.pub.app.mobile.vo.UFMobileConfig;

import nc.bs.logging.Logger;

import com.ufmobile.sms.ReceivedSmsRecord;
import com.ufmobile.sms.Session;

/**
 * 使用UfMobile SDK2.0的短消息服务实现类
 * <li>作为短信服务的默认实现类
 * 
 * @author leijun 2007-3-22
 * @since NC5.0 
 * @modifier leijun 2007-9-4 由于UFMobile不支持单个用户并发操作，所以需要并发控制
 * @deprecated v6后使用PubSmsDevImpl
 */
public class UfmobileSmsImpl extends ShortMessageService {

	private static Object lock = new Object();

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public Object sendMessage(String targetPhone, String msg) {

		synchronized (lock) {
			Session ufmobileSession = null;
			try {
				ufmobileSession = getUfMobileSession();
				//1.登录
				ufmobileSession.login();
				//2.发送短信
				return ufmobileSession.getSmsSender().sendSms(msg, targetPhone);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.登出
					if (ufmobileSession != null)
						ufmobileSession.logout();
				} catch (Exception e) {
					Logger.error(e.getMessage(),e);
				}
			}
		}
		//TODO::
		return null;
	}

	/**
	 * 获取UFMobile连接
	 * @return
	 * @throws Exception
	 */
	public Session getUfMobileSession() throws Exception {
		MobileConfig mc = WirelessManager.getMobileConfig();
		UFMobileConfig ufmobile = mc.getUfmobile();
		UFMobileAgent agent = ufmobile.getAgent();

		Session ufmobileSession = new Session(ufmobile.getUrl(), new Integer(ufmobile.getCompanyid())
				.intValue(), ufmobile.getUser(), ufmobile.getPassword());
		if (agent.isNeeded()) {
			ufmobileSession.setProxy(agent.getIp(), agent.getPort(), agent.getAgentuser(), agent
					.getAgentpwd());
		}
		return ufmobileSession;
	}

	@Override
	public Object sendMessage(String targetPhone, String msg, String sid) {
		synchronized (lock) {
			Session ufmobileSession = null;
			try {
				ufmobileSession = getUfMobileSession();
				//1.登录
				ufmobileSession.login();
				//2.发送短信
				return ufmobileSession.getSmsSender().sendSms(msg, targetPhone, sid);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.登出
					if (ufmobileSession != null)
						ufmobileSession.logout();
				} catch (Exception e) {
					Logger.error(e.getMessage(),e);
				}
			}
		}
		//TODO:: 如果无返回做处理
		return null;
	}

	@Override
	public Object sendMessages(String[] targetPhones, String msg) {
		if (targetPhones == null || targetPhones.length == 0)
			return null;

		StringBuffer strCompositePhone = new StringBuffer();
		//拼成以","分隔的字符串
		for (int i = 0; i < targetPhones.length; i++) {
			strCompositePhone.append(targetPhones[i]);
			strCompositePhone.append(",");
		}
		//发送
		//FIXME::返回值是什么意思，对应于哪个手机号呢？
		return sendMessage(strCompositePhone.toString(), msg);
	}

	@Override
	public Object sendMessages(String[] targetPhones, String msg, String[] sids) {
		if (targetPhones == null || targetPhones.length == 0)
			return null;
		synchronized (lock) {
			Session ufmobileSession = null;
			try {
				ufmobileSession = getUfMobileSession();
				//1.登录
				ufmobileSession.login();
				//2.循环发送短信
				HashMap hmRet = new HashMap();
				for (int i = 0; i < targetPhones.length; i++) {
					Object obj = ufmobileSession.getSmsSender().sendSms(msg, targetPhones[i], sids[i]);
					hmRet.put(targetPhones[i], obj);
				}
				return hmRet;
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.登出
					if (ufmobileSession != null)
						ufmobileSession.logout();
				} catch (Exception e) {
					Logger.error(e.getMessage(),e);
				}
			}
		}
		return null;
	}

	@Override
	public ReceivedSmsVO[] receiveMessages() {
		synchronized (lock) {
			Session ufmobileSession = null;
			try {
				ufmobileSession = getUfMobileSession();
				//1.登录
				ufmobileSession.login();
				//2.FIXME::接收短信-一次接收20条
				ReceivedSmsRecord[] smses = ufmobileSession.getSmsReceiver().receiveSms(20);
				int length = smses == null ? 0 : smses.length;

				ReceivedSmsVO[] smsVOs = new ReceivedSmsVO[length];
				for (int i = 0; i < length; i++) {
					smsVOs[i] = new ReceivedSmsVO();
					smsVOs[i].setMobileNumber(smses[i].getMobileNumber());
					smsVOs[i].setSessionId(smses[i].getSerailNumber());
					smsVOs[i].setSmsContent(smses[i].getSmsContent());
				}
				return smsVOs;
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.登出
					if (ufmobileSession != null)
						ufmobileSession.logout();
				} catch (Exception e) {
					Logger.error(e.getMessage(),e);
				}
			}
		}
		return null;
	}

	@Override
	public Object sendMessages(String[][] targetPhones, String[] msges) {
		HashMap hmRet = new HashMap();
		for (int i = 0; i < msges.length; i++) {
			String[] phones = targetPhones[i];
			sendMessages(phones, msges[i]);
		}

		//FIXME::
		return null;
	}

}
