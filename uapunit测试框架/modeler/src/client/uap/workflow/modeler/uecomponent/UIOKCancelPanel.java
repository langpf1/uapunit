package uap.workflow.modeler.uecomponent;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;

public class UIOKCancelPanel extends UIPanel implements ActionListener{
	
	private UIButton ivjUIButtonOK;
	private UIButton ivjUIButtonCancel;
	private UIPanel ivjUIPanelButton = null; //Ok,Cancel��ťPanel
	private UIPanel ivjUIPanelMain = null; //��Panel
	
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
	
	//���า��
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
	
	//���ิд
	public void onButtonOK(){
		
	}
	//���ิд
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
				//ivjUIButtonOK.setText("ȷ ��( O )");
				// user code begin {1}
				ivjUIButtonOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "ȷ��"*/ + "(O)");
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
				//ivjUIButtonCancel.setText("ȡ ��");
				// user code begin {1}
				ivjUIButtonCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/ + "(X)");
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
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {


	}
}
