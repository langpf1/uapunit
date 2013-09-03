package uap.workflow.ui.workitem;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.component.RoleUserTreeScrollPnl;
import nc.ui.pub.component.RoleUserTreeToList;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.RoleUserParaVO;

import com.borland.jbcl.layout.VerticalFlowLayout;

/**
 * ���͵��û�ѡ��tree2List ����д������������С��չ����
 * @modifier zhangrui ���Ƴ���ť�ſ� ������������� 2012-3-15
 * @author zhouzhenga 2010-5-11
 *
 */
public class CpySendUserTree2List extends RoleUserTreeToList {

	private HashMap<String, OrganizeUnit> msgMap = new HashMap<String, OrganizeUnit>();

	private HashMap<String, OrganizeUnit> mailMap = new HashMap<String, OrganizeUnit>();

	private UIButton msgAddBtn = null;

	private UIButton mailAddBtn = null;

	private UIButton msgRemoveBtn = null;

	private UIPanel BtnPane = null;

	public CpySendUserTree2List(RoleUserParaVO paravo) {
		super(paravo);
		getRoleuserScrollpane().getRoleUserTree().setRootVisible(false);
		expandTree(getRoleuserScrollpane().getRoleUserTree(), new TreePath(
				(TreeNode) getRoleuserScrollpane().getRoleUserTree().getModel().getRoot()));
		getRoleuserScrollpane().getRoleUserTree().updateUI();
		//		getUserRoleList().setCellRenderer(new myCellRenderer());
		initListener();
	}

	private UIButton getBtnMsgAdd() {//���Ͳ�����Ϣ��ʽ
		if (msgAddBtn == null) {
			msgAddBtn = new UIButton();
			msgAddBtn.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000261")/*
																																												* @res "��Ϣ��ʽ"
																																												*/);
		}
		return msgAddBtn;
	}

	private UIButton getBtnMailAdd() {//���Ͳ����ʼ���ʽ
		if (mailAddBtn == null) {
			mailAddBtn = new UIButton();
			mailAddBtn.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000263")/*
																																													* @res "�ʼ���ʽ"
																																													*/);
		}
		return mailAddBtn;
	}

	private UIButton getBtnRemove() {//�Ƴ�
		if (msgRemoveBtn == null) {
			msgRemoveBtn = new UIButton();
			msgRemoveBtn.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000262")/*
																																														* @res "�Ƴ�"
																																														*/);
		}
		return msgRemoveBtn;
	}

	protected UIPanel getMovePnl() {
		if (BtnPane == null) {
			BtnPane = new UIPanel();
			VerticalFlowLayout vFLayout = new VerticalFlowLayout();
			vFLayout.setVgap(10);
			vFLayout.setAlignment(VerticalFlowLayout.MIDDLE);
			vFLayout.setHorizontalFill(false);
			BtnPane.setLayout(vFLayout);
			BtnPane.add(getBtnMailAdd());
			BtnPane.add(getBtnMsgAdd());
			BtnPane.add(getBtnRemove());
		}
		return BtnPane;
	}

	protected void initListener() {
		getBtnMsgAdd().addActionListener(this);
		getBtnMailAdd().addActionListener(this);
		getBtnRemove().addActionListener(this);
	}

	/**
	 * ���������б��С
	 */
	protected UIScrollPane getListScrollPane() {
		super.getListScrollPane().setPreferredSize(new Dimension(280, 280));
		return super.getListScrollPane();
	}

	/**
	 * ������������С
	 */
	protected RoleUserTreeScrollPnl getRoleuserScrollpane() {
		super.getRoleuserScrollpane().setPreferredSize(new Dimension(280, 280));
		return super.getRoleuserScrollpane();
	}

	/**
	 * ���²���
	 */
	public void layoutPanel() {
		Box box = Box.createHorizontalBox();
		box.add(getRoleuserScrollpane());
		Box.createHorizontalStrut(0);
		box.add(getMovePnl());
		Box.createHorizontalStrut(10);
		box.add(getListScrollPane());
		Box.createHorizontalStrut(0);
		this.add(box);
	}

	/**
	 * չ����
	 */
	private void expandTree(JTree tree, TreePath parentPath) {
		TreeNode parent = (TreeNode) parentPath.getLastPathComponent();
		if (parent.getChildCount() > 0) {
			for (int i = 0; i < parent.getChildCount(); i++) {
				expandTree(tree, parentPath.pathByAddingChild(parent.getChildAt(i)));
			}
		} else {
			return;
		}
		tree.expandPath(parentPath);
	}

	//��д��Ϊ�����ֳ�������µ����ֲ�ͬ�Ĵ��ͷ���--??��֪��Ϊʲô
	public void actionPerformed(ActionEvent e) {
		String code = ((UIButton) e.getSource()).getText();
		if (code.equals(getBtnMailAdd().getText())) {

			onMailAddBtnClick();
		} else if (code.equals(getBtnMsgAdd().getText())) {

			onMsgAddBtnClick();
		} else if (code.equals(getBtnRemove().getText())) {
			onMsgRemoveBtnClick();
		}
	}

	//���ʼ���ʽ����
	private void onMailAddBtnClick() {
		TreePath[] paths = getRoleuserScrollpane().getRoleUserTree().getSelectionPaths();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				addNodeDataToList(node);
				OrganizeUnit roleuservo = (OrganizeUnit) node.getUserObject();
				if (roleuservo != null && !mailMap.containsKey(composeListMapKey(roleuservo))) {
					putMapKey(mailMap, roleuservo);
				}
			}
		}
	}

	//����Ϣ��ʽ����	
	private void onMsgAddBtnClick() {
		TreePath[] paths = getRoleuserScrollpane().getRoleUserTree().getSelectionPaths();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				addNodeDataToList(node);
				OrganizeUnit roleuservo = (OrganizeUnit) node.getUserObject();
				//��Ϣ��ӵ�mesgeMap��
				if (roleuservo != null && !msgMap.containsKey(composeListMapKey(roleuservo))) {
					putMapKey(msgMap, roleuservo);
				}
			}
		}
	}

	//�Ƴ�
	public void onMsgRemoveBtnClick() {
		int[] selindexs = getUserRoleList().getSelectedIndices();
		int maxIndex = 0;
		for (int i = selindexs.length - 1; i >= 0; i--) {
			OrganizeUnit orgUnit = (OrganizeUnit) getListModel().getElementAt(selindexs[i]);
			getListModel().removeElementAt(selindexs[i]);
			msgMap.remove(composeListMapKey(orgUnit));
			mailMap.remove(composeListMapKey(orgUnit));
			removeMapKey(composeListMapKey(orgUnit));
			maxIndex = Math.max(selindexs[i], maxIndex);
		}

		maxIndex = maxIndex - (selindexs.length - 1);
		if (maxIndex < getListModel().getSize() - 1) {
			getUserRoleList().setSelectedIndex(maxIndex);
		} else
			getUserRoleList().setSelectedIndex(getListModel().getSize() - 1);
	}

	/**@return ���ʼ���ʽ���͵Ľ������
	 * */
	public List<String> getMailVOs() {
		Collection<OrganizeUnit> orus = mailMap.values();
		List<String> userNames = new ArrayList<String>();
		for (OrganizeUnit oru : orus)
			userNames.add(oru.getPk());
		return userNames;
	}

	/**�����͵ķ�ʽ����
	 * */
	private void sortBySendType() {

	}

	/**@return ������Ϣ��ʽ���͵Ľ������
	 * */
	public List<String> getMsgVOs() {
		Collection<OrganizeUnit> orus = msgMap.values();
		List<String> userNames = new ArrayList<String>();
		for (OrganizeUnit oru : orus)
			userNames.add(oru.getPk());
		return userNames;
	}

	//MAIL���͵ļ�ǰ׺ MAIL :
	class myCellRenderer extends JLabel implements ListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub			
			if (!(value instanceof OrganizeUnit))
				return null;
			String s = value.toString();
			setText(msgMap.containsKey(composeListMapKey(value)) ? s : "MAIL: " + s);
			return this;
		}
	}

}