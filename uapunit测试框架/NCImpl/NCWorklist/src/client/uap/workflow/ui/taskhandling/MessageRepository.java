package uap.workflow.ui.taskhandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;

import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.ui.util.PfUIDataCache;

/**
 * 消息 在客户端的存储仓库
 * <li>主要包括：公告消息、待办消息、通知消息
 * 
 * @author leijun 2006-5-22
 */
public class MessageRepository extends Observable {
	/**
	 * 单例模式
	 */
	private static MessageRepository inst = new MessageRepository();

	public static MessageRepository getInstance() {
		return inst;
	}

	/**
	 * 公告消息
	 */
	private ArrayList<MessageVO> alBulletinMsgs = new ArrayList();

	/**
	 * 预警消息
	 */
	private ArrayList<MessageVO> alPaMsgs = new ArrayList();

	/**
	 * 待办事务
	 */
	private ArrayList<MessageVO> alWorkList = new ArrayList();
	
	/**
	 * 通知消息
	 */
	private ArrayList<MessageVO> alInfoMsgs = new ArrayList();

	/**
	 * 消息面板的配置信息，需要缓存
	 */
	private MessagePanelOptions mpo = null;

	private MessageRepository() {
		super();
	}

	/**
	 * 增加公告消息
	 * 
	 * @param msgs
	 */
	public void addBulletinMsgs(MessageVO[] msgs) {
		alBulletinMsgs.addAll(Arrays.asList(msgs));
	}

	/**
	 * 增加预警消息
	 * 
	 * @param msgs
	 */
	public void addPaMsgs(MessageVO[] msgs) {
		alPaMsgs.addAll(Arrays.asList(msgs));
		setChanged();
	}
	
	/**
	 * 增加通知消息
	 * 
	 * @param msgs
	 */
	public void addInfoMsgs(MessageVO[] msgs) {
		alInfoMsgs.addAll(Arrays.asList(msgs));
		setChanged();
	}

	/**
	 * 增加待办事务
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
	 * 清空公告消息列表
	 */
	public void clearBulletinMsgs() {
		alBulletinMsgs.clear();
		setChanged();
	}

	/**
	 * 清空预警消息列表
	 */
	public void clearPaMsgs() {
		alPaMsgs.clear();
		setChanged();
	}
	
	/**
	 * 清空通知消息列表
	 */
	public void clearInfoMsgs() {
		alInfoMsgs.clear();
		setChanged();
	}

	/**
	 * 清空待办事务列表
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
	 * 消息面板的配置
	 * 
	 * @return
	 */
	public MessagePanelOptions getMsgPanelOptions() {
		mpo = PfUIDataCache.getMsgPanelOptions();
		return mpo;
	}

	/**
	 * 手动通知观察者来更新
	 */
	public void handNofityOserver(MessageVO msgvo) {
		setChanged();
		notifyObservers(msgvo);
	}

	/**
	 * 消息面板的配置发生改变后，需要缓存
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
