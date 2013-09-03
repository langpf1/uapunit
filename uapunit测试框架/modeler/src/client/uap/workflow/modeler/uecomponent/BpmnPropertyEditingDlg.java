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
	private UIPanel ivjUIPanelMain = null; // 主Panel
	private UIPanel ivjUIPanelButton = null; // Ok,Cancel按钮Panel
	protected PropertyEditorSupport propertyEditor;
	private List<IPushOkandCancelListener> listeners = new ArrayList<IPushOkandCancelListener>();
	// 默认是OKCancelDlg
	private boolean isOkCancelDlg = true;
	/**
	 * TempletChooserDlg 构造子注解.
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
	 * 此处插入方法说明. 创建日期:(2003-03-25 12:45:35)
	 * 
	 * @return java.lang.String
	 */
	public String checkError() {
		return null;
	}
	/**
	 * 此处插入方法说明. 创建日期:(2003-03-25 12:45:35)
	 * 
	 * @return java.lang.String
	 */
	public String checkWarning() {
		return null;
	}
	/**
	 * 返回 UIButtonCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	public nc.ui.pub.beans.UIButton getUIButtonCancel() {
		if (ivjUIButtonCancel == null) {
			try {
				ivjUIButtonCancel = new nc.ui.pub.beans.UIButton();
				ivjUIButtonCancel.setName("UIButtonCancel");
				// ivjUIButtonCancel.setText("取 消");
				// user code begin {1}
				ivjUIButtonCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*
																												 * @
																												 * res
																												 * "取消"
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
	 * 返回 UIButtonOK 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	public nc.ui.pub.beans.UIButton getUIButtonOK() {
		if (ivjUIButtonOK == null) {
			try {
				ivjUIButtonOK = new nc.ui.pub.beans.UIButton();
				ivjUIButtonOK.setName("UIButtonOK");
				// ivjUIButtonOK.setText("确 定( O )");
				// user code begin {1}
				ivjUIButtonOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*
																											 * @
																											 * res
																											 * "确定"
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
	 * 返回 UIDialogContentPane 特性值.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告:此方法将重新生成. */
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
	 * 返回 UIPanel1 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. */
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
	 * 返回 UIPanel2 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. 需要实现IAssemberPropertyData接口 */
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
	 * 每当部件抛出异常时被调用
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
	 * 此处插入方法描述. 创建日期:(2003-3-12 16:15:20)
	 */
	public void onButtonCancel() {
		doAfterCancelButtonClicked();
		this.closeCancel();
		firePushCancelListener();
	}
	/**
	 * 此处插入方法描述. 创建日期:(2003-3-12 16:15:08)
	 */
	public void onButtonOK() {
		// String msg = checkError(); //对于错误,提示后返回
		// if (msg != null) {
		// MessageDialog.showErrorDlg(this,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template","UPP_Template-000065")/*@res
		// "错误"*/, msg);
		// return;
		// }
		// msg = checkWarning(); //对于警告,提示是否继续
		// if (msg != null) {
		// if (MessageDialog.showOkCancelDlg(this,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template","UPP_Template-000033")/*@res
		// "提示"*/, msg) != MessageDialog.ID_OK)
		// return;
		// }
		//
		// this.closeOK();
		try{
			doAfterOKButtonClicked();
			this.closeOK();
			firePushOkListener();
		}catch(Exception e){
			MessageBox.showMessageDialog("提示", e.getMessage());
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