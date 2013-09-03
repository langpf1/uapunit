package uap.workflow.modeler.uecomponent;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;

public class UIOKCancelPanel extends UIPanel implements ActionListener{
	
	private UIButton ivjUIButtonOK;
	private UIButton ivjUIButtonCancel;
	private UIPanel ivjUIPanelButton = null; //Ok,Cancel按钮Panel
	private UIPanel ivjUIPanelMain = null; //主Panel
	
	public UIOKCancelPanel(){
		super();
		setLayout(new BorderLayout());
		add(getUIPanelMain(),BorderLayout.SOUTH);
		add(getUIPanelButton(),BorderLayout.CENTER);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Object o = e.getSource();
		if (o == getUIButtonOK()) {
			onButtonOK();
		} else
			if (o == getUIButtonCancel()) {
				onButtonCancel();
			}
	}
	
	//子类覆盖
	public UIPanel getUIPanelMain(){
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
	
	//子类复写
	public void onButtonOK(){
		
	}
	//子类复写
	public void onButtonCancel(){
		
	}
	
	private UIPanel getUIPanelButton() {

		if (ivjUIPanelButton == null) {
			try {
				ivjUIPanelButton = new UIPanel();
				ivjUIPanelButton.setName("UIPanelButton");
//				ivjUIPanelButton.setPreferredSize(new java.awt.Dimension(200, 80));
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
	
	
	public nc.ui.pub.beans.UIButton getUIButtonOK() {
		if (ivjUIButtonOK == null) {
			try {
				ivjUIButtonOK = new nc.ui.pub.beans.UIButton();
				ivjUIButtonOK.setName("UIButtonOK");
				//ivjUIButtonOK.setText("确 定( O )");
				// user code begin {1}
				ivjUIButtonOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/ + "(O)");
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
	
	public nc.ui.pub.beans.UIButton getUIButtonCancel() {
		if (ivjUIButtonCancel == null) {
			try {
				ivjUIButtonCancel = new nc.ui.pub.beans.UIButton();
				ivjUIButtonCancel.setName("UIButtonCancel");
				//ivjUIButtonCancel.setText("取 消");
				// user code begin {1}
				ivjUIButtonCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/ + "(X)");
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
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {


	}
}
