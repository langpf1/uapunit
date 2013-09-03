package uap.workflow.ui.taskhandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;

import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.ui.util.PfUIDataCache;

/**
 * ��Ϣ �ڿͻ��˵Ĵ洢�ֿ�
 * <li>��Ҫ������������Ϣ��������Ϣ��֪ͨ��Ϣ
 * 
 * @author leijun 2006-5-22
 */
public class MessageRepository extends Observable {
	/**
	 * ����ģʽ
	 */
	private static MessageRepository inst = new MessageRepository();

	public static MessageRepository getInstance() {
		return inst;
	}

	/**
	 * ������Ϣ
	 */
	private ArrayList<MessageVO> alBulletinMsgs = new ArrayList();

	/**
	 * Ԥ����Ϣ
	 */
	private ArrayList<MessageVO> alPaMsgs = new ArrayList();

	/**
	 * ��������
	 */
	private ArrayList<MessageVO> alWorkList = new ArrayList();
	
	/**
	 * ֪ͨ��Ϣ
	 */
	private ArrayList<MessageVO> alInfoMsgs = new ArrayList();

	/**
	 * ��Ϣ����������Ϣ����Ҫ����
	 */
	private MessagePanelOptions mpo = null;

	private MessageRepository() {
		super();
	}

	/**
	 * ���ӹ�����Ϣ
	 * 
	 * @param msgs
	 */
	public void addBulletinMsgs(MessageVO[] msgs) {
		alBulletinMsgs.addAll(Arrays.asList(msgs));
	}

	/**
	 * ����Ԥ����Ϣ
	 * 
	 * @param msgs
	 */
	public void addPaMsgs(MessageVO[] msgs) {
		alPaMsgs.addAll(Arrays.asList(msgs));
		setChanged();
	}
	
	/**
	 * ����֪ͨ��Ϣ
	 * 
	 * @param msgs
	 */
	public void addInfoMsgs(MessageVO[] msgs) {
		alInfoMsgs.addAll(Arrays.asList(msgs));
		setChanged();
	}

	/**
	 * ���Ӵ�������
	 * 
	 * @param msgs
	 */
	public void addWorkItems(MessageVO[] msgs) {
		alWorkList.addAll(Arrays.asList(msgs));
		setChanged();
	}

	public void clearAll() {
		clearBulletinMsgs();
		clearPaMsgs();
		clearWorkList();
		clearInfoMsgs();
		setChanged();
	}

	/**
	 * ��չ�����Ϣ�б�
	 */
	public void clearBulletinMsgs() {
		alBulletinMsgs.clear();
		setChanged();
	}

	/**
	 * ���Ԥ����Ϣ�б�
	 */
	public void clearPaMsgs() {
		alPaMsgs.clear();
		setChanged();
	}
	
	/**
	 * ���֪ͨ��Ϣ�б�
	 */
	public void clearInfoMsgs() {
		alInfoMsgs.clear();
		setChanged();
	}

	/**
	 * ��մ��������б�
	 */
	public void clearWorkList() {
		alWorkList.clear();
		setChanged();
	}

	public ArrayList<MessageVO> getAlBulletinMsgs() {
		return alBulletinMsgs;
	}

	public ArrayList<MessageVO> getAlPaMsgs() {
		return alPaMsgs;
	}
	
	public ArrayList<MessageVO> getAlInfoMsgs() {
		return alInfoMsgs;
	}

	public ArrayList<MessageVO> getAlWorkList() {
		return alWorkList;
	}

	/**
	 * ��Ϣ��������
	 * 
	 * @return
	 */
	public MessagePanelOptions getMsgPanelOptions() {
		mpo = PfUIDataCache.getMsgPanelOptions();
		return mpo;
	}

	/**
	 * �ֶ�֪ͨ�۲���������
	 */
	public void handNofityOserver(MessageVO msgvo) {
		setChanged();
		notifyObservers(msgvo);
	}

	/**
	 * ��Ϣ�������÷����ı����Ҫ����
	 */
	public void msgPanelOptionsChanged() {
		PfUIDataCache.putMsgPanelOptions(mpo);
		setChanged();
	}

	public void removeMsgs(Collection cc) {
		alBulletinMsgs.removeAll(cc);
		alWorkList.removeAll(cc);
		alPaMsgs.removeAll(cc);
		alInfoMsgs.removeAll(cc);
		setChanged();
	}
}
