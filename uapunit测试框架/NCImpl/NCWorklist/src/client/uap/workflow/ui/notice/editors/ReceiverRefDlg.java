package uap.workflow.ui.notice.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.org.ICorpQryService;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.component.CorpRoleUserTreeCellRender;

//
import nc.ui.wfengine.designer.DesignerMapCache;
//

import uap.ui.participant.designer.LazyRoleOrgTree;
import uap.ui.participant.designer.LazyUserOrgTree;
import uap.ui.participant.designer.OrganizationTree;
import uap.workflow.app.notice.ReceiverVO;
import uap.workflow.app.vo.IApproveflowConst;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.CorpVO;
import nc.vo.org.GroupVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;

import uap.workflow.pub.app.mail.vo.MailReceiverVO;
import uap.workflow.pub.app.message.vo.MsgReceiverTypes;
import uap.workflow.pub.app.message.vo.MsgReceiverVO;
import uap.workflow.pub.app.mobile.vo.MobileMsgReceiverVO;
import uap.workflow.pub.app.notice.IPfMsgCustomReceiver;
import uap.workflow.pub.app.notice.PfSysVariable;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.ui.util.PfUIDataCache;
import uap.workflow.ui.util.PfUtilUITools;

import nc.vo.sm.UserVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.rbac.constant.IRoleConst;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;

/**
 * 消息配置 接收者选择对话框
 *
 * @author leijun 2003-10-21
 * @modifier leijun 2004-10-20 修改为显示所有 的用户和组了
 * @modifier leijun 2006-5-15 NC50废弃组，增加角色
 * @modifier leijun 2006-7-11 不再支持岗位，FIXME:可考虑动态组织
 * @modifier leijun 2007-4-24 增加对系统变量和自定义接收者的支持
 * @modifier zhouzhenga 2010-6 监控人参照出业务单元定义的用户和角色
 */
