package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

import uap.workflow.vo.WorkflownoteVO;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.wfengine.designer.ResManager;
import nc.ui.wfengine.ext.IApplicationRuntimeAdjust;
import nc.ui.wfengine.ext.add.WFOrgTreeToListPanel;
import nc.vo.uap.pf.OrganizeUnit;

/**
 * ��ǩ�Ի���
 * @modifier zhangrui ��ȡ����ť�ſ���������������� 2012-3-15
 * @author 
 */
public class AddAssignDialog extends UIDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	
	private WFOrgTreeToListPanel addAssignPanel;
	
	int style = -1;
	
	/*��ť*/
	private UIPanel btnPane = null;
	private UIButton btnOK;
	private UIButton btnCancel;
	private UIPanel sortButtonPane;
	private UIButton upBtn;
	private UIButton downBtn;
	private UIPanel _addStypePanel = null;
	private ButtonGroup radioGroup = new ButtonGroup();
	
	private List<OrganizeUnit> orgUnits=null;
	

	private UIPanel centerPanel;
	
	public AddAssignDialog(Container parent,WorkflownoteVO m_workFlow){
		super(parent);
				
		//��ʼ��UI
		initUI();
		initListener();
	}
	
	/**
	 * ��ʼ��UI
	 */
	private void initUI(){
		setTitle(NCLangRes.getInstance().getStrByID("pfworkflow1", "AddAssignDialog-000000")/*��������ת��Ϣ*/);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(500, 400);
		setLayout(new BorderLayout());
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getBtnPane(), BorderLayout.SOUTH);
		
	}
	
	private void initListener(){
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		upBtn.addActionListener(this);
		downBtn.addActionListener(this);
	}
	
	public WFOrgTreeToListPanel getAddAssignPanel(){
		if(addAssignPanel == null){
			addAssignPanel = new WFOrgTreeToListPanel();
		}
		return addAssignPanel;
	}
	
	private UIPanel getCenterPanel(){
		if(centerPanel == null){
			centerPanel = new UIPanel(new BorderLayout());
			centerPanel.add(getAddAssignPanel(),BorderLayout.CENTER);
			centerPanel.add(getSortButtonPanel(),BorderLayout.EAST);
			centerPanel.add(getAddStylePane(),BorderLayout.SOUTH);
		}
		return centerPanel;
	}
	
	/**
	 * ��ȡ��ǩ��ʽ���(�������С�����)
	 * @return
	 */
	private UIPanel getAddStylePane(){
		if(_addStypePanel == null){
			_addStypePanel = new UIPanel(new GridLayout(1,6));
			_addStypePanel.setBounds(0,400,400,40);
			UIRadioButton rbCooperation = new UIRadioButton(NCLangRes.getInstance().getStrByID("pfworkflow1", "AddAssignDialog-000001")/*Э��*/);
			rbCooperation.setName(String.valueOf(IApplicationRuntimeAdjust.ADDASSIGN_STYLE_COOPERATION));
//			UIRadioButton rbNotice = new UIRadioButton("֪��");
//			rbNotice.setName(String.valueOf(IApplicationRuntimeAdjust.ADDASSIGN_STYLE_PARALLEL));
//			UIRadioButton rbSerial = new UIRadioButton("����");
//			rbSerial.setName(String.valueOf(IApplicationRuntimeAdjust.ADDASSIGN_STYLE_SERIAL));
//			UIRadioButton rbParallel = new UIRadioButton("����");
//			rbParallel.setName(String.valueOf(IApplicationRuntimeAdjust.ADDASSIGN_STYLE_PARALLEL));
			radioGroup.add(rbCooperation);
//			radioGroup.add(rbNotice);
//			radioGroup.add(rbSerial);
//			radioGroup.add(rbParallel);
			rbCooperation.setSelected(true);
			UILabel label1 = new UILabel("");
			UILabel label2 = new UILabel("");
			UILabel label3 = new UILabel("");
			UILabel label4 = new UILabel("");
			
			_addStypePanel.add(rbCooperation);
			//_addStypePanel.add(rbNotice);
//			_addStypePanel.add(rbSerial);
//			_addStypePanel.add(rbParallel);
			_addStypePanel.add(label1);
			_addStypePanel.add(label2);
			_addStypePanel.add(label3);
			_addStypePanel.add(label4);
		}
		return _addStypePanel;
	}
	
	/**
	 * ��ȡ����ť���
	 * @return
	 */
	private UIPanel getSortButtonPanel(){
		if(sortButtonPane == null){
			ImageIcon upIcon = new ImageIcon(ResManager.loadImage("nc/ui/wfengine/designer/resources/arrowup.gif"));
			ImageIcon dowIcon = new ImageIcon(ResManager.loadImage("nc/ui/wfengine/designer/resources/arrowdown.gif"));
			sortButtonPane = new UIPanel(new GridLayout(10,1));
			sortButtonPane.setBounds(20, 0, 20, 200);
			sortButtonPane.setPreferredSize(new Dimension(20,200));
			upBtn = new UIButton(upIcon);
			upBtn.setPreferredSize(new Dimension(20,50));
			downBtn = new UIButton(dowIcon);
			downBtn.setPreferredSize(new Dimension(20,50));
			sortButtonPane.add(upBtn);
			sortButtonPane.add(downBtn);
		}
		return sortButtonPane;
	}
	
	/**
	 * ��ť��Ϊ����
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			onOK();
		} else if (e.getSource() == getBtnCancel()) {
			closeCancel();
		} else if(e.getSource() == upBtn){
			onUp();
		} else if(e.getSource() == downBtn){
			onDown();
		}
	}
	
	/**
	 * ȷ��
	 */
	protected void onOK() {
		setResult(ID_OK);
		orgUnits = addAssignPanel.getSelectedOrg();
	/*	style = getAddAssignStyle();
		if(style == IApplicationRuntimeAdjust.ADDASSIGN_STYLE_PARALLEL && orgUnits.size() < 2) {
			MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("pfworkflow1", "AddAssignDialog-000002")��ǩ��ʽѡ�񲢷�ʱ������Ҫѡ�������û����ɫ��);
			return;
		}*/
		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_OK));
		return;
	}

	public List<OrganizeUnit> getOrgUnits() {
		return orgUnits;
	}

	public void setOrgUnits(List<OrganizeUnit> orgUnits) {
		this.orgUnits = orgUnits;
	}
	
	public int getAddAssignStyle(){
		Enumeration<AbstractButton> buttons = radioGroup.getElements();
		UIRadioButton selectedRadioButton = null;
		while(buttons.hasMoreElements()){
			selectedRadioButton = (UIRadioButton)buttons.nextElement();
			if(selectedRadioButton.isSelected()){
				break;
			}
		}
		
		return Integer.parseInt(selectedRadioButton.getName());
	}
	
	/**
	 * �ԡ�ȡ����ģʽ�رնԻ��� ҵ��ڵ������Ҫ�޸�
	 */
	public void closeCancel() {
		setResult(ID_CANCEL);
		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
		return;
	}
	
	private void onUp(){
		UIList rList = addAssignPanel.getLstRight();
		DefaultListModel _listModel = (DefaultListModel)rList.getModel();
		int selectedIndex = rList.getSelectedIndex();
		if (selectedIndex > 0) {
			Object o = _listModel.getElementAt(selectedIndex);
			_listModel.remove(selectedIndex);
			_listModel.insertElementAt(o, selectedIndex - 1);
			rList.setSelectedIndex(selectedIndex - 1);
		}
	}
	
	private void onDown(){
		UIList rList = addAssignPanel.getLstRight();
		DefaultListModel _listModel = (DefaultListModel)rList.getModel();
		int selectedIndex = rList.getSelectedIndex();
		if (selectedIndex >= _listModel.getSize() - 1)
			return;
		Object o = _listModel.getElementAt(selectedIndex);
		_listModel.remove(selectedIndex);
		_listModel.insertElementAt(o, selectedIndex + 1);
		rList.setSelectedIndex(selectedIndex + 1);
	}
	
	/**
	 * ��ťpanel
	 * @return
	 */
	private UIPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new UIPanel();
			btnPane.add(getBtnOK(), null);
			btnPane.add(getBtnCancel(), null);
		}
		return btnPane;
	}
	
	/**
	 * ���ȷ����ť
	 * @return
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setText(NCLangRes.getInstance().getStrByID("pfworkflow1", "EmailSmsDlg-000013")/*ȷ��*/);
		}
		return btnOK;
	}
	
	/**
	 * ���ȡ����ť
	 * @return
	 */
	public UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setText(NCLangRes.getInstance().getStrByID("pfworkflow1", "SplitRuleRegisterUI-000011")/*ȡ��*/);
		}
		return btnCancel;
	}

	public int getStyle() {
		return style;
	}
}
