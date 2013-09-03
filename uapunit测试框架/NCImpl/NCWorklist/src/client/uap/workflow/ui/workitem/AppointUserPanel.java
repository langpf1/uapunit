package uap.workflow.ui.workitem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.wfengine.designer.LazyUserOrgTree;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.pf.RoleUserParaVO;

import com.borland.jbcl.layout.VerticalFlowLayout;

/**
 * 角色用户树->List 的选择对话框
 */

public class AppointUserPanel extends UIPanel implements ActionListener {

	private static final long serialVersionUID = 123432432141L;

	public static int VERTI_HEIGHT = 5; // 按钮竖直间距

	private UIScrollPane userTreePanel = null;
	
	private LazyUserOrgTree userTree = null;

	private UIPanel movePane = null;

	private UIButton rightBt = null;

	private UIButton leftBt = null;

	private UIList listroleuser = null;

	private UIScrollPane userScroll = null; // @jve:decl-index=0:visual-constraint="102,180"

	private RoleUserParaVO paravo = null;

	private DefaultListModel listmodel = null;

	private HashMap mapList = new HashMap();// key:roleuservo.getOrgUnitType()+roleuservo.getPk();value:OrgnizeUnit

	private String prefixKey = null;// mapList 的key的前缀。

	public AppointUserPanel() {
		super();
		initialize();
	}

	public AppointUserPanel(RoleUserParaVO paravo) {
		super();
		this.paravo = paravo;
		initialize();
	}