public class ReceiverRefDlg extends UIDialog implements ActionListener,
		ItemListener {
	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBtnCancel = null;

	private UIButton ivjBtnOK = null;

	private UIPanel ivjPnlBtn = null;

	private UIPanel ivjPnlCenter = null;

	private UIPanel ivjPnlRecvType = null;

	private UIComboBox comboReceiverTypes = null;

	private UILabel ivjLabType = null;

	private UIPanel ivjPnlOrient = null;

	private UIScrollPane ivjSclPnlDest = null;

	private UIScrollPane ivjSclPnlSource = null;

	private UIButton ivjBtnAddOne = null;

	private UIButton ivjBtnDelOne = null;

	// 接收者源树
	private UITree _treeSource = null;

	// private TreeModel _tmJobs = null; //源树的树模型 - 岗位

	private TreeModel _tmUsers = null; // 源树的树模型 - 人员

	private TreeModel _tmRoles = null; // 源树的树模型 - 角色

	private TreeModel _tmViariable = null; // 源树的树模型 - 系统变量

	private TreeModel _tmCustom = null; // 源树的树模型 - 自定义

	// 接收者目标列表
	private UIList _listDest = null;

	// 所有选择接收者VO链表
	private LinkedList _linkedList = null;

	private String _strReceivers = "";

	// 消息类型，分为“邮件”、“实时消息”、“短信”三种
	private int _iMSG_TYPE = 0;

	private Object[] _receiverTypes = null;

	private ReceiverVO[] receivers = null;

	private ArrayList<String> receiverPks = null;

	/**
	 * @param parent
	 * @param msgType
	 *            消息类型，0-邮件；1-待办消息；2-短信
	 */
	public ReceiverRefDlg(java.awt.Container parent, int msgType) {
		super(parent);
		initialize();
		_iMSG_TYPE = msgType; // 消息类型
	}

	/**
	 * ReceiverRefDlg 构造子注解。
	 *
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public ReceiverRefDlg(java.awt.Container parent, Object[] receiveTypes,
			String title) {
		super(parent, title);
		// 可支持的接收者类型
		_receiverTypes = receiveTypes;
		initialize();
	}

	/**
	 * 取消按钮响应事件
	 */
	public void BtnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * 确定按钮响应事件 注：仅仅从链表中获取所有接收者，并构造一个字符串用户显示
	 */
	public void BtnOK_ActionPerformed(ActionEvent actionEvent) {
		receivers = (ReceiverVO[]) getLinkedList().toArray(new ReceiverVO[] {});
		_strReceivers = ReceiverUtils.analyseStrRevsFromXML(receivers);
		receiverPks = ReceiverUtils.analyseStrRevPKsFromXML(receivers);
		closeOK();
	}

	/**
	 * 增加一个接收者按钮响应事件 WARN::只是 把一个字符串添加到目标列表中，实际对象保存到_linkedList中
	 */
	public void BtnAddOne_ActionPerformed(ActionEvent actionEvent) {

		UITree tree = (UITree) getSclPnlSource().getViewport().getView();
		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree
				.getSelectionPath().getLastPathComponent();
		// DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)
		// getTreeSource()
		// .getSelectionPath().getLastPathComponent();
		Object obj = selNode.getUserObject();
		if (selNode.isRoot())
			return;

		ReceiverVO recVO = null;
		if (_iMSG_TYPE == 0) {
			// 说明当前编辑的是邮件接收者
			recVO = new MailReceiverVO();
		} else if (_iMSG_TYPE == 1) {
			// 说明当前编辑的是实时消息接收者
			recVO = new MsgReceiverVO();
		} else if (_iMSG_TYPE == 2) {
			// 说明当前编辑的是短信接收者
			recVO = new MobileMsgReceiverVO();
		}

		if (obj instanceof OrganizeUnit) {
			// 说明选择的是“人员和组”树
			OrganizeUnit orgUnit = (OrganizeUnit) obj;

			if (orgUnit.getOrgUnitType() == OrganizeUnitTypes.Role_INT) {
				recVO.setType(MsgReceiverTypes.REVEIVER_TYPE_ROLE);
				recVO.setCode(orgUnit.getCode());
				recVO.setCorpPK(orgUnit.getPkOrg());
				recVO.setName(orgUnit.getName());
				recVO.setPK(orgUnit.getPk());
			} else if (orgUnit.getOrgUnitType() == OrganizeUnitTypes.Operator_INT) {
				recVO.setType(MsgReceiverTypes.REVEIVER_TYPE_USER);
				recVO.setCode(orgUnit.getCode());
				recVO.setCorpPK(orgUnit.getPkOrg());
				recVO.setName(orgUnit.getName());
				recVO.setPK(orgUnit.getPk());
			} else {
				return;
			}
		} else if (obj instanceof PfSysVariable.VarEntry) {
			// 选择的是系统变量
			PfSysVariable.VarEntry ve = (PfSysVariable.VarEntry) obj;
			recVO.setType(MsgReceiverTypes.REVEIVER_TYPE_SYSTEM);
			recVO.setCode(ve.getCode());
			// recVO.setCorpPK(orgUnit.getPkCorp());
			recVO.setName(ve.getName());
			// recVO.setPK(ve.getValueGetterClz());
		} else if (obj instanceof ReceiverVO) {
			ReceiverVO rv = (ReceiverVO) obj;
			recVO.setType(MsgReceiverTypes.REVEIVER_TYPE_CUSTOM);
			recVO.setCode(rv.getCode());
			recVO.setCorpPK(rv.getCorpPK());
			recVO.setName(rv.getName());
			recVO.setPK(rv.getPK());
		} else {
			return;
		}

		if (getLinkedList().contains(recVO)) {
			return;
		} else {
			// 说明在目标接收者中不存在，则添加并选中该项
			getLinkedList().add(recVO);
			((DefaultListModel) getListDest().getModel()).addElement(recVO);
			getListDest()
					.setSelectedIndex(
							((DefaultListModel) getListDest().getModel())
									.getSize() - 1);
			getListDest().repaint();
		}
	}

	/**
	 * 从列表中删除一个接收者 注：不仅要从目标列表中删除，还要从对象链表中删除
	 */
	public void BtnDelOne_ActionPerformed(ActionEvent actionEvent) {
		int iSelected = getListDest().getSelectedIndex();
		if (iSelected == -1) {
			return;
		}
		// 从链表中删除对象
		getLinkedList().remove(iSelected);
		// 从列表中删除该项（界面）
		((DefaultListModel) getListDest().getModel())
				.removeElementAt(iSelected);
		getListDest().repaint();
	}

	private UIButton getBtnAddOne() {
		if (ivjBtnAddOne == null) {
			ivjBtnAddOne = new UIButton();
			ivjBtnAddOne.setName("BtnAddOne");
			ivjBtnAddOne.setText(">");
			// user code begin {1}
			ivjBtnAddOne.setToolTipText(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000176")/* @res "增加一个接收者" */);

		}
		return ivjBtnAddOne;
	}

	private UIButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			ivjBtnCancel = new UIButton();
			ivjBtnCancel.setName("BtnCancel");
			ivjBtnCancel.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "取消" */);

		}
		return ivjBtnCancel;
	}

	private UIButton getBtnDelOne() {
		if (ivjBtnDelOne == null) {
			ivjBtnDelOne = new UIButton();
			ivjBtnDelOne.setName("BtnDelOne");
			ivjBtnDelOne.setText("<");
			// user code begin {1}
			ivjBtnDelOne.setToolTipText(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000177")/* @res "删除一个接收者" */);

		}
		return ivjBtnDelOne;
	}

	private UIButton getBtnOK() {
		if (ivjBtnOK == null) {
			ivjBtnOK = new UIButton();
			ivjBtnOK.setName("BtnOK");
			ivjBtnOK.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnOK.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "确定" */);

		}
		return ivjBtnOK;
	}

	private UIComboBox getComboReceiverTypes() {
		if (comboReceiverTypes == null) {
			comboReceiverTypes = new UIComboBox();
			comboReceiverTypes.setName("ComboxType");
			comboReceiverTypes.setBounds(94, 8, 123, 22);
			// user code begin {1}
			if (_receiverTypes != null)
				comboReceiverTypes.addItems(_receiverTypes);
			else
				comboReceiverTypes.addItems(MsgReceiverTypes.VALUES);
			comboReceiverTypes.setSelectedIndex(-1);
			comboReceiverTypes.addItemListener(this);
			comboReceiverTypes.setSelectedIndex(0);
		}
		return comboReceiverTypes;
	}

	private UILabel getLabType() {
		if (ivjLabType == null) {
			ivjLabType = new UILabel();
			ivjLabType.setName("LabType");
			ivjLabType.setText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000178")/* @res "类型选择" */);
			ivjLabType.setBounds(31, 8, 52, 22);

		}
		return ivjLabType;
	}

	private UIPanel getPnlBtn() {
		if (ivjPnlBtn == null) {
			ivjPnlBtn = new UIPanel();
			ivjPnlBtn.setName("PnlBtn");
			ivjPnlBtn.setPreferredSize(new java.awt.Dimension(445, 45));
			ivjPnlBtn.setLayout(getPnlBtnUIButtonLayout());
			getPnlBtn().add(getBtnOK(), getBtnOK().getName());
			getPnlBtn().add(getBtnCancel(), getBtnCancel().getName());

		}
		return ivjPnlBtn;
	}

	private UIButtonLayout getPnlBtnUIButtonLayout() {
		UIButtonLayout ivjPnlBtnUIButtonLayout = null;
		ivjPnlBtnUIButtonLayout = new UIButtonLayout();
		ivjPnlBtnUIButtonLayout.setGap(40);

		return ivjPnlBtnUIButtonLayout;
	}

	private UIPanel getPnlCenter() {
		if (ivjPnlCenter == null) {
			ivjPnlCenter = new UIPanel();
			ivjPnlCenter.setName("PnlCenter");
			ivjPnlCenter.setLayout(new java.awt.BorderLayout());
			getPnlCenter().add(getSclPnlSource(), "West");
			getPnlCenter().add(getSclPnlDest(), "East");
			getPnlCenter().add(getPnlOrient(), "Center");

		}
		return ivjPnlCenter;
	}

	private UIPanel getPnlOrient() {
		if (ivjPnlOrient == null) {
			ivjPnlOrient = new UIPanel();
			ivjPnlOrient.setName("PnlOrient");
			ivjPnlOrient.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsBtnAddOne = new java.awt.GridBagConstraints();
			constraintsBtnAddOne.gridx = 1;
			constraintsBtnAddOne.gridy = 1;
			constraintsBtnAddOne.ipadx = 2;
			constraintsBtnAddOne.insets = new java.awt.Insets(49, 25, 24, 27);
			getPnlOrient().add(getBtnAddOne(), constraintsBtnAddOne);

			java.awt.GridBagConstraints constraintsBtnDelOne = new java.awt.GridBagConstraints();
			constraintsBtnDelOne.gridx = 1;
			constraintsBtnDelOne.gridy = 2;
			constraintsBtnDelOne.ipadx = 2;
			constraintsBtnDelOne.insets = new java.awt.Insets(25, 25, 50, 27);
			getPnlOrient().add(getBtnDelOne(), constraintsBtnDelOne);

		}
		return ivjPnlOrient;
	}

	private UIPanel getPnlRecvType() {
		if (ivjPnlRecvType == null) {
			ivjPnlRecvType = new UIPanel();
			ivjPnlRecvType.setName("PnlRecvType");
			ivjPnlRecvType.setPreferredSize(new java.awt.Dimension(0, 40));
			ivjPnlRecvType.setLayout(null);
			getPnlRecvType().add(getLabType(), getLabType().getName());
			getPnlRecvType().add(getComboReceiverTypes(),
					getComboReceiverTypes().getName());

		}
		return ivjPnlRecvType;
	}

	private UIScrollPane getSclPnlDest() {
		if (ivjSclPnlDest == null) {
			ivjSclPnlDest = new UIScrollPane();
			ivjSclPnlDest.setName("SclPnlDest");
			ivjSclPnlDest.setPreferredSize(new java.awt.Dimension(180, 3));
			// user code begin {1}
			ivjSclPnlDest.setViewportView(getListDest());

		}
		return ivjSclPnlDest;
	}

	private UIScrollPane getSclPnlSource() {
		if (ivjSclPnlSource == null) {
			ivjSclPnlSource = new UIScrollPane();
			ivjSclPnlSource.setName("SclPnlSource");
			ivjSclPnlSource.setPreferredSize(new java.awt.Dimension(180, 3));
			// user code begin {1}
			ivjSclPnlSource.setViewportView(getTreeSource());
		}
		return ivjSclPnlSource;
	}

	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
			getUIDialogContentPane().add(getPnlRecvType(), "North");
			getUIDialogContentPane().add(getPnlCenter(), "Center");
			getUIDialogContentPane().add(getPnlBtn(), "South");

		}
		return ivjUIDialogContentPane;
	}

	private void initConnections() {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnAddOne().addActionListener(this);
		getBtnDelOne().addActionListener(this);
		getTreeSource().addMouseListener(new MyMouseAdaptor());
	}

	/**
	 * 初始化类。
	 */
	private void initialize() {
		setName("ReceiverRefDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(445, 298);
		if (StringUtil.isEmptyWithTrim(getTitle()))
			setTitle(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000179")/* @res "接收者配置" */);
		setContentPane(getUIDialogContentPane());
		initConnections();

	}

	/**
	 * 获得“接收者”源树
	 */
	public UITree getTreeSource() {
		if (_treeSource == null) {
			_treeSource = new UITree();
			_treeSource.setName("TrSource");
			_treeSource.setBounds(0, 0, 78, 72);
			// 设置树线型
			_treeSource.putClientProperty("JTree.lineStyle", "Angled");
			// 限制树节点单选
			_treeSource.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			// _treeSource.setModel(getTreeModelJobs())
			_treeSource.setCellRenderer(new CorpRoleUserTreeCellRender());
			// _treeSource.setShowsRootHandles(true);
			_treeSource.setToolTipText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0040")/*@res "双击集团或公司节点以获取用户或角色"*/);
		}
		return this._treeSource;
	}

	/**
	 * 获得“接收者”目标列表
	 */
	public UIList getListDest() {
		if (_listDest == null) {
			_listDest = new UIList();
			_listDest.setName("listDest");
			_listDest.setBounds(0, 0, 78, 72);
			DefaultListModel model = new DefaultListModel();
			_listDest.setModel(model);
			_listDest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			_listDest.setCellRenderer(new UserRoleListRender());
		}
		return this._listDest;
	}

	// public TreeModel getTreeModelJobs() {
	// if (_tmJobs == null) {
	// DefaultMutableTreeNode root = new
	// DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
	// "pfworkflow", "UPPpfworkflow-000180")/*@res "所有岗位"*/);
	// _tmJobs = new OmJobDBVoFcTreeModel(root);
	// }
	// return this._tmJobs;
	// }

	public TreeModel getTreeModelUsers() {
		if (_tmUsers == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
					.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000181")/* @res "所有操作员" */);
			_tmUsers = new DefaultTreeModel(root);

			// 加载当前集团以及其所属公司树节点
			queryUser(root);
		}
		return this._tmUsers;
	}

	public TreeModel getTreeModelRoles() {
		if (_tmRoles == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
					.getInstance().getStrByID("101203", "UPP101203-000057")/*
																			 * @res
																			 * "所有角色"
																			 */);
			_tmRoles = new DefaultTreeModel(root);

			// 加载当前集团以及其所属公司树节点
			queryRoles(root);
		}
		return this._tmRoles;
	}

	/**
	 *
	 * @param root
	 */
	private void queryUser(DefaultMutableTreeNode root) {
		// 当前集团
		// root.add(new DefaultMutableTreeNode(new
		// OrganizeUnit(WorkbenchEnvironment.getInstance()
		// .getGroupVO())));

		// 查询当前集团下所有公司

		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator
				.getInstance().lookup(IUserManageQuery.class);
		try {
			UserVO[] users = null;
			// 1.查询当前集团下的用户
			users = iCorpService.queryAllUsersByOrg(PfUtilUITools
					.getLoginGroup());
			for (int i = 0; i < (users == null ? 0 : users.length); i++) {
				root.add(new DefaultMutableTreeNode(new OrganizeUnit(users[i])));
			}
			// 2.得到当前登录集团的所有业务单元
			Vector<OrgVO> allOrgsofThisGroup = getOrgsOfThisGroup();
			// 3.依次查询各个业务单元下的用户
			if (allOrgsofThisGroup == null || allOrgsofThisGroup.size() == 0)
				return;
			for (OrgVO orgvo : allOrgsofThisGroup) {
				users = iCorpService.queryAllUsersByOrg(orgvo.getPk_org());
				for (int i = 0; i < (users == null ? 0 : users.length); i++) {
					root.add(new DefaultMutableTreeNode(new OrganizeUnit(
							users[i])));
				}
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	// 查询当前集团下的所有业务单元
	public Vector<OrgVO> getOrgsOfThisGroup() {
		Vector<OrgVO> m_vecOrgsOfThisGroup = new Vector<OrgVO>();
		try {
			IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator
					.getInstance().lookup(IOrgUnitQryService.class.getName());
			OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID(
					WorkbenchEnvironment.getInstance().getGroupVO()
							.getPk_group(), true, true);
			for (int i = 0; i < (aryCorps == null ? 0 : aryCorps.length); i++) {
				m_vecOrgsOfThisGroup.addElement(aryCorps[i]);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return m_vecOrgsOfThisGroup;
	}

	private void queryRoles(DefaultMutableTreeNode root) {
		// 当前集团
		RoleVO[] rolevos = null;
		RoleGroupVO[] rolegroupvos = null;
		try {
			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator
					.getInstance().lookup(IRoleManageQuery.class.getName());
			// 1..查询当前集团下的角色
			rolevos = roleBS.queryRoleByOrg(PfUtilUITools.getLoginGroup(),
					Integer.valueOf(IRoleConst.BUSINESS_TYPE));
			for (int i = 0; i < (rolevos == null ? 0 : rolevos.length); i++) {
				root.add(new DefaultMutableTreeNode(
						new OrganizeUnit(rolevos[i])));
			}
			// 2.得到当前集团下的所有业务单元
			Vector<OrgVO> allOrgsofThisGroup = getOrgsOfThisGroup();
			// 3.依次查询各个业务单元下的角色
			for (OrgVO orgvo : allOrgsofThisGroup) {
				rolevos = roleBS.queryRoleByOrg(orgvo.getPk_org(), Integer.valueOf(IRoleConst.BUSINESS_TYPE));
				for (int i = 0; i < (rolevos == null ? 0 : rolevos.length); i++) {
					root.add(new DefaultMutableTreeNode(new OrganizeUnit(
							rolevos[i])));
				}
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	class MyMouseAdaptor extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2
					&& getTreeSource().getSelectionPath() != null // add by hxr
					&& getTreeSource().getSelectionPath().getPathCount() == 2) {
				DefaultMutableTreeNode clickNode = (DefaultMutableTreeNode) getTreeSource()
						.getSelectionPath().getLastPathComponent();
				Object obj = clickNode.getUserObject();

				if (obj instanceof OrganizeUnit) {
					OrganizeUnit orgUnit = (OrganizeUnit) obj;
					if (orgUnit.isQueryed())
						return;
					if (orgUnit.getOrgUnitType() != OrganizeUnitTypes.Corp_INT
							&& orgUnit.getOrgUnitType() != OrganizeUnitTypes.Group_INT)
						return;

					String pkcorpOrGroup = orgUnit.getPkOrg();
					Object selectedReceivertype = getComboReceiverTypes()
							.getSelectedItem();
					if (selectedReceivertype == MsgReceiverTypes.USERS)
						// 获得点击公司下所有用户 - 延迟加载
						fetchUsersOfCorp(clickNode, pkcorpOrGroup);
					else if (selectedReceivertype == MsgReceiverTypes.ROLES)
						// 获得点击公司下所有角色 - 延迟加载
						fetchRolesOfCorp(clickNode, pkcorpOrGroup);

					// 表示已经查询过了
					orgUnit.setQueryed(true);
					// 显示树路径
					TreePath currpath = getTreeSource().getSelectionPath();
					getTreeSource().expandPath(currpath);
					getTreeSource().makeVisible(currpath);
					getTreeSource().updateUI();
				} else {
					return;
				}
			}
		}
	}

	/**
	 * 获得某公司的所有角色，包括分配给该公司的角色
	 *
	 * @param clickNode
	 * @param corpPK
	 */
	private void fetchRolesOfCorp(DefaultMutableTreeNode clickNode,
			String corpPK) {
		RoleVO[] roles = null;
		try {
			roles = NCLocator.getInstance().lookup(IRoleManageQuery.class)
					.queryRoleByOrg(corpPK, IRoleConst.BUSINESS_TYPE);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		for (int i = 0; i < (roles == null ? 0 : roles.length); i++) {
			// XXX:将所有角色的公司属性都赋值为当前选中的公司PK
			roles[i].setPk_org(corpPK);
			DefaultMutableTreeNode userLeaf = new DefaultMutableTreeNode(
					new OrganizeUnit(roles[i]));
			userLeaf.setAllowsChildren(false);
			clickNode.add(userLeaf);
		}
	}

	/**
	 * 获得某公司的所有用户，即可登录某公司的所有用户，包括 <li>本公司创建的用户 <li>上级公司用户委派了分配给本公司的上级角色 <li>
	 * 本公司角色信任给其他公司，并且其他公司用户委派了该信任角色
	 *
	 * @param clickNode
	 * @param pkcorpOrGroup
	 */
	private void fetchUsersOfCorp(DefaultMutableTreeNode clickNode,
			String pkcorpOrGroup) {
		IUserManageQuery userBS = (IUserManageQuery) NCLocator.getInstance()
				.lookup(IUserManageQuery.class.getName());
		UserVO[] uservos = null;
		try {
			uservos = userBS.queryAllUsersByOrg(pkcorpOrGroup);
			VOUtil.ascSort(uservos, new String[] { "usercode" });
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		for (int i = 0; i < (uservos == null ? 0 : uservos.length); i++) {
			DefaultMutableTreeNode userLeaf = new DefaultMutableTreeNode(
					new OrganizeUnit(uservos[i]));
			userLeaf.setAllowsChildren(false);
			clickNode.add(userLeaf);
		}
	}

	public LinkedList getLinkedList() {
		if (_linkedList == null) {
			_linkedList = new LinkedList();
		}
		return this._linkedList;
	}

	public String getReceiverSTRs() {
		return this._strReceivers;
	}

	public ArrayList<String> getReceiverPks() {
		return this.receiverPks;
	}

	/**
	 * 初始化目标接收者列表和链表
	 */
	public void initReceivers(ReceiverVO[] revs) {
		if (revs == null || revs.length == 0)
			return;

		// yanke1+ 先清除list中已有数据
		getLinkedList().clear();
		((DefaultListModel)getListDest().getModel()).removeAllElements();

		for (int i = 0; i < revs.length; i++) {
			getLinkedList().add(revs[i]);
			((DefaultListModel) getListDest().getModel()).addElement(revs[i]);
		}
		getListDest().setSelectedIndex(
				((DefaultListModel) getListDest().getModel()).getSize() - 1);
	}

	public ReceiverVO[] getReceivers() {
		return (ReceiverVO[]) getLinkedList().toArray(new ReceiverVO[0]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			BtnOK_ActionPerformed(e);
		}
		if (e.getSource() == this.getBtnCancel()) {
			BtnCancel_ActionPerformed(e);
		}
		if (e.getSource() == this.getBtnAddOne()) {
			BtnAddOne_ActionPerformed(e);
		}
		if (e.getSource() == this.getBtnDelOne()) {
			BtnDelOne_ActionPerformed(e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		Object selObject = e.getItem();
		// if (STR_TYPES[0].equals(selObject)) {
		// getTreeSource().setModel(getTreeModelJobs());
		// } else
		if (selObject == MsgReceiverTypes.USERS) {
			getSclPnlSource().setViewportView(getLazyUserOrgTree());
		} else if (selObject == MsgReceiverTypes.ROLES) {
			getSclPnlSource().setViewportView(getLazyRoleOrgTree());
		} else if (selObject == MsgReceiverTypes.SYSTEM) {
			getSclPnlSource().setViewportView(getContextTree());
		} else if (selObject == MsgReceiverTypes.CUSTOM) {
			getSclPnlSource().setViewportView(getCustomTree());
		}
		getSclPnlSource().repaint();

	}

	private LazyUserOrgTree lazyUserTree = null;

	private OrganizationTree getLazyUserOrgTree() {
		if (lazyUserTree == null) {
			lazyUserTree = new LazyUserOrgTree(null, null);
			lazyUserTree.constructTreeModel();
		}

		return lazyUserTree;
	}

	private LazyRoleOrgTree lazyRoleTree = null;

	private OrganizationTree getLazyRoleOrgTree() {
		if (lazyRoleTree == null) {
			lazyRoleTree = new LazyRoleOrgTree(null, null);
			lazyRoleTree.constructTreeModel();
		}

		return lazyRoleTree;
	}

	private UITree getCustomTree() {
		getTreeSource().setModel(getTreeModelCustom());
		return getTreeSource();
	}

	private UITree getContextTree() {
		getTreeSource().setModel(getTreeModelContext());
		return getTreeSource();
	}

	private TreeModel getTreeModelCustom() {
		if (_tmCustom == null) {
			// FIXME::i18n
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0041")/*@res "自定义接收者"*/);
			_tmCustom = new DefaultTreeModel(root);

			// NOTE::根据缓存获取当前编辑的流程的单据类型
			String currBillType = (String) DesignerMapCache.Instance().get(
					IApproveflowConst.CURRENT_BILLTYPE_PK);
			ArrayList alBilltype2VO = PfUIDataCache.getBillType2Info(
					PfUtilBaseTools.getRealBilltype(currBillType),
					ExtendedClassEnum.MSGCONFIG_RECEIVER.getIntValue());
			if (alBilltype2VO.size() == 0)
				return _tmCustom;

			Billtype2VO bt2VO = (Billtype2VO) alBilltype2VO.get(0);
			String checkClsName = bt2VO.getClassname();
			if (!StringUtil.isEmptyWithTrim(checkClsName)) {
				try {
					Object objImpl = Class.forName(checkClsName).newInstance();
					if (objImpl instanceof IPfMsgCustomReceiver) {
						ReceiverVO[] revVOs = ((IPfMsgCustomReceiver) objImpl)
								.createReceivers();
						for (int i = 0; i < (revVOs == null ? 0 : revVOs.length); i++) {
							DefaultMutableTreeNode customNode = new DefaultMutableTreeNode(
									revVOs[i]);
							root.add(customNode);
						}
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return this._tmCustom;
	}

	private TreeModel getTreeModelContext() {
		if (_tmViariable == null) {
			// FIXME::i18n
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0042")/*@res "所有系统变量"*/);
			_tmViariable = new DefaultTreeModel(root);
			Collection coVars = PfSysVariable.instance().getAllVariables()
					.values();
			for (Iterator iter = coVars.iterator(); iter.hasNext();) {
				PfSysVariable.VarEntry var = (PfSysVariable.VarEntry) iter
						.next();
				DefaultMutableTreeNode billmakerNode = new DefaultMutableTreeNode(
						var);
				root.add(billmakerNode);
			}
		}
		return this._tmViariable;
	}
}