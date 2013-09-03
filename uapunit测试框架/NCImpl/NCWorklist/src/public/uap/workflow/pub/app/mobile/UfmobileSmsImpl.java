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
 * ʹ��UfMobile SDK2.0�Ķ���Ϣ����ʵ����
 * <li>��Ϊ���ŷ����Ĭ��ʵ����
 * 
 * @author leijun 2007-3-22
 * @since NC5.0 
 * @modifier leijun 2007-9-4 ����UFMobile��֧�ֵ����û�����������������Ҫ��������
 * @deprecated v6��ʹ��PubSmsDevImpl
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
				//1.��¼
				ufmobileSession.login();
				//2.���Ͷ���
				return ufmobileSession.getSmsSender().sendSms(msg, targetPhone);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.�ǳ�
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
	 * ��ȡUFMobile����
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
				//1.��¼
				ufmobileSession.login();
				//2.���Ͷ���
				return ufmobileSession.getSmsSender().sendSms(msg, targetPhone, sid);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			} finally {
				try {
					//3.�ǳ�
					if (ufmobileSession != null)
						ufmobileSession.logout();
				} catch (Exception e) {
					Logger.error(e.getMessage(),e);
				}
			}
		}
		//TODO:: ����޷���������
		return null;
	}

	@Override
	public Object sendMessages(String[] targetPhones, String msg) {
		if (targetPhones == null || targetPhones.length == 0)
			return null;

		StringBuffer strCompositePhone = new StringBuffer();
		//ƴ����","�ָ����ַ���
		for (int i = 0; i < targetPhones.length; i++) {
			strCompositePhone.append(targetPhones[i]);
			strCompositePhone.append(",");
		}
		//����
		//FIXME::����ֵ��ʲô��˼����Ӧ���ĸ��ֻ����أ�
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
				//1.��¼
				ufmobileSession.login();
				//2.ѭ�����Ͷ���
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
					//3.�ǳ�
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
				//1.��¼
				ufmobileSession.login();
				//2.FIXME::���ն���-һ�ν���20��
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
					//3.�ǳ�
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