	/**
	 * @param prefixKey 需要重新自己的mapKey
	 */
	public AppointUserPanel(RoleUserParaVO paravo, String prefixKey) {
		super();
		this.paravo = paravo;
		this.prefixKey = prefixKey;
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnRight()) {
			onRightClick();
		} else if (e.getSource() == getBtnLeft()) {
			onLeftClick();
		}
	}

	/**
	 * 把多个用户加到List中
	 * @param localvos
	 */
	protected void addBatchUserToList(OrganizeUnit[] localvos) {
		if (localvos != null) {
			for (int i = 0; i < localvos.length; i++) {
				addUserToList(localvos[i]);
			}
		}
	}

	protected void addNodeDataToList(DefaultMutableTreeNode node) {
		if (!(node.getUserObject() instanceof OrganizeUnit))
			return;
		OrganizeUnit roleuservo = (OrganizeUnit) node.getUserObject();
		if (roleuservo.getOrgUnitType() == OrganizeUnitTypes.Role_INT) {//如果是角色
			// 更加该树类型,是否保存为用户
			if (paravo.getTreeType() == RoleUserParaVO.ALL_USER) {
				OrganizeUnit[] localvos = null;
				// 懒加载
//				if (roleuservo.isQueryed())
//					localvos = roleuservo.getSubUnits();
//				else
//					localvos = RoleUserBizDelegator.getInstance().getLocalUserVOByRolePK(roleuservo.getPk(),
//							paravo.getCorppk());
				roleuservo.setQueryed(true);
				roleuservo.setSubUnits(localvos);
				addBatchUserToList(localvos);
			} else {
				addUserToList(roleuservo);
			}

			// 展开树角色节点
			//if (getParaVo().getTreeType() == RoleUserParaVO.ALL_USER)
			//	getRoleuserScrollpane().expandRoleGroup(node, true);

		} else if (roleuservo.getOrgUnitType() == OrganizeUnitTypes.Group_INT) {//如果是在线用户组,如果时间可以,两者合并之
			OrganizeUnit[] localvos = null;
			// 懒加载
//			if (roleuservo.isQueryed())
//				localvos = roleuservo.getSubUnits();
//			else
//				localvos = RoleUserBizDelegator.getInstance().getOnlineUser(paravo.getCorppk());
			roleuservo.setQueryed(true);
			roleuservo.setSubUnits(localvos);
			addBatchUserToList(localvos);
			//展开树角色节点
//			getRoleuserScrollpane().expandRoleGroup(node, true);
		} else {//最后情况为用户
			addUserToList(roleuservo);
		}

	}

	/**
	 * 把一个角色或者用户加到List中
	 */
	protected void addUserToList(OrganizeUnit roleuservo) {
		if (roleuservo != null && !mapList.containsKey(composeListMapKey(roleuservo))) {
			putMapKey(mapList, roleuservo);
			if (roleuservo.getOrgUnitType() == OrganizeUnitTypes.Operator_INT)
				getListModel().addElement(roleuservo);
			else
				getListModel().insertElementAt(roleuservo, getLastIndexOfRole());
			getUserRoleList().setSelectedIndex(getListModel().getSize() - 1);
		}
	}

	/**
	 * 构造Mapkey.<Strong>这里默认是用OrganizeUnit的类型和Pk.如果具体情况可以override it.</Strong>
	 */
	public String composeListMapKey(Object obj) {
		if (obj instanceof OrganizeUnit) {
			OrganizeUnit orgUnit = (OrganizeUnit) obj;
			return getPrefixKey() + orgUnit.getOrgUnitType() + orgUnit.getPk();
		}
		return null;
	}

	private UIButton getBtnLeft() {
		if (leftBt == null) {
			leftBt = new UIButton();
			leftBt.setText("< ");
		}
		return leftBt;
	}

	private UIButton getBtnRight() {
		if (rightBt == null) {
			rightBt = new UIButton();
			rightBt.setText(" >");
		}
		return rightBt;
	}

	// 得到右边List的为角色的最后一个索引
	private int getLastIndexOfRole() {
		int roleIndex = 0;
		for (int i = 0; i < getListModel().getSize(); i++) {
			if (((OrganizeUnit) getListModel().get(i)).getOrgUnitType() == OrganizeUnitTypes.Operator_INT) {
				roleIndex = i;
				break;
			}
		}
		return roleIndex;
	}

	// 得到其缓存map，方便外部其操作
	public HashMap getListMap() {
		return mapList;
	}

	public DefaultListModel getListModel() {
		if (listmodel == null) {
			listmodel = new DefaultListModel();
			listmodel.removeAllElements();
			loadDataToList(listmodel, getParaVo().getSelectedUservos());
		}
		return listmodel;
	}
	

	protected UIScrollPane getUserScrollpane() {
		if (userTreePanel == null) {
			userTreePanel = new UIScrollPane();
			userTreePanel.setViewportView(getUserTree());
			add(userTreePanel);
			userTreePanel.setPreferredSize(new Dimension(280, 350));
		}
		return userTreePanel;
	}

	protected UIScrollPane getListScrollPane() {
		if (userScroll == null) {
			userScroll = new UIScrollPane();
			userScroll.setViewportView(getUserRoleList());
			userScroll.setPreferredSize(new Dimension(240, 350));
		}
		return userScroll;
	}

	protected UIPanel getMovePnl() {
		if (movePane == null) {
			movePane = new UIPanel();
			VerticalFlowLayout vFLayout = new VerticalFlowLayout();
			vFLayout.setVgap(25);
			vFLayout.setAlignment(VerticalFlowLayout.MIDDLE);
			vFLayout.setHorizontalFill(false);
			movePane.setLayout(vFLayout);
			movePane.add(getBtnRight());
			movePane.add(getBtnLeft());
		}
		return movePane;
	}

	public RoleUserParaVO getParaVo() {
		return paravo;
	}

	public String getPrefixKey() {
		return prefixKey;
	}

	/**
	 * 得到最终的List的结果
	 */
	public OrganizeUnit[] getResultVOs() {
		OrganizeUnit[] localuservos = new OrganizeUnit[getListModel().getSize()];
		getListModel().copyInto(localuservos);
		return localuservos;
	}

	public LazyUserOrgTree getUserTree() {
		if (userTree == null){
			userTree = new LazyUserOrgTree(null, this);
			userTree.constructTreeModel();
		}		
		return userTree;
	}

	public DefaultTreeModel getTreeModel() {
		return (DefaultTreeModel) getUserTree().getModel();
	}

	public UIList getUserRoleList() {
		if (listroleuser == null) {
			listroleuser = new UIList();
			//listroleuser.setCellRenderer(new RoleUserListRender());
			listroleuser.setModel(getListModel());
		}
		return listroleuser;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {

		layoutPanel();
		initListener();
	}

	protected void initListener() {
		getBtnRight().addActionListener(this);
		getBtnLeft().addActionListener(this);

	}

	/**
	 * 初始化布局。如果使用者觉得布局不爽，自己override it.
	 */
	public void layoutPanel() {
		Box box = Box.createHorizontalBox();
		box.add(getUserScrollpane());
		Box.createHorizontalStrut(5);
		box.add(getMovePnl());
		Box.createHorizontalStrut(5);
		box.add(getListScrollPane());
		Box.createHorizontalStrut(5);
		this.add(box);
	}

	public void loadDataToList(DefaultListModel listmodel, Object[] uservos) {
		setPrefixKey(prefixKey);
		if (uservos != null) {
			for (int i = 0; i < uservos.length; i++) {
				OrganizeUnit orgunit = (OrganizeUnit) uservos[i];
				listmodel.addElement(orgunit);
				mapList.put(composeListMapKey(orgunit), orgunit);
			}
		}

	}
 //priavte-->protected 抄送界面方便重载
	protected void onLeftClick() {
		// 只是把ListModel中数据remove
		int[] selindexs = getUserRoleList().getSelectedIndices();
		int maxIndex = 0;
		for (int i = selindexs.length - 1; i >= 0; i--) {
			OrganizeUnit orgUnit = (OrganizeUnit) getListModel().getElementAt(selindexs[i]);
			getListModel().removeElementAt(selindexs[i]);
			mapList.remove(composeListMapKey(orgUnit));
			maxIndex = Math.max(selindexs[i], maxIndex);
		}

		maxIndex = maxIndex - (selindexs.length - 1);
		if (maxIndex < getListModel().getSize() - 1) {
			getUserRoleList().setSelectedIndex(maxIndex);
		} else
			getUserRoleList().setSelectedIndex(getListModel().getSize() - 1);

	}
//priavte-->protected 抄送界面方便重载
	private void onRightClick() {
		TreePath[] paths = getUserTree().getSelectionPaths();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				addNodeDataToList(node);
			}
		}
	}

	public void putMapKey(HashMap map, OrganizeUnit localvo) {
		if (localvo != null) {
			if (!map.containsKey(composeListMapKey(localvo))) {
				map.put(composeListMapKey(localvo), localvo);
			}
		}
	}

	public void putMapKeys(OrganizeUnit[] localvos) {
		if (localvos != null) {
			for (int i = 0; i < localvos.length; i++) {
				getListMap().put(composeListMapKey(localvos[i]), localvos[i]);
			}
		}
	}
	
	/**根据key删除mapList中的一个键值对，为了在CpySendUerTree2List中使用。
	 * @param key hashMap的key 
	 * @return void 
	 * */
	protected void removeMapKey(String key){
		getListMap().remove(key);
	}

	/** 重新设置选择了的List的值 */
	public void resetListModel(OrganizeUnit[] organizeUnits, String prefixKey) {
		setPrefixKey(prefixKey);
		getListModel().removeAllElements();
		mapList.clear();//hzg++.2006-8-18
		loadDataToList(getListModel(), organizeUnits);
	}

	/**
	 * 重新设置树
	 */
	public void resetTreeModel(RoleUserParaVO para) {
		this.setParaVo(para);
		//getUserScrollpane().setParavo(para);
		//getUserTree().refreshTree();
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getBtnLeft().setEnabled(enabled);
		getBtnRight().setEnabled(enabled);
	}

	public void setParaVo(RoleUserParaVO paravo) {
		this.paravo = paravo;
	}

	public void setPrefixKey(String prefixKey) {
		this.prefixKey = prefixKey;
	}
	
	

}
