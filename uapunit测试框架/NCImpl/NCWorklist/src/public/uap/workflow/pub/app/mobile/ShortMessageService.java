package uap.workflow.pub.app.mobile;

import uap.workflow.pub.app.mobile.vo.ReceivedSmsVO;


/**
 * ����Ϣ����ӿ�
 * <li>����ʵ�������ע����ierp/bin/mobileplugin.xml�ļ���
 * 
 * @author leijun 2003-10-15
 * @modifier leijun 2007-3-22 �����������͵ķ���
 * @modifier ewei 2007-9-24 ������ͬ�ĺ����鷢��ͬ����Ϣ�ķ���
 */
public abstract class ShortMessageService {

	public ShortMessageService() {
		super();
	}

	/**
	 * ��ʼ��������������ŷ����豸��
	 */
	public abstract boolean initialize();

	/**
	 * ���Ͷ���
	 * @param targetPhone Ŀ���ֻ���
	 * @param msg ��Ϣ����
	 */
	public abstract Object sendMessage(String targetPhone, String msg);

	/**
	 * ���Ͷ��ţ����ỰID
	 * @param targetPhone Ŀ���ֻ���
	 * @param msg ��Ϣ����
	 * @param sid �ỰID�����ֻ��Ŷ�Ӧ
	 */
	public abstract Object sendMessage(String targetPhone, String msg, String sid);

	/**
	 * �������Ͷ���
	 * @param targetPhones Ŀ���ֻ�������
	 * @param msg ��Ϣ����
	 */
	public abstract Object sendMessages(String[] targetPhones, String msg);

	/**
	 * �������Ͷ��ţ����ỰID����
	 * @param targetPhones Ŀ���ֻ�������
	 * @param msg ��Ϣ����
	 * @param sids �ỰID���飬���ֻ��Ŷ�Ӧ
	 */
	public abstract Object sendMessages(String[] targetPhones, String msg, String[] sids);

	/**
	 * ��ͬ�ĺ����鷢��ͬ����Ϣ
	 * @param targetPhones
	 * @param msg
	 */
	public abstract Object sendMessages(String[][] targetPhones, String[] msg);

	/**
	 * �������ն���
	 * @return
	 */
	public abstract ReceivedSmsVO[] receiveMessages();
}
