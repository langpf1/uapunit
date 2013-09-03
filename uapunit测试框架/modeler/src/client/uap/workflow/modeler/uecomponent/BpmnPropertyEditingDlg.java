package uap.workflow.modeler.uecomponent;
import java.awt.event.KeyEvent;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
public abstract class BpmnPropertyEditingDlg extends nc.ui.pub.beans.UIDialog implements java.awt.event.ActionListener {
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UIButton ivjUIButtonCancel = null;
	private UIButton ivjUIButtonOK = null;
	private UIPanel ivjUIPanelMain = null; // ��Panel
	private UIPanel ivjUIPanelButton = null; // Ok,Cancel��ťPanel
	protected PropertyEditorSupport propertyEditor;
	private List<IPushOkandCancelListener> listeners = new ArrayList<IPushOkandCancelListener>();
	// Ĭ����OKCancelDlg
	private boolean isOkCancelDlg = true;
	/**
	 * TempletChooserDlg ������ע��.
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public BpmnPropertyEditingDlg(java.awt.Container parent) {
		super(parent);
		setSize(800, 400);
		setResizable(true);
		setContentPane(getUIDialogContentPane());
	}
	public boolean isOkCancelDlg() {
		return isOkCancelDlg;
	}
	public void setOkCancelDlg(boolean isOkCancelDlg) {
		this.isOkCancelDlg = isOkCancelDlg;
	}
	public void firePushOkListener() {
		for (IPushOkandCancelListener listener : listeners) {
			listener.pushOkStopEditing();
		}
	}
	public void firePushCancelListener() {
		for (IPushOkandCancelListener listener : listeners) {
			listener.pushCancelstopEditing();
		}
	}
	public void addPushOkandCancelListener(IPushOkandCancelListener listener) {
		listeners.add(listener);
	}
	public void removePushOkandCancelListener(IPushOkandCancelListener listener) {
		listeners.remove(listener);
	}
	public void SetPropertys(Object obj) {
		if (getUIPanelMain() instanceof IAssemberPropertyData) {
			((IAssemberPropertyData) getUIPanelMain()).unassemberData(obj);
		}
	}
	public Object getPropertys() {
		if (getUIPanelMain() instanceof IAssemberPropertyData) {
			return ((IAssemberPropertyData) getUIPanelMain()).assemberData();
		}
		return null;
	}
	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Object o = e.getSource();
		if (o == getUIButtonOK()) {
			onButtonOK();
		} else if (o == getUIButtonCancel()) {
			onButtonCancel();
		}
	}
	/**
	 * �˴����뷽��˵��. ��������:(2003-03-25 12:45:35)
	 * 
	 * @return java.lang.String
	 */
	public String checkError() {
		return null;
	}
	/**
	 * �˴����뷽��˵��. ��������:(2003-03-25 12:45:35)
	 * 
	 * @return java.lang.String
	 */
	public String checkWarning() {
		return null;
	}
	/**
	 * ���� UIButtonCancel ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	public nc.ui.pub.beans.UIButton getUIButtonCancel() {
		if (ivjUIButtonCancel == null) {
			try {
				ivjUIButtonCancel = new nc.ui.pub.beans.UIButton();
				ivjUIButtonCancel.setName("UIButtonCancel");
				// ivjUIButtonCancel.setText("ȡ ��");
				// user code begin {1}
				ivjUIButtonCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*
																												 * @
																												 * res
																												 * "ȡ��"
																												 */+ "(X)");
				ivjUIButtonCancel.setMnemonic('X');
				ivjUIButtonCancel.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonCancel;
	}
	/**
	 * ���� UIButtonOK ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	public nc.ui.pub.beans.UIButton getUIButtonOK() {
		if (ivjUIButtonOK == null) {
			try {
				ivjUIButtonOK = new nc.ui.pub.beans.UIButton();
				ivjUIButtonOK.setName("UIButtonOK");
				// ivjUIButtonOK.setText("ȷ ��( O )");
				// user code begin {1}
				ivjUIButtonOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*
																											 * @
																											 * res
																											 * "ȷ��"
																											 */+ "(O)");
				ivjUIButtonOK.setMnemonic('O');
				ivjUIButtonOK.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonOK;
	}
	/**
	 * ���� UIDialogContentPane ����ֵ.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ����:�˷�������������. */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				// user code begin {1}
				ivjUIDialogContentPane.add(getUIPanelMain(), java.awt.BorderLayout.CENTER);
				if (isOkCancelDlg) {
					ivjUIDialogContentPane.add(getUIPanelButton(), java.awt.BorderLayout.SOUTH);
				}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}
	/**
	 * ���� UIPanel1 ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ����:�˷�������������. */
	private UIPanel getUIPanelButton() {
		if (ivjUIPanelButton == null) {
			try {
				ivjUIPanelButton = new UIPanel();
				ivjUIPanelButton.setName("UIPanelButton");
				// ivjUIPanelButton.setPreferredSize(new java.awt.Dimension(200,
				// 80));
				ivjUIPanelButton.add(getUIButtonOK(), getUIButtonOK().getName());
				ivjUIPanelButton.add(getUIButtonCancel(), getUIButtonCancel().getName());
				ivjUIPanelButton.setOpaque(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanelButton;
	}
	/**
	 * ���� UIPanel2 ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ����:�˷�������������. ��Ҫʵ��IAssemberPropertyData�ӿ� */
	public UIPanel getUIPanelMain() {
		if (ivjUIPanelMain == null) {
			try {
				ivjUIPanelMain = new nc.ui.pub.beans.UIPanel();
				ivjUIPanelMain.setName("ivjUIPanelMain");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanelMain;
	}
	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {}
	protected void hotKeyPressed(javax.swing.KeyStroke hotKey, java.awt.event.KeyEvent e) {
		int modifiers = hotKey.getModifiers();
		if (modifiers != 0) {
			// Combined hot key:
			switch (hotKey.getKeyCode()) {
			case KeyEvent.VK_O:
				onButtonOK();
				break;
			case KeyEvent.VK_X:
				onButtonCancel();
				break;
			}
		} else {
			switch (hotKey.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				onButtonCancel();
				break;
			}
		}
	}
	/**
	 * �˴����뷽������. ��������:(2003-3-12 16:15:20)
	 */
	public void onButtonCancel() {
		doAfterCancelButtonClicked();
		this.closeCancel();
		firePushCancelListener();
	}
	/**
	 * �˴����뷽������. ��������:(2003-3-12 16:15:08)
	 */
	public void onButtonOK() {
		// String msg = checkError(); //���ڴ���,��ʾ�󷵻�
		// if (msg != null) {
		// MessageDialog.showErrorDlg(this,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template","UPP_Template-000065")/*@res
		// "����"*/, msg);
		// return;
		// }
		// msg = checkWarning(); //���ھ���,��ʾ�Ƿ����
		// if (msg != null) {
		// if (MessageDialog.showOkCancelDlg(this,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template","UPP_Template-000033")/*@res
		// "��ʾ"*/, msg) != MessageDialog.ID_OK)
		// return;
		// }
		//
		// this.closeOK();
		try{
			doAfterOKButtonClicked();
			this.closeOK();
			firePushOkListener();
		}catch(Exception e){
			MessageBox.showMessageDialog("��ʾ", e.getMessage());
		}
	}
	protected void doAfterOKButtonClicked() {
		propertyEditor.setValue(getPropertys());
		propertyEditor.firePropertyChange();
		if (getUIPanelMain() instanceof AbstractTablePane) {
			((AbstractTablePane) getUIPanelMain()).doAfterOKButtonClicked();
		}
	}
	protected void doAfterCancelButtonClicked() {
		if (getUIPanelMain() instanceof AbstractTablePane) {
			((AbstractTablePane) getUIPanelMain()).doAfterCancelButtonClicked();
		}
	}
}