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
 * ��Ϣ���� ������ѡ��Ի���
 *
 * @author leijun 2003-10-21
 * @modifier leijun 2004-10-20 �޸�Ϊ��ʾ���� ���û�������
 * @modifier leijun 2006-5-15 NC50�����飬���ӽ�ɫ
 * @modifier leijun 2006-7-11 ����֧�ָ�λ��FIXME:�ɿ��Ƕ�̬��֯
 * @modifier leijun 2007-4-24 ���Ӷ�ϵͳ�������Զ�������ߵ�֧��
 * @modifier zhouzhenga 2010-6 ����˲��ճ�ҵ��Ԫ������û��ͽ�ɫ
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

	// ������Դ��
	private UITree _treeSource = null;

	// private TreeModel _tmJobs = null; //Դ������ģ�� - ��λ

	private TreeModel _tmUsers = null; // Դ������ģ�� - ��Ա

	private TreeModel _tmRoles = null; // Դ������ģ�� - ��ɫ

	private TreeModel _tmViariable = null; // Դ������ģ�� - ϵͳ����

	private TreeModel _tmCustom = null; // Դ������ģ�� - �Զ���

	// ������Ŀ���б�
	private UIList _listDest = null;

	// ����ѡ�������VO����
	private LinkedList _linkedList = null;

	private String _strReceivers = "";

	// ��Ϣ���ͣ���Ϊ���ʼ�������ʵʱ��Ϣ���������š�����
	private int _iMSG_TYPE = 0;

	private Object[] _receiverTypes = null;

	private ReceiverVO[] receivers = null;

	private ArrayList<String> receiverPks = null;

	/**
	 * @param parent
	 * @param msgType
	 *            ��Ϣ���ͣ�0-�ʼ���1-������Ϣ��2-����
	 */
	public ReceiverRefDlg(java.awt.Container parent, int msgType) {
		super(parent);
		initialize();
		_iMSG_TYPE = msgType; // ��Ϣ����
	}

	/**
	 * ReceiverRefDlg ������ע�⡣
	 *
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public ReceiverRefDlg(java.awt.Container parent, Object[] receiveTypes,
			String title) {
		super(parent, title);
		// ��֧�ֵĽ���������
		_receiverTypes = receiveTypes;
		initialize();
	}

	/**
	 * ȡ����ť��Ӧ�¼�
	 */
	public void BtnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * ȷ����ť��Ӧ�¼� ע�������������л�ȡ���н����ߣ�������һ���ַ����û���ʾ
	 */
	public void BtnOK_ActionPerformed(ActionEvent actionEvent) {
		receivers = (ReceiverVO[]) getLinkedList().toArray(new ReceiverVO[] {});
		_strReceivers = ReceiverUtils.analyseStrRevsFromXML(receivers);
		receiverPks = ReceiverUtils.analyseStrRevPKsFromXML(receivers);
		closeOK();
	}

	/**
	 * ����һ�������߰�ť��Ӧ�¼� WARN::ֻ�� ��һ���ַ�����ӵ�Ŀ���б��У�ʵ�ʶ��󱣴浽_linkedList��
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
			// ˵����ǰ�༭�����ʼ�������
			recVO = new MailReceiverVO();
		} else if (_iMSG_TYPE == 1) {
			// ˵����ǰ�༭����ʵʱ��Ϣ������
			recVO = new MsgReceiverVO();
		} else if (_iMSG_TYPE == 2) {
			// ˵����ǰ�༭���Ƕ��Ž�����
			recVO = new MobileMsgReceiverVO();
		}

		if (obj instanceof OrganizeUnit) {
			// ˵��ѡ����ǡ���Ա���顱��
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
			// ѡ�����ϵͳ����
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
			// ˵����Ŀ��������в����ڣ�����Ӳ�ѡ�и���
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
	 * ���б���ɾ��һ�������� ע������Ҫ��Ŀ���б���ɾ������Ҫ�Ӷ���������ɾ��
	 */
	public void BtnDelOne_ActionPerformed(ActionEvent actionEvent) {
		int iSelected = getListDest().getSelectedIndex();
		if (iSelected == -1) {
			return;
		}
		// ��������ɾ������
		getLinkedList().remove(iSelected);
		// ���б���ɾ��������棩
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
					"pfworkflow", "UPPpfworkflow-000176")/* @res "����һ��������" */);

		}
		return ivjBtnAddOne;
	}

	private UIButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			ivjBtnCancel = new UIButton();
			ivjBtnCancel.setName("BtnCancel");
			ivjBtnCancel.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "ȡ��" */);

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
					"pfworkflow", "UPPpfworkflow-000177")/* @res "ɾ��һ��������" */);

		}
		return ivjBtnDelOne;
	}

	private UIButton getBtnOK() {
		if (ivjBtnOK == null) {
			ivjBtnOK = new UIButton();
			ivjBtnOK.setName("BtnOK");
			ivjBtnOK.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnOK.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);

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
					"UPPpfworkflow-000178")/* @res "����ѡ��" */);
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
	 * ��ʼ���ࡣ
	 */
	private void initialize() {
		setName("ReceiverRefDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(445, 298);
		if (StringUtil.isEmptyWithTrim(getTitle()))
			setTitle(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000179")/* @res "����������" */);
		setContentPane(getUIDialogContentPane());
		initConnections();

	}

	/**
	 * ��á������ߡ�Դ��
	 */
	public UITree getTreeSource() {
		if (_treeSource == null) {
			_treeSource = new UITree();
			_treeSource.setName("TrSource");
			_treeSource.setBounds(0, 0, 78, 72);
			// ����������
			_treeSource.putClientProperty("JTree.lineStyle", "Angled");
			// �������ڵ㵥ѡ
			_treeSource.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			// _treeSource.setModel(getTreeModelJobs())
			_treeSource.setCellRenderer(new CorpRoleUserTreeCellRender());
			// _treeSource.setShowsRootHandles(true);
			_treeSource.setToolTipText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0040")/*@res "˫�����Ż�˾�ڵ��Ի�ȡ�û����ɫ"*/);
		}
		return this._treeSource;
	}

	/**
	 * ��á������ߡ�Ŀ���б�
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
	// "pfworkflow", "UPPpfworkflow-000180")/*@res "���и�λ"*/);
	// _tmJobs = new OmJobDBVoFcTreeModel(root);
	// }
	// return this._tmJobs;
	// }

	public TreeModel getTreeModelUsers() {
		if (_tmUsers == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
					.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000181")/* @res "���в���Ա" */);
			_tmUsers = new DefaultTreeModel(root);

			// ���ص�ǰ�����Լ���������˾���ڵ�
			queryUser(root);
		}
		return this._tmUsers;
	}

	public TreeModel getTreeModelRoles() {
		if (_tmRoles == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
					.getInstance().getStrByID("101203", "UPP101203-000057")/*
																			 * @res
																			 * "���н�ɫ"
																			 */);
			_tmRoles = new DefaultTreeModel(root);

			// ���ص�ǰ�����Լ���������˾���ڵ�
			queryRoles(root);
		}
		return this._tmRoles;
	}

	/**
	 *
	 * @param root
	 */
	private void queryUser(DefaultMutableTreeNode root) {
		// ��ǰ����
		// root.add(new DefaultMutableTreeNode(new
		// OrganizeUnit(WorkbenchEnvironment.getInstance()
		// .getGroupVO())));

		// ��ѯ��ǰ���������й�˾

		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator
				.getInstance().lookup(IUserManageQuery.class);
		try {
			UserVO[] users = null;
			// 1.��ѯ��ǰ�����µ��û�
			users = iCorpService.queryAllUsersByOrg(PfUtilUITools
					.getLoginGroup());
			for (int i = 0; i < (users == null ? 0 : users.length); i++) {
				root.add(new DefaultMutableTreeNode(new OrganizeUnit(users[i])));
			}
			// 2.�õ���ǰ��¼���ŵ�����ҵ��Ԫ
			Vector<OrgVO> allOrgsofThisGroup = getOrgsOfThisGroup();
			// 3.���β�ѯ����ҵ��Ԫ�µ��û�
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

	// ��ѯ��ǰ�����µ�����ҵ��Ԫ
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
		// ��ǰ����
		RoleVO[] rolevos = null;
		RoleGroupVO[] rolegroupvos = null;
		try {
			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator
					.getInstance().lookup(IRoleManageQuery.class.getName());
			// 1..��ѯ��ǰ�����µĽ�ɫ
			rolevos = roleBS.queryRoleByOrg(PfUtilUITools.getLoginGroup(),
					Integer.valueOf(IRoleConst.BUSINESS_TYPE));
			for (int i = 0; i < (rolevos == null ? 0 : rolevos.length); i++) {
				root.add(new DefaultMutableTreeNode(
						new OrganizeUnit(rolevos[i])));
			}
			// 2.�õ���ǰ�����µ�����ҵ��Ԫ
			Vector<OrgVO> allOrgsofThisGroup = getOrgsOfThisGroup();
			// 3.���β�ѯ����ҵ��Ԫ�µĽ�ɫ
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
						// ��õ����˾�������û� - �ӳټ���
						fetchUsersOfCorp(clickNode, pkcorpOrGroup);
					else if (selectedReceivertype == MsgReceiverTypes.ROLES)
						// ��õ����˾�����н�ɫ - �ӳټ���
						fetchRolesOfCorp(clickNode, pkcorpOrGroup);

					// ��ʾ�Ѿ���ѯ����
					orgUnit.setQueryed(true);
					// ��ʾ��·��
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
	 * ���ĳ��˾�����н�ɫ������������ù�˾�Ľ�ɫ
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
			// XXX:�����н�ɫ�Ĺ�˾���Զ���ֵΪ��ǰѡ�еĹ�˾PK
			roles[i].setPk_org(corpPK);
			DefaultMutableTreeNode userLeaf = new DefaultMutableTreeNode(
					new OrganizeUnit(roles[i]));
			userLeaf.setAllowsChildren(false);
			clickNode.add(userLeaf);
		}
	}

	/**
	 * ���ĳ��˾�������û������ɵ�¼ĳ��˾�������û������� <li>����˾�������û� <li>�ϼ���˾�û�ί���˷��������˾���ϼ���ɫ <li>
	 * ����˾��ɫ���θ�������˾������������˾�û�ί���˸����ν�ɫ
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
	 * ��ʼ��Ŀ��������б������
	 */
	public void initReceivers(ReceiverVO[] revs) {
		if (revs == null || revs.length == 0)
			return;

		// yanke1+ �����list����������
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
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0041")/*@res "�Զ��������"*/);
			_tmCustom = new DefaultTreeModel(root);

			// NOTE::���ݻ����ȡ��ǰ�༭�����̵ĵ�������
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
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0042")/*@res "����ϵͳ����"*/);
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