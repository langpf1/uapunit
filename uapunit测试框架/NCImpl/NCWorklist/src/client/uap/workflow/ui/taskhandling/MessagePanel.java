package uap.workflow.ui.taskhandling;

import java.util.Observable;
import java.util.Observer;

import nc.ui.pub.beans.UIPanel;

/**
 * Let��������Ϣ����
 *  
 * @author leijun 2008-12
 * @since 5.5
 * @deprecated 6.0
 */
public class MessagePanel extends UIPanel implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

//	/** ��Ϣ���ն��� */
//	private MessageReceiveObject m_msgReceiveObject = null;
//
//	/** ��Ϣ�����߳� */
//	private MessageReceiveThread m_msgReceiveThread = null;
//
//	/** Ĭ�ϵı�����ɫ */
//	private Color defBackColor = Color.WHITE;
//
//	/** ��Ϣ��嵱ǰ��������ϢƬ */
//	private HashMap<String, AbstractMessageLet> hmLets = new HashMap<String, AbstractMessageLet>();
//	
//	public MessagePanel() {
//		super();
//
//		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();
//		if (mpo.getLetInfo() == null) {
//			mpo.setLetInfo(createDefaultLetInfo2());
//		}
//
//		//����չ۲��ߣ�����ӽ�ȥ
//		MessageRepository.getInstance().deleteObservers();
//		MessageRepository.getInstance().addObserver(this);
//
//		//��ʼ��UI
//		initUI();
//
//		//ʹ��һ����ʱ�߳����첽���β�ѯ��Ϣ�������� �Զ�ˢ���߳�
////		startTempThread();
//	}
//
//	/**
//	 * ʹ��һ����ʱ�߳����첽���β�ѯ��Ϣ�������� �Զ�ˢ���߳�
//	 */
//	private void startTempThread() {
//		//
//		Thread tempThread = new Thread(new Runnable() {
//			public void run() {
//				try {
//					//ʹ�����е�ɸѡ����ѯ��Ϣ
//					getMsgReceiveObject().fetchNewMessagesAllFilter();
//				} catch (Exception e) {
//					Logger.error(e.getMessage(), e);
//				}
//
//				try {
//					//�ȴ�2s
//					Thread.sleep(2000);
//				} catch (Exception e) {
//					Logger.error(e.getMessage(), e);
//				}
//
//				//���� �Զ�ˢ���߳�
//				startRefreshThread(MessageRepository.getInstance().getMsgPanelOptions().getFreshSetting());
//			}
//		});
//		tempThread.setName("MessagePanel.tempThread");
//		Logger.debug("##��Ϣ����ʼ������ǰ�߳�=" + Thread.currentThread() + "��������ʱ�߳�=" + tempThread);
//		tempThread.start();
//	}
//
//	private LetNode createDefaultLetInfo2() {
//		//ʹ��6.0Ĭ�ϵ�һ��ʽ���ҵ���Ϣ
//		LetNode msgLetNode = new LetNode(MessagePanelOptions.MESSAGE_WORKLIST);
//
//		return msgLetNode;
//	}
//
//	private void initUI() {
//		setBackground(defBackColor);
//		setName("MessagePanel");
//
//		reLayout();
//	}
//
//	/**
//	 * �������Ϊ�Զ�ˢ�£�������ˢ���߳�
//	 * @param refreshSetting
//	 */
//	public void startRefreshThread(RefreshSetting refreshSetting) {
//		if (refreshSetting.isAutoRefresh()) {
//			//�������Ϊ�Զ�ˢ�£������� �Զ�ˢ���߳�
//			int refreshInterval = refreshSetting.getRefreshInterval();
//			int refreshUnit = refreshSetting.getTimeUnit();
//			int refreshSecond = refreshInterval * 1000 * (int) Math.pow(60, refreshUnit);
//			getMsgReceiveObject().setReceInterval(refreshSecond);
//
//			MessageReceiveThread autoReceiveThread = new MessageReceiveThread(this);
//
//			autoReceiveThread.setMessageObj(getMsgReceiveObject());
//			//thread.setMessageReceiver(this);
//			m_msgReceiveThread = autoReceiveThread;
//
//			// ���浽�ͻ����ڴ���
//			WorkbenchEnvironment.getInstance().putClientCache(MessageReceiveThread.class.getName(),
//					m_msgReceiveThread);
//
//			autoReceiveThread.start();
//			Logger.debug("������Ϣˢ���߳�=" + autoReceiveThread);
//		}
//	}
//
//	public Container getParentOfBanner() {
//		return this;
//	}
//
//	protected void finalize() throws Throwable {
//		stopReceiveMessage();
//		//getMsgDetailDlg().dispose();
//		super.finalize();
//	}
//
//	private UISplitPane buildSplit(LetNode virtualLetNode) {
//		int iOrientation = virtualLetNode.getSplitDirection();
//		UISplitPane sp = createSplitPane(iOrientation);
//		sp.setResizeWeight(virtualLetNode.getSplitPercent());
//
//		//������������
//		LetNode leftNode = (LetNode) virtualLetNode.getChildAt(0);
//		String strLeftCode = leftNode.getCode();
//		if (strLeftCode.equals(LetNode.VIRTUAL_NODE_CODE)) {
//			//˵��Ϊ��ڵ�
//			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
//				sp.add(buildSplit(leftNode), JSplitPane.TOP);
//			else
//				sp.add(buildSplit(leftNode), JSplitPane.LEFT);
//		} else {
//			AbstractMessageLet aml = createLet(strLeftCode);
//			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
//				sp.add(aml, JSplitPane.TOP);
//			else
//				sp.add(aml, JSplitPane.LEFT);
//		}
//
//		//���������Һ���
//		LetNode rightNode = (LetNode) virtualLetNode.getChildAt(1);
//		String strRightCode = rightNode.getCode();
//		if (strRightCode.equals(LetNode.VIRTUAL_NODE_CODE)) {
//			//˵��Ϊ��ڵ�
//			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
//				sp.add(buildSplit(rightNode), JSplitPane.BOTTOM);
//			else
//				sp.add(buildSplit(rightNode), JSplitPane.RIGHT);
//		} else {
//			AbstractMessageLet aml = createLet(strRightCode);
//			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
//				sp.add(aml, JSplitPane.BOTTOM);
//			else
//				sp.add(aml, JSplitPane.RIGHT);
//		}
//		return sp;
//	}
//
//	private JComponent createCenterComponent() {
//		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();
//		LetNode letInfo = mpo.getLetInfo();
//		if (letInfo.getCode().equals(LetNode.VIRTUAL_NODE_CODE))
//			return buildSplit(letInfo);
//		else {
//			//ֻ��һ��Ҷ�ӽڵ�
//			return createLet(letInfo.getCode());
//		}
//	}
//
//	private AbstractMessageLet createLet(String strCode) {
//		AbstractMessageLet amlReturn;
//		MessageLetVO letVo = PfUIDataCache.getMessageLet(strCode);
//		if (letVo == null) {
//			amlReturn = new ErrorMessageLet(this, strCode);
//		} else {
//			//����Ѿ����ڸ���ϢƬ����ֱ��ʹ��
//			if (hmLets.containsKey(strCode))
//				return hmLets.get(strCode);
//
//			//�´�������ϢƬ
//			try {
//				Constructor construct = Class.forName(letVo.getUiclass()).getConstructor(this.getClass());
//				amlReturn = (AbstractMessageLet) construct.newInstance(this);
//			} catch (Exception e) {
//				Logger.error(e.getMessage(), e);
//				amlReturn = new ErrorMessageLet(this, strCode);
//			}
//		}
//		hmLets.put(amlReturn.getCode(), amlReturn);
//		return amlReturn;
//	}
//
//	/**
//	 * ��ע����¼ʱ����Ҫ���¼�������JSplitPane�ķָ��ٷֱ�
//	 */
//	public void saveSplitPercentWhenLogout() {
//		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
//		Enumeration<LetNode> enumLet = letInfo.depthFirstEnumeration();
//		while (enumLet.hasMoreElements()) {
//			LetNode lNode = enumLet.nextElement();
//			if (!lNode.getCode().equals(LetNode.VIRTUAL_NODE_CODE)) {
//				//��ϢƬ�ĸ�SplitPane
//				AbstractMessageLet aml = hmLets.get(lNode.getCode());
//
//				Container ct = aml.getParent();
//				if (!(ct instanceof UISplitPane))
//					continue;
//				UISplitPane sp = (UISplitPane) aml.getParent();
//				double splitPercent = 0.5;
//				if (sp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
//					splitPercent = (double) sp.getDividerLocation() / (sp.getWidth() - sp.getDividerSize());
//				else
//					splitPercent = (double) sp.getDividerLocation() / (sp.getHeight() - sp.getDividerSize());
//				((LetNode) lNode.getParent()).setSplitPercent(splitPercent);
//			}
//		}
//
//		//��������Ϣ���浽�ļ�����
//		MessageRepository.getInstance().msgPanelOptionsChanged();
//	}
//
//	/**
//	 * �ر�ĳ����ϢƬ
//	 * @param strCode
//	 */
//	public void closeLet(String strCode) {
//		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
//		LetNode lNode = findLetNode(letInfo, strCode);
//		LetNode lParent = (LetNode) lNode.getParent();
//		LetNode lParentParent = (LetNode) lParent.getParent();
//		if (lParentParent == null) {
//			letInfo = getAnotherChild(lNode);
//			letInfo.removeFromParent();
//			MessageRepository.getInstance().getMsgPanelOptions().setLetInfo(letInfo);
//		} else {
//			int index = lParentParent.getIndex(lParent);
//			lParentParent.remove(lParent);
//			lParentParent.insert(getAnotherChild(lNode), index);
//		}
//
//		//����ϢƬ������ɾ������ϢƬ
//		hmLets.remove(strCode);
//		//��������Ϣ���浽�ļ�����
//		MessageRepository.getInstance().msgPanelOptionsChanged();
//
//		//���²���
//		reLayout();
//		revalidate();
//	}
//
//	/**
//	 * ����ĳ����ϢƬ 
//	 * @param strCode ԭʼ��ϢƬ
//	 * @param strNewCode ����ϢƬ
//	 * @param iOrientation
//	 */
//	public void addLet(String strCode, String strNewCode, int iOrientation) {
//		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
//		LetNode lNode = findLetNode(letInfo, strCode);
//		LetNode lParent = (LetNode) lNode.getParent();
//
//		if (lParent == null) {
//			LetNode virtualNode = new LetNode(LetNode.VIRTUAL_NODE_CODE);
//			virtualNode.add(lNode);
//			virtualNode.add(new LetNode(strNewCode));
//			virtualNode.setSplitPercent(0.5);
//			virtualNode.setSplitDirection(iOrientation);
//			MessageRepository.getInstance().getMsgPanelOptions().setLetInfo(virtualNode);
//		} else {
//			int index = lParent.getIndex(lNode);
//			LetNode virtualNode = new LetNode(LetNode.VIRTUAL_NODE_CODE);
//			virtualNode.add(lNode);
//			virtualNode.add(new LetNode(strNewCode));
//			virtualNode.setSplitPercent(0.5);
//			virtualNode.setSplitDirection(iOrientation);
//			lParent.insert(virtualNode, index);
//		}
//
//		//��������Ϣ���浽�ļ�����
//		MessageRepository.getInstance().msgPanelOptionsChanged();
//
//		//���²���
//		reLayout();
//		revalidate();
//	}
//
//	/**
//	 * ��ö�������ĳ�����ӵ����ں���
//	 * @param childNode
//	 * @return
//	 */
//	private LetNode getAnotherChild(LetNode childNode) {
//		LetNode nextNode = (LetNode) childNode.getNextSibling();
//		if (nextNode != null)
//			return nextNode;
//		LetNode previousNode = (LetNode) childNode.getPreviousSibling();
//		if (previousNode != null)
//			return previousNode;
//		throw new IllegalArgumentException("����Ĳ�����" + childNode.getCode());
//	}
//
//	private LetNode findLetNode(LetNode rootNode, String strCode) {
//		Enumeration<LetNode> enumLet = rootNode.depthFirstEnumeration();
//		while (enumLet.hasMoreElements()) {
//			LetNode lNode = enumLet.nextElement();
//			if (lNode.getCode().equals(strCode))
//				return lNode;
//		}
//		return null;
//	}
//
//	/**
//	 * ������Ϣ���ն��� 
//	 */
//	public MessageReceiveObject getMsgReceiveObject() {
//		if (m_msgReceiveObject == null) {
//			m_msgReceiveObject = new MessageReceiveObject(WorkbenchEnvironment.getInstance()
//					.getLoginUser().getPrimaryKey());
//			m_msgReceiveObject.setPkgroup(PfUtilUITools.getLoginGroup());
//		}
//		return m_msgReceiveObject;
//	}
//
//	/**
//	 * ������Ϣ�����߳�
//	 */
//	public MessageReceiveThread getMsgReceiveThread() {
//		return m_msgReceiveThread;
//	}
//
//	public UISplitPane createSplitPane(int orientation) {
//		UISplitPane sp = new UISplitPane(orientation) {
//			public void paint(Graphics g) {
//				super.paint(g);
//				// Ϊ��ʹ�ָ����͵�ɫ����һ��,�����൱������
//				g.setColor(defBackColor);// this.getBackground());
//				if (getOrientation() == JSplitPane.VERTICAL_SPLIT)
//					g.fillRect(0, getDividerLocation(), this.getWidth(), getDividerSize());
//				else
//					g.fillRect(getDividerLocation(), 0, getDividerSize(), this.getWidth());
//			}
//		};
//		sp.setOpaque(false);
//		sp.setName("SpMessage");
//		sp.setDividerSize(2);
//		sp.setBorder(null);
//		return sp;
//	}
//
//	/**
//	 * ��ĳ��UI��� ���в����
//	 */
//	public void centerMax(Component comp) {
//		this.removeAll();
//		this.setLayout(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.BOTH;
//		gbc.insets = new Insets(4, 4, 4, 4);
//		gbc.weightx = 1;
//		gbc.weighty = 1;
//		add(comp, gbc);
//	}
//
//	public void reLayout() {
//		centerMax(createCenterComponent());
//
//		//��Ϣ����
//		WorkbenchEnvironment.getInstance().getWorkbench().insertCompToTopBar(
//				MessageCountLabel.getInstance());
//	}
//
//	/**
//	 * ֹͣ������Ϣ
//	 */
//	public void stopReceiveMessage() {
//		if (getMsgReceiveThread() != null) {
//			getMsgReceiveThread().setStopThread();
//		}
//	}
//
//	// The method is critical .It maybe execute when update that login and
//	// startRefreshThread.
//	public void update(Observable o, Object arg) {
////		if (arg != null && arg instanceof MessageVO)
////			return;
////		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();
////
////		//������Ϣ����е�������ϢƬ
////		Iterator<AbstractMessageLet> iter = hmLets.values().iterator();
////		while (iter.hasNext()) {
////			IMessageLet msgLet = iter.next();
////			if (msgLet instanceof PfMessageLet) {
////				//ֻ��ƽ̨��ϢƬ����Ҫ����
////				PfMessageLet mLet = (PfMessageLet) msgLet;
////				mLet.filterMsgs(mpo.getFilterByName(mLet.getCode()), true);
////			}
////		}
//
//	}
//
//	/**
//	 * ר��ΪPortalʹ�õ���Ϣ������
//	 * @param msg
//	 * @param strCode ������MessagePanelOptions.MESSAGE_WORKLIST
//	 */
//	public void acceptMessageForPortal(MessageVO msg, String strCode) {
//		//��ѯ��Ϣ������Ƿ�����ƽ̨��ϢƬ
////		AbstractMessageLet aml = hmLets.get(strCode);
////		if (aml instanceof PfMessageLet) {
////			((PfMessageLet) aml).acceptMessage(msg);
////		} else {
////			MessageLetVO letVo = PfUIDataCache.getMessageLet(strCode);
////			if (letVo == null) {
////				MessageDialog.showErrorDlg(this, "����", "�Ҳ�������ϢƬ��ע����Ϣ��" + strCode);
////				return;
////			}
////			//�´�������ϢƬ��Ȼ�������ϢVO
////			try {
////				Constructor construct = Class.forName(letVo.getUiclass()).getConstructor(this.getClass());
////				PfMessageLet pfLet = (PfMessageLet) construct.newInstance(this);
////				pfLet.acceptMessage(msg);
////			} catch (Exception e) {
////				Logger.error(e.getMessage(), e);
////				MessageDialog.showErrorDlg(this, "����", "������Ϣʱ�����쳣��" + e.getMessage());
////			}
////		}
//	}
}