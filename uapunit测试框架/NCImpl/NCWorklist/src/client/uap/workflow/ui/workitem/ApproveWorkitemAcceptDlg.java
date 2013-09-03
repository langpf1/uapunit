package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.AbstractFunclet;
import nc.funcnode.ui.IFuncletWindow;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.message.Attachment;
import nc.message.FileAttachment;
import nc.message.vo.AttachmentSetting;
import nc.message.vo.AttachmentVO;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.checknote.PfChecknoteEnum;
import nc.ui.pub.beans.HyperlinkLabelEvent;
import nc.ui.pub.beans.HyperlinkLabelListener;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.pub.workflowqry.WorkflowManageUtil;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.io.FileFilterFactoryAdapter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.term.ApproveTermConfig;
import nc.vo.pf.term.IApproveTerm;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.pf.RoleUserParaVO;
import nc.vo.wfengine.core.util.CoreUtilities;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.admin.WorkflowManageContext;
import uap.workflow.engine.context.AddSignUserInfoCtx;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.vo.WorkflownoteVO;

/**
 * �������Ĺ������Ի���
 * 
 * @author leijun 2009-9
 * @since 6.0
 * @Modifier zhouzhenga 2010-4 ������̵ĸ��ٺͳ���
 * @Modifier zhouzhenga 2011-1 ����֧����Ӹ���
 * @Modifier zhouzhenga 2011-1 ֧��Ԥ�õ�����
 * @Modifier wcj, yanke1 2011-7 ���١����͹��ܵ�ʵ��
 * @modifier yanke1 2011-9-8 ����changlx�������󣬵�����Ϣ���Ĵ򿪵��ݲ�����ʱ�������������Զ��رյ��ݽ���
 * @modifier zhangrui 2012-3-13 ������UEҪ��Դ˽����ع�
 */
public class ApproveWorkitemAcceptDlg extends UIDialog implements ActionListener {

	private static final long serialVersionUID = -9104313190799304533L;
	
	public static int STATUS_APPROVE = 0;
	public static int STATUS_REJECT = 1;
	public static int STATUS_TRANSFER = 2;
	public static int STATUS_ADDASSIGN = 3;

	private WorkflownoteVO worknoteVO = null;
	private boolean isInWorkflow = false;
	private String hintMessage;
	
	UITextArea txtApproveNote = null;
	UIScrollPane paneTxtApprove = null;
	TextListPane paneDispatchPerson = null;
	UILabel lblDipatchTo = null;
	UIPanel pnlDispatch = null;
	UIPanel pnlApproveCenter = null;
	UIPanel pnlApprove = null;

	ApproveFlowRejectPanel rejectPanel;
	UITextArea txtRejectNote = null;
	UIScrollPane txtRejectPane = null;
	UISplitPane splitReject = null;
	UIPanel pnlRejectDownHalf = null;
	UIPanel pnlReject = null;

	UITextArea txtTransferNote = null;
	UIScrollPane txtTransferPane = null;
	UILabel lblTransfer = null;
	TextListPane paneTransferPerson = null;
	UIPanel pnlTransferTop = null;
	UIPanel pnlTransfer = null;
//	BranchPane branch = null;	

	UITextArea txtAddAssignNote = null;
	UIScrollPane txtAddAssignPane = null;
	UILabel lblAddAssign = null;
	TextListPane paneAddAssignPerson = null;
	UIPanel pnlAddAssignTop = null;
	UIPanel pnlAddAssign = null;

	UICheckBox chkTrack = null;
	UICheckBox chkSubmitToRejectTache = null;
	ImageIcon iconCopySend = null;
	UILabel lblCopySend = null;
	ImageIcon iconAttach = null;
	UILabel lblAddAttach = null;
	UIPanel pnlInnerOperations = null;
	
	BlockListPanel pnlAttachs = null;
	BlockListPanel pnlCopySenders = null;
	
	UIPanel pnlInnerBottom = null;

	// �����������Ƿ�رյ��ݽ���
	UICheckBox chkAutoClose = null;
	UIPanel pnlBottomUp = null;
	
	UILabel lblViewFlow = null;
	
	UIButton btnApprovePass = null;
	UIButton btnApproveNoPass = null;
	UIButton btnReject = null;
	UIButton btnTransfer = null;
	
	UIButton btnBeforeAddAssign = null;
	UIButton btnAfterAddAssign = null;
	
	UIButton btnCancel = null;
	
	UIPanel pnlButton = null;
	UIPanel pnlBottom = null;
	UITabbedPane tabPane = null;
	
	Map<Integer, UIButton[]> statusButtonMap = new HashMap<Integer, UIButton[]>();
	Map<Integer, UIPanel> statusPanelMap = new HashMap<Integer, UIPanel>();
	List<UIButton> allButtonList = new ArrayList<UIButton>();
	List<Attachment> attchlist = new ArrayList<Attachment>();

	// yanke1+ 2011-9-8
	/** ���ݽڵ��funcletWindow */
	private IFuncletWindow parentFuncletWindow = null;
	private boolean isOpenedInDialog = false;
	private boolean isAllreadyTracked = false;
	
	/** ��ǩ�Ի��� */
	private AddAssignDialog addAssignDialog = null;
	JRadioButton serial=null;
	JRadioButton coexist=null;

	/* ���ɵ� �û�ѡ��panel */
	private AppointUserPanel transferPanel;
	private TransferUserDialog transferDialog;

	/* ���͵��û�ѡ��panel */
	private CpySendUserTree2List copySendPanel;
	private CopySendDialog copySendDialog;
	
	//ָ�ɵ����
	private ApproveFlowDisPatchPanel dispatchPanel;
	private ApproveFlowDispatchDialog dispatchDialog;
	
	private JFileChooser fileChooser = null;

	private UIRefPane refRemark = null;
	
	private String passCheckNote = null;
	private String nopassCheckNote = null;
	private String rejectCheckNote = null;
	private String allCheckNote = null;
	
	private static boolean DEFAULT_CLOSE_AFTER_APPROVE = false;
	
	/**
	 * ����
	 * 
	 * @param parent
	 * @param noteVO
	 *            ����������VO
	 * @param isInWorkflow
	 *            �Ƿ�Ϊ�����������������̵Ĺ�����
	 */
	public ApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO,
			boolean isInWorkflow) {
		super(parent);
		// yanke1+ 2011-9-8
		// �ҵ����ݽڵ��IFuncletWindow
		// �жϴ򿪷�ʽ�Ƿ�Ϊ�Ի���
		if (parent instanceof AbstractFunclet) {
			AbstractFunclet func = (AbstractFunclet) parent;

			String funcCode = func.getFuncCode();

			IFuncletWindow[] windows = WorkbenchEnvironment.getInstance()
					.getAllOpenedFuncletWindow();

			for (IFuncletWindow window : windows) {
				String windowCode = window.getFuncRegisterVO().getFuncode();
				if (windowCode.equals(funcCode)) {
					parentFuncletWindow = window;
					isOpenedInDialog = (parentFuncletWindow instanceof JDialog);
					break;
				}
			}
		}

		this.worknoteVO = noteVO;
		this.isInWorkflow = isInWorkflow;
		// ����һ���߳�,��ʼ�������
		CoreUtilities.dummyThread4Performance();
		// ��ʼ������
		initialize();
		
	}
	
	protected boolean isStatusShowOnTab(int status) {
		return statusPanelMap.containsKey(status);
	}
	
	protected int getTabIndexByStatus(int status) {
		UIPanel panel = statusPanelMap.get(status);
		return tabPane.indexOfComponent(panel);
	}
	
	private void addPanelToTabPane(int status, String title, Component panel) {
		tabPane.add(title,  panel);
	}

	/**
	 * ��ʼ������ؼ�
	 */
	protected void initialize() {
		setResizable(true);
		setName("PfWorkFlowCheck");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ApproveLangUtil.getApproveDealStatus());
		
		tabPane = new UITabbedPane();
		
		addPanelToTabPane(STATUS_APPROVE, ApproveLangUtil.getApprove(), getPanelApprove()); 
		addPanelToTabPane(STATUS_REJECT, ApproveLangUtil.getReject(), getPanelReject());
		// �ж��Ƿ�ɸ���
//		if (canTransfer()) {
			//addPanelToTabPane(STATUS_TRANSFER, ApproveLangUtil.getTransfer(), getPanelPatch());
		  addPanelToTabPane(STATUS_TRANSFER, ApproveLangUtil.getTransfer(), getPanelTransfer());
			
//		}
		// �ж��Ƿ�ɼ�ǩ
	//	if (canAddApprover()) {
			addPanelToTabPane(STATUS_APPROVE, ApproveLangUtil.getAddAssign(), getPanelAddAssign());
	//	}
		
		Container container = this.getContentPane();
		Container panelContent = createPanelContentWithMargin(container, new BorderLayout(), new Insets(0, 14, 0, 14));		
		panelContent.add(tabPane, BorderLayout.CENTER);		
		panelContent.add(getPanelBottomUp(), BorderLayout.SOUTH); 
		panelContent.add(getPanelBottom(), BorderLayout.SOUTH);

		//��ʼ��״̬
		statusButtonMap.put(STATUS_APPROVE, new UIButton[] { getBtnApprovePass(), getBtnApproveNoPass(), getBtnCancel() });
		statusButtonMap.put(STATUS_REJECT, new UIButton[] { getBtnReject(), getBtnCancel() });
		statusButtonMap.put(STATUS_TRANSFER, new UIButton[] { getBtnTransfer(), getBtnCancel() });
		statusButtonMap.put(STATUS_ADDASSIGN, new UIButton[] { getBtnBeforeAddAssign(), getBtnAfterAddAssign(),getBtnCancel() });
		//statusButtonMap.put(STATUS_ADDASSIGN, new UIButton[] { getBtnAfterAddAssign(), getBtnCancel() });
		
		// Ĭ��������ҳǩ
		setStatus(STATUS_APPROVE);
		
		attachEventListener();

		List<AttachmentVO> attSetting = worknoteVO.getAttachmentSetting();
		if (attSetting != null) {
			AttachmentSetting attachSetting = new AttachmentSetting(attSetting);
			Attachment[] attchments = attachSetting.getAttachments();
			for (int count = 0; count < attchments.length; count++) {
				attchlist.add(attchments[count]);
			}
		}
	}
	
	public WorkflownoteVO getWorkFlow() {
		return worknoteVO;
	}
	
	private JPanel createPanelContentWithMargin(Container container, LayoutManager panelLayout, Insets insets) {
//		JPanel panelContent = new JPanel(panelLayout);
//		panelContent.setBorder(null);
//		if(container instanceof JPanel) {
//			((JPanel)container).setBorder(null);
//		}
//		container.setLayout(new GridBagLayout());
//		container.add(panelContent,
//				new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
//		return panelContent;
		JPanel panel = (JPanel)container;
		panel.setLayout(panelLayout);
		panel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
		return panel;
	}

	private boolean canTransfer() {
		Object value = this.worknoteVO.getRelaProperties().get(
				XPDLNames.CAN_TRANSFER);
		if (value != null && "true".equalsIgnoreCase(value.toString())) {
			if (this.worknoteVO.actiontype
					.equalsIgnoreCase(WorkflownoteVO.WORKITEM_TYPE_APPROVE
							+ WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
				return false;
			else
				return true;
		} else
			return false;
	}
	
	private boolean canAddApprover() {
		Object value = this.worknoteVO.getRelaProperties().get(
				XPDLNames.CAN_ADDAPPROVER);
		if (value != null && "true".equalsIgnoreCase(value.toString())) {
			if (this.worknoteVO.actiontype
					.equalsIgnoreCase(WorkflownoteVO.WORKITEM_TYPE_APPROVE
							+ WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
				return false;
			else
				return true;
		} else
			return false;
	}
	
	protected void attachEventListener() {
		tabPane.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				UITabbedPane tabPane = (UITabbedPane) e.getSource();
				if(tabPane.getSelectedComponent() == pnlApprove) {
					setStatus(STATUS_APPROVE);
				} else if(tabPane.getSelectedComponent() == pnlReject) {
					setStatus(STATUS_REJECT);
				} else if(tabPane.getSelectedComponent() == pnlTransfer) {
					setStatus(STATUS_TRANSFER);
				} else if(tabPane.getSelectedComponent() == pnlAddAssign) {
					setStatus(STATUS_ADDASSIGN);
				}
			}
		});
		//����ͨ��
		getBtnApprovePass().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				onApprove(true);
			}
		});
		//������ͨ��
		getBtnApproveNoPass().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				onApprove(false);
			}
		});
		//����
		getBtnReject().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				onReject();
			}
		});
		//ѡ�������
		getPaneTransferPerson().setActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				getTransferDialog().showModal();
			}
		});
		//ѡ����������
		getTransferDialog().addUIDialogListener(new UIDialogListener() {
			@Override
			public void dialogClosed(UIDialogEvent event) {
				onChooseTransferOK();
			}
		});
		//����
		getBtnTransfer().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onTransfer();
			}
		});
		//ѡ���ǩ��
		getPaneAddAssignPerson().setActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				getAddAssignDialog().showModal();
			}
		});
		//ѡ���ǩ���
		getAddAssignDialog().addUIDialogListener(new UIDialogListener() {			
			@Override
			public void dialogClosed(UIDialogEvent event) {
//				if(event.m_Operation == UIDialogEvent.WINDOW_OK) {
					onChooseAddAssignOK();
//				}
			}
		});
		//��ǩ
		getBtnBeforeAddAssign().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				  onBeforeAddAssign();
			}
		});
		
		getBtnAfterAddAssign().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAfterAddAssign();
			}
		});
		
		//����
		getLblCopySend().addHyperlinkLabelListener(new HyperlinkLabelListener() {			
			@Override
			public void hyperlinkClicked(HyperlinkLabelEvent event) {
				onCopySend();
			}
		});
		//ѡ��ָ����
//		getPaneDispatchPerson().setActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				getDispatchDialog().showModal();
//			}
//		});
		//ѡ��ָ�������
//		getDispatchDialog().addUIDialogListener(new UIDialogListener() {			
//			@Override
//			public void dialogClosed(UIDialogEvent event) {
//				if(event.m_Operation == UIDialogEvent.WINDOW_OK) {
//					onChooseDispatchOK();
//				}
//			}
//		});
		//ɾ��������
		getPanelCopySenders().setRemovePaneListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockPaneEvent event = (BlockPaneEvent)e;
				event.getValue();
				int deleteIndex = -1;
				for(int i=0; i < getCpySendPanel().getListModel().getSize(); i++) {
					Object object = getCpySendPanel().getListModel().get(i);
					if(object == event.getValue()) {
						deleteIndex = i;
						break;
					}
				}
				if(deleteIndex >= 0) {
					getCpySendPanel().getUserRoleList().setSelectedIndex(deleteIndex);
					getCpySendPanel().onMsgRemoveBtnClick();
				}
			}
		});
		//����������
		getLinkViewFlow().addHyperlinkLabelListener(new HyperlinkLabelListener() {			
			@Override
			public void hyperlinkClicked(HyperlinkLabelEvent event) {
				showFlowinfo();
			}
		});
		//ȡ���Ի���
		getBtnCancel().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeCancel();
			}
		});
		//�ϴ�����
		getLblAddAttach().addHyperlinkLabelListener(new HyperlinkLabelListener() {			
			@Override
			public void hyperlinkClicked(HyperlinkLabelEvent event) {
				if (getChooser().showOpenDialog(
						ApproveWorkitemAcceptDlg.this) == JFileChooser.APPROVE_OPTION) {
					File[] files = getChooser().getSelectedFiles();
					addAttachPane(files);
				}
			}
		});
	}
	
	private void onChooseTransferOK() {
		getPaneTransferPerson().clearTexts();
		OrganizeUnit[] users = getTransferUserPanel().getResultVOs();
		if(users != null && users.length > 0) {
			for(OrganizeUnit user : users) {
				getPaneTransferPerson().addTextItem(user.getName(), user);
			}
		}
	}

//	private void onChooseDispatchOK() {
//		//��ȡPanelÿ��ҳǩ�����Ӧ������
//		StringBuffer sbDispatchUser = new StringBuffer();
//		UITabbedPane tabPane = getDisPatchPanel().getUITabbedPane();
//		for (int i = 0; i < tabPane.getTabCount(); i++) {
//			//����
//			sbDispatchUser.append(tabPane.getTitleAt(i));
//			sbDispatchUser.append("��");
//			
//			UIListToList ltl = (UIListToList) tabPane.getTabComponentAt(i);
//			Object[] rightData = ltl.getRightData();
//			if(rightData != null) {
//				for(int j=0; j < rightData.length; j++) {
//					OrganizeUnit selectUser = (OrganizeUnit) rightData[j];
//					sbDispatchUser.append(selectUser.getName());
//					if(j < rightData.length - 1) {
//						sbDispatchUser.append(selectUser.getName());
//					}
//				}
//				sbDispatchUser.append("��");
//			}
//		}
//		getPaneDispatchPerson().setText(sbDispatchUser.toString());
//	}

	@Override
	protected void hotKeyPressed(KeyStroke hotKey, KeyEvent e) {
		if (hotKey.getKeyCode() == KeyEvent.VK_F2) {
			getRefRemark().onButtonClicked();
			
			if(!StringUtil.isEmpty(getRefRemark().getRefName())) {
				int pos = getCurrentTextArea().getCaretPosition();
				getCurrentTextArea().insert("\n" + getRefRemark().getRefName(), pos);
				e.consume();
			}
		}
	}
	
	private void beforeButtonOperate() {
		//����
		copySend();
		
		// ��Ӹ���
		List<AttachmentVO> Attachments = updateAttachment2DocServer();
		worknoteVO.setAttachmentSetting(Attachments);
		// XXXX
		Logger.debug("******�ϴ������ɹ�����������Ϊ " + Attachments.size() + "****");
	}
	
	private void onChooseAddAssignOK() {
		List<OrganizeUnit> userList = getAddAssignDialog().getAddAssignPanel().getSelectedOrg();
		getPaneAddAssignPerson().clearTexts();
		if(userList != null) {
			for(int i=0; i < userList.size(); i++) {
				OrganizeUnit user = userList.get(i);
				getPaneAddAssignPerson().addTextItem(user.getName(), user);
			}
		}
	}
private void onBeforeAddAssign() {
		
	/*	serial.isSelected();  //�Ƿ���
		coexist.isSelected();  //�Ƿ���
		addAssignDialog.getOrgUnits();  //��,��֯
		
		this.worknoteVO.getTaskInstanceVO().getPk_task();
		this.worknoteVO.getTaskInstanceVO().getPk_org();*/
		
		  CreateBeforeAddSignCtx beforeAddSignCtx = this.worknoteVO.getBeforeAddSignCtx();
		  beforeAddSignCtx.setTaskPk(this.worknoteVO.getTaskInstanceVO().getPk_task());
		  List <AddSignUserInfoCtx> sddSignUserInfoCtxs = new ArrayList<AddSignUserInfoCtx>() ;
			int j=0;
			for(int i=0;i<addAssignDialog.getOrgUnits().size();i++)
			{
				if(addAssignDialog.getOrgUnits().get(i).getOrgUnitType()==OrganizeUnitTypes.Operator_INT)
				{
					AddSignUserInfoCtx infoCtxs=new AddSignUserInfoCtx();
					infoCtxs.setUserPk(addAssignDialog.getOrgUnits().get(i).getPk());
					sddSignUserInfoCtxs.add(infoCtxs);
					infoCtxs.setOrder(j);
					j++;
				}
			}
				
			 beforeAddSignCtx.setAddSignUsers(sddSignUserInfoCtxs.toArray(new AddSignUserInfoCtx[j]));
			 beforeAddSignCtx.setUserPk(InvocationInfoProxy.getInstance().getUserId());
			 beforeAddSignCtx.setComment(getTxtAddAssignNote().getText());
			 if(serial.isSelected())
			     beforeAddSignCtx.setLogic(uap.workflow.engine.context.Logic.Sequence);
			 if(coexist.isSelected())
				 beforeAddSignCtx.setLogic(uap.workflow.engine.context.Logic.Parallel);
		  	 
			 IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator.getInstance().lookup(IWorkflowMachine.class.getName());
			 try {
				bsWorkflow.beforeAddSign(this.worknoteVO);
			} catch (BusinessException e) {
				//e.printStackTrace();
			}
		  
		this.closeOK();
	}
	
	private void onAfterAddAssign() {

		CreateAfterAddSignCtx afterAddSignCtx = this.worknoteVO.getAfterAddSignCtx();
		afterAddSignCtx.setTaskPk(this.worknoteVO.getTaskInstanceVO().getPk_task());
		List<AddSignUserInfoCtx> sddSignUserInfoCtxs = new ArrayList<AddSignUserInfoCtx>();
		int j = 0;
		for (int i = 0; i < addAssignDialog.getOrgUnits().size(); i++) {
			if (addAssignDialog.getOrgUnits().get(i).getOrgUnitType() == OrganizeUnitTypes.Operator_INT) {
				AddSignUserInfoCtx infoCtxs = new AddSignUserInfoCtx();
				infoCtxs.setUserPk(addAssignDialog.getOrgUnits().get(i).getPk());
				infoCtxs.setOrder(j);
				sddSignUserInfoCtxs.add(infoCtxs);
				j++;
			}
		}

		afterAddSignCtx.setAddSignUsers(sddSignUserInfoCtxs.toArray(new AddSignUserInfoCtx[j]));
		afterAddSignCtx.setUserPk(InvocationInfoProxy.getInstance().getUserId());
		afterAddSignCtx.setComment(getTxtAddAssignNote().getText());
		if (serial.isSelected())
			afterAddSignCtx.setLogic(uap.workflow.engine.context.Logic.Sequence);
		if (coexist.isSelected())
			afterAddSignCtx.setLogic(uap.workflow.engine.context.Logic.Parallel);

		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator.getInstance().lookup(IWorkflowMachine.class.getName());
		try {
			bsWorkflow.afterAddSign(this.worknoteVO);
		} catch (BusinessException e) {
			//e.printStackTrace();
		}
		this.closeOK();
	}
	private void onTransfer() {
		beforeButtonOperate();
		worknoteVO.setChecknote(getTxtTransferNote().getText());
		// ����
		boolean doneSuccessed = true;
		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator.getInstance().lookup(IWorkflowMachine.class.getName());
		ArrayList<String> turnPks=new ArrayList<String>();
		String turnPk=null;
		for(int i=0;i<paneTransferPerson.getItemList().size();i++)
		{
			turnPk=((OrganizeUnit)paneTransferPerson.getItemList().get(i).getValue()).getPk();
			turnPks.add(turnPk);
		}
		//String turnPk=((OrganizeUnit)paneTransferPerson.getItemList().get(0).getValue()).getPk();
		try {
			// String s=branch.getDm1().get(0).toString();	
			bsWorkflow.delegateTask(this.worknoteVO, turnPks);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		
		
		if (doneSuccessed) {
			this.closeOK();
		}
	}

	/**
	 * ����
	 */
	private void onReject() {
		beforeButtonOperate();
		
		boolean isSubmitToRejectTache = chkSubmitToRejectTache.isSelected();
		
		String checkNote = getTxtRejectNote().getText();
		if(StringUtil.isEmpty(checkNote)) {
			checkNote = getRejectCheckNote();
		}
		worknoteVO.setChecknote(checkNote);

		String activiId=rejectPanel.getM_elementId(); 
		
		// ����
		//worknoteVO.getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.Reject.getIntValue());
		RejectTaskInsCtx rejectTaskCtx = worknoteVO.getBackwardInfo();
		rejectTaskCtx.setTaskPk(worknoteVO.getTaskInstanceVO().getPk_task());
		rejectTaskCtx.setUserPk(InvocationInfoProxy.getInstance().getUserId());
		//rejectInfo.setUserPks(null);
		rejectTaskCtx.setComment(checkNote);
		UserTaskRunTimeCtx rejectInfo = new UserTaskRunTimeCtx();
		rejectInfo.setActivityId(activiId);
	    rejectTaskCtx.setRejectInfo(rejectInfo);

		//���ص���λ��
		/*//xry TODO:
		worknoteVO.getTaskInstanceVO().getTask().setSubmit2RjectTache(isSubmitToRejectTache);
		IActivity rejectActivity = getPanelRejectFlow().getRejectToActivity();
		if (rejectActivity == null|| (rejectActivity.isStartActivity() && !rejectActivity.isInSubflow()))
			worknoteVO.getTaskInstanceVO().getTask().setBackToFirstActivity(true);
		else
			worknoteVO.getTaskInstanceVO().getTask().setBackToFirstActivity(false);
		worknoteVO.getBackwardInfo().getRejectInfo().setActiviId(rejectActivity == null ? null : rejectActivity.getId());
		*/
		worknoteVO.setApproveresult("R");
		this.closeOK();
	}
	
	private void onCopySend() {
		getCopySendDialog().showModal();
	}
	
	private AddAssignDialog getAddAssignDialog() {
		if (addAssignDialog == null) {
			addAssignDialog = new AddAssignDialog(this, worknoteVO);
			//��ʱ����ȡ����ť
			addAssignDialog.getBtnCancel().setVisible(false);
		}
		return addAssignDialog;
	}
	
	private CopySendDialog getCopySendDialog() {
		if (copySendDialog == null) {
			copySendDialog = new CopySendDialog(this);
		}
		return copySendDialog;
	}
	
	private TransferUserDialog getTransferDialog() {
		if (transferDialog == null) {
			transferDialog = new TransferUserDialog(this);
		}
		return transferDialog;
	}
	
	private ApproveFlowDispatchDialog getDispatchDialog() {
		if (dispatchDialog == null) {
			dispatchDialog = new ApproveFlowDispatchDialog(this);
		}
		return dispatchDialog;
	}

	private ApproveFlowDisPatchPanel getDisPatchPanel() {
		if (dispatchPanel == null) {
			dispatchPanel = new ApproveFlowDisPatchPanel();
		}
		return dispatchPanel;
	}

	private class ApproveFlowDispatchDialog extends UIDialog {
		private static final long serialVersionUID = 3328208132975742401L;
		
		private UIPanel pnlButton = null;
		private UIButton btnOK = null;
		private UIButton btnCancel = null;

		public ApproveFlowDispatchDialog(Container parent) {
			super(parent);
			initialize();
		}
		
		private void initialize() {
			setTitle(ApproveLangUtil.getPleaseDispatchNextUser());
			setSize(550, 400);
			setLayout(new BorderLayout());
			getContentPane().add(getDisPatchPanel(), BorderLayout.CENTER);	
			getContentPane().add(getPnlButton(), BorderLayout.SOUTH);
			
			attachEventListener();
		}

		private void attachEventListener() {
			getBtnOK().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					getDisPatchPanel().runDispatch();					
					closeOK();
				}
			});
			getBtnCancel().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					closeCancel();
				}
			});
		}
		
		private UIPanel getPnlButton() {
			if (pnlButton == null) {
				pnlButton = new UIPanel(new FlowLayout());
				pnlButton.add(getBtnOK(), (Object) FlowLayout.CENTER);
				pnlButton.add(getBtnCancel(), (Object) FlowLayout.CENTER);
			}
			return pnlButton;
		}
		
		private UIButton getBtnOK() {
			if (btnOK == null) {
				btnOK = new UIButton(ApproveLangUtil.getOK());
			}
			return btnOK;
		}

		private UIButton getBtnCancel() {
			if (btnCancel == null) {
				btnCancel = new UIButton(ApproveLangUtil.getCancel());
			}
			return btnCancel;
		}
	}
	
	private class TransferUserDialog extends UIDialog {
		private static final long serialVersionUID = -9217377452652709676L;
		private UIPanel pnlButton = null;
		private UIButton btnOK = null;
		private UIButton btnCancel = null;
		
		public TransferUserDialog(Container parent) {
			super(parent);
			initialize();
		}
		
		private void initialize() {
			setTitle("������Ϣ");
			setLayout(new BorderLayout());
			setSize(800, 600);
			
			UIPanel panelContent = new UIPanel(new BorderLayout());
			panelContent.add(getTransferUserPanel(), BorderLayout.CENTER);
			panelContent.add(getPnlButton(), BorderLayout.SOUTH);
			
			UIScrollPane scrollPane = new UIScrollPane();
			scrollPane.add(panelContent);
			scrollPane.setViewportView(panelContent);
			
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			
			attachEventListener();
		}
		
		private void attachEventListener() {
			getBtnOK().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					closeOK();
				}
			});
			getBtnCancel().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					closeCancel();
				}
			});
		}
		
		private UIPanel getPnlButton() {
			if (pnlButton == null) {
				pnlButton = new UIPanel(new FlowLayout());
				pnlButton.add(getBtnOK(), (Object) FlowLayout.CENTER);
//				pnlButton.add(getBtnCancel(), (Object) FlowLayout.CENTER);
			}
			return pnlButton;
		}
		
		private UIButton getBtnOK() {
			if (btnOK == null) {
				btnOK = new UIButton(ApproveLangUtil.getOK());
			}
			return btnOK;
		}

		private UIButton getBtnCancel() {
			if (btnCancel == null) {
				btnCancel = new UIButton(ApproveLangUtil.getCancel());
			}
			return btnCancel;
		}
	}
	
	private class CopySendDialog extends UIDialog{
		private static final long serialVersionUID = -8590726968790943701L;
		
		private UIPanel pnlButton = null;
		private UIButton btnOK = null;
		private UIButton btnCancel = null;
		
		public CopySendDialog(Container parent) {
			super(parent);
			initialize();
		}
		
		private void initialize() {
			setSize(650, 330);
			setLayout(new BorderLayout());
			getContentPane().add(getCpySendPanel(), BorderLayout.CENTER);
			getContentPane().add(getPnlButton(), BorderLayout.SOUTH);
			
			attachEventListener();
		}
		
		private void attachEventListener() {
			getBtnOK().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					getPanelCopySenders().clearBlocks();
					OrganizeUnit[] users = getCpySendPanel().getResultVOs();
					if(users != null && users.length > 0) {
						for(OrganizeUnit user : users) {
							getPanelCopySenders().addNewBlock(user.getName(), user);
						}
					}
					getPanelCopySenders().updateUI();
					closeOK();
				}
			});
			getBtnCancel().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					closeCancel();
				}
			});
		}
		
		private UIPanel getPnlButton() {
			if (pnlButton == null) {
				pnlButton = new UIPanel(new FlowLayout());
				pnlButton.add(getBtnOK(), (Object) FlowLayout.CENTER);
//				pnlButton.add(getBtnCancel(), (Object) FlowLayout.CENTER);
			}
			return pnlButton;
		}
		
		private UIButton getBtnOK() {
			if (btnOK == null) {
				btnOK = new UIButton(ApproveLangUtil.getOK());
			}
			return btnOK;
		}

		private UIButton getBtnCancel() {
			if (btnCancel == null) {
				btnCancel = new UIButton(ApproveLangUtil.getCancel());
			}
			return btnCancel;
		}
	}
	
	/**
	 * ���͵�panel
	 * */
	private CpySendUserTree2List getCpySendPanel() {
		if (copySendPanel == null) {
			copySendPanel = new CpySendUserTree2List(getRoleUserParaVO());
			copySendPanel.getRoleUserTree().setRootVisible(false);
		}
		return copySendPanel;
	}

	// �ṩ��ǩ�����ɣ���������ʼ���Ĳ���
	private RoleUserParaVO getRoleUserParaVO() {
		RoleUserParaVO paravo = new RoleUserParaVO();
		paravo.setCorppk(PfUtilUITools.getLoginGroup());
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(PfUtilUITools.getLoginGroup());
		ArrayList<UserVO> users = null;
		users = (ArrayList<UserVO>) DBCacheQueryFacade.runQuery(
				"select * from sm_user where pk_group =?", sqlParam,
				new BeanListProcessor(UserVO.class));
		if (users != null && users.size() > 0)
			paravo.setRoleuservos(OrganizeUnit.fromUserVOs(users
					.toArray(new UserVO[0])));
		paravo.setShowCorp(false);
		paravo.setSelectRole(false);
		return paravo;
	}

	/**
	 * ��ʾ������Ϣ <li>����Ϊ������ <li>Ҳ����Ϊ������
	 */
	protected void showFlowinfo() {
		FlowStateDlg dlg = new FlowStateDlg(this,
				worknoteVO.getPk_billtype(), worknoteVO.getBillVersionPK(),
				isInWorkflow ? WorkflowTypeEnum.Workflow.getIntValue()
						: WorkflowTypeEnum.Approveflow.getIntValue());
		dlg.setVisible(true);
	}
	
	private void copySend() {
		// @modifier yanke1 2011-7-15 ���ó�������Ϣ
		// �����Ƿ���׼�����г���
		worknoteVO.setMailExtCpySenders(getCpySendPanel().getMailVOs());
		worknoteVO.setMsgExtCpySenders(getCpySendPanel().getMsgVOs());
	}
	
	/**
	 * ����ͨ����ͨ��
	 */
	protected void onApprove(boolean pass) {

		if (worknoteVO.isAssign()) {
			// ���ָ����Ϣ
			AssignDlg test=  new AssignDlg(worknoteVO);
			test.setBounds(150, 60, 1200, 800); // ���ô��ڴ�С��λ��
			test.setVisible(true);
		/*	getDisPatchPanel().initByWorknoteVO(worknoteVO,
					pass ? AssignableInfo.CRITERION_PASS
							: AssignableInfo.CRITERION_NOPASS);
			int result = getDispatchDialog().showModal();
			if(result == UIDialog.ID_CANCEL) {
				// ���ָ�ɶԻ�������ȡ������ôȡ������������������»ص�������� ��changlx�����壩modified by zhangrui 2012-04-17
				return;
			}*/
		}
//		beforeButtonOperate();
		
		// yanke1+ 2011-7-15 ���õ�ǰ�������Ƿ�����̽��и���
//		boolean isTrack = chkTrack.isSelected();
//		worknoteVO.setTrack(isTrack);
		
		String checkNote = txtApproveNote.getText();
		if(StringUtil.isEmpty(checkNote)) {
			checkNote = pass ? getPassCheckNote() : getNoPassCheckNote();
		}
		worknoteVO.setChecknote(checkNote);
		worknoteVO.setApproveresult(UFBoolean.valueOf(pass).toString());
		this.closeOK();
	}

	public void setShowPass(boolean isShow) {
		//����������ť������
		btnApprovePass.setEnabled(isShow);
		if(isShow) {
			//ѡ������ҳǩ
			tabPane.setSelectedIndex(STATUS_APPROVE);
		}
	}
	
	public void setShowNoPass(boolean isShow) {
		//����������ť������
		btnApproveNoPass.setEnabled(isShow);
		if(isShow) {
			//ѡ������ҳǩ
			tabPane.setSelectedIndex(STATUS_APPROVE);
		}
	}
	
	public void setShowReject(boolean isShow) {
		if(isStatusShowOnTab(STATUS_REJECT)) {
			//���ò���ҳǩ������
			tabPane.setEnabledAt(getTabIndexByStatus(STATUS_REJECT), isShow);
			if(isShow) {
				//ѡ�в���ҳǩ
				tabPane.setSelectedIndex(STATUS_REJECT);
			}
		}
	}

	/**
	 * ��ǰ�Ƿ���ĳ��״̬
	 * @param status
	 * @return
	 */
	public boolean isCurrentInStatus(int status) {
		int tabIndex = getTabIndexByStatus(status);
		return tabPane.getSelectedIndex() == tabIndex;
	}
	
	private UITextArea getCurrentTextArea() {
		if(isCurrentInStatus(STATUS_APPROVE)) {
			return getTxtApproveNote();
		} else if(isCurrentInStatus(STATUS_REJECT)) {
			return getTxtRejectNote();
		} else if(isCurrentInStatus(STATUS_TRANSFER)) {
			return getTxtTransferNote();
		} else if(isCurrentInStatus(STATUS_ADDASSIGN)) {
			return getTxtAddAssignNote();
		}
		return getTxtApproveNote();
	}
	
	private String getCurrentNote() {
		if(isCurrentInStatus(STATUS_APPROVE)) {
			return getApproveNote();
		} else if(isCurrentInStatus(STATUS_REJECT)) {
			return getRejectCheckNote();
		}
		return "";
	}
	
	private String getApproveNote() {
		return "�������������Ĭ��Ϊ��" + getPassCheckNote() + "��" + getNoPassCheckNote();
	}
	
	/**
	 * ���õ�ǰ��Ԥ������ 
	 * @param noteMessage
	 */
	public void setCheckNote(String noteMessage) {
		String note = getCurrentNote();
		
		String checkNote = note;
		if (!StringUtil.isEmptyWithTrim(hintMessage))
			checkNote = note
					+ "\n"
					+ ApproveLangUtil.getHintTo() + " : "
					+ hintMessage;
		getCurrentTextArea().setText(checkNote);
	}
	
	private String getPassCheckNote() {
		if (passCheckNote == null) {
			passCheckNote = getIndividualCheckNote(PfChecknoteEnum.PASS);
			if (passCheckNote == null)
				passCheckNote = "";
		}
		return passCheckNote;
	}

	private String getNoPassCheckNote() {
		if (nopassCheckNote == null) {
			nopassCheckNote = getIndividualCheckNote(PfChecknoteEnum.NOPASS);
			if (nopassCheckNote == null)
				nopassCheckNote = "";
		}
		return nopassCheckNote;
	}

	private String getRejectCheckNote() {
		if (rejectCheckNote == null) {
			rejectCheckNote = getIndividualCheckNote(PfChecknoteEnum.REJECT);
			if (rejectCheckNote == null)
				rejectCheckNote = "";
		}
		return rejectCheckNote;
	}

	public void setHintMessage(String hintMessage) {
		this.hintMessage = hintMessage;
	}

	/**
	 * ���Ҹ��Ի��������õ�������δ���ã������ϵͳԤ�õġ�
	 * 
	 * @param type
	 *            ��������
	 * @return ����
	 * */
	private String getIndividualCheckNote(PfChecknoteEnum type) {
		String sqlCond = "select note from pub_wf_checknote where pk_user in(?,?)";
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(PfUtilUITools.getLoginUser());
		sqlParam.addParam("SYSTEM");
		if (type.toInt() != -1) {
			sqlCond += " and notetype=?";
			sqlParam.addParam(type.toInt());
		}
		sqlCond += " order by pk_user";// system�϶����ں��棿
		ArrayList<String> notes = (ArrayList<String>) DBCacheQueryFacade
				.runQuery(sqlCond, sqlParam, new ColumnListProcessor());
		// if (notes == null || notes.size() == 0){
		// //�ٲ�ϵͳԤ�Ƶ�
		// sqlParam.clearParams();
		// sqlParam.addParam("SYSTEM");
		// sqlParam.addParam(type.toInt());
		// notes = (ArrayList<String>)DBCacheQueryFacade.runQuery(sqlCond,
		// sqlParam, new ColumnListProcessor());
		// }

		if (notes == null || notes.size() == 0)
			return type.toString();
		else
			return notes.get(0);
	}
	
	private void closeParentFuncletWindow() {
		boolean isAutoClose = getChkAutoClose().isSelected();

		if (isOpenedInDialog && isAutoClose) {
			parentFuncletWindow.closeWindow();
		}
	}

	// yanke1+ 2011-9-8
	// ���okʱ������
	// �����жϵ��ݽ����Ƿ�Ϊ�Ի��򣨼��Ƿ�����Ϣ���Ĵ򿪣�
	// ��Ϊ�Ի�����һ���رյ��ݽ���
	@Override
	public void closeOK() {

		closeParentFuncletWindow();
		super.closeOK();
	}

	
	/**
	 * �ԡ�ȡ����ģʽ�رնԻ��� ҵ��ڵ������Ҫ�޸�
	 */
	@Override
	public void closeCancel() {
		setResult(ID_CANCEL);
		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
		return;
	}
	
	protected UIScrollPane createTextAreaScrollPane(UITextArea txtArea) {
		UIScrollPane scrollPane = new UIScrollPane();
		scrollPane.add(txtArea);
		scrollPane.setViewportView(txtArea);
		return scrollPane;
	}
	
	public void setStatus(int status) {
		getChkAutoClose().setVisible(isOpenedInDialog);
		
		if(status == STATUS_APPROVE) {
			setSize(500, 290);
			setPreferredSize(new Dimension(500, 290));
			
			//TODO ���ø��Ի�����
			
		} else if(status == STATUS_REJECT) {
			setSize(850, 650);
			setPreferredSize(new Dimension(850, 650));
		} else if(status == STATUS_TRANSFER) {
			setSize(500, 290);
			setPreferredSize(new Dimension(500, 290));
		} else if(status == STATUS_ADDASSIGN) {
			setSize(540, 290);
			setPreferredSize(new Dimension(540, 290));
		}		
		
		//���õײ��������ؼ�
		for(Component compInBottom : getPanelInnerOperations().getComponents()) {
			getPanelInnerOperations().remove(compInBottom);
		}
		getPanelInnerOperations().add(getChkTrack());
		if(status == STATUS_REJECT) {
			getPanelInnerOperations().add(getChkSubmitToRejectTache());
		}
		getPanelInnerOperations().add(getLblCopySend());
		getPanelInnerOperations().add(getLblAddAttach());
		getPanelInnerOperations().updateUI();
		
	/*	if(((Container)tabPane.getSelectedComponent()) instanceof BranchPane){
			//((Container)tabPane.getSelectedComponent()).add(getPanelInnerBottom());
		}else{*/
			((Container)tabPane.getSelectedComponent()).add(getPanelInnerBottom(), BorderLayout.SOUTH);
	//	}
		
		//���ð�ť
		//�ȶ�remove
		for(UIButton button : allButtonList) {
			pnlButton.remove(button);
		}

		UIButton[] buttons = statusButtonMap.get(status);
		if(buttons == null) {
			return;
		}
		//��add��Ӧ�İ�ť
		for(UIButton button : buttons) {
			if(button == getBtnApproveNoPass()) {
				// �Ƿ����ز���׼��ť
				if (ApproveTermConfig.getInstance().isHidden(IApproveTerm.NO_PASS)) {
					continue;
				}
			}
			if(button == getBtnApproveNoPass()) {
				// �Ƿ����ز���׼��ť
				if (ApproveTermConfig.getInstance().isHidden(IApproveTerm.NO_PASS)) {
					continue;
				}
			}
			if(button == getBtnApproveNoPass()) {
				// �Ƿ����ز���׼��ť
				if (ApproveTermConfig.getInstance().isHidden(IApproveTerm.NO_PASS)) {
					continue;
				}
			}
			
			pnlButton.add(button);
		} 
		pnlButton.updateUI();
	}

	private void setWfInstanceIsTracked(String pk_wf_instance, String supervisor) {
		try {
			isAllreadyTracked = new WorkflowManageUtil().checkIsAlreadyTracked(
					pk_wf_instance, supervisor);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	private UIPanel getPanelDispatch() {
		if (pnlDispatch == null) {
			pnlDispatch = new UIPanel(new FlowLayout());
			pnlDispatch.add(getLblDispatchTo(), FlowLayout.LEFT);
			pnlDispatch.add(getPaneDispatchPerson(), FlowLayout.CENTER);
		}
		return pnlDispatch;
	}
	
	private UILabel getLblDispatchTo() {
		if (lblDipatchTo == null) {
			lblDipatchTo = new UILabel("ָ����");
			lblDipatchTo.setName("lblDipatchTo");
		}
		return lblDipatchTo;
	}
	
	private TextListPane getPaneDispatchPerson() {
		if (paneDispatchPerson == null) {
			paneDispatchPerson = new TextListPane();
			paneDispatchPerson.setName("paneDispatchPerson");
		}
		return paneDispatchPerson;
	}
	
	private UIPanel getPanelBottomUp() {
		if (pnlBottomUp == null) {
			pnlBottomUp = new UIPanel(new FlowLayout());
			pnlBottomUp.setName("pnlBottomUp");
			pnlBottomUp.add(getChkAutoClose(), FlowLayout.LEFT);
		}
		return pnlBottomUp;
	}
	
	/**
	 * �ϴ��������ĵ����������ϴ�ʧ�ܣ���Ӱ����������
	 * */
	private List<AttachmentVO> updateAttachment2DocServer() {
		List<AttachmentVO> vos = new ArrayList<AttachmentVO>();
		try {
			for (Attachment attachment : attchlist) {
				AttachmentVO vo = attachment.uploadToFileServer();
				vo.setFilesize(attachment.getSize());
				vo.setFilename(attachment.getName());
				vos.add(vo);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage() + "�ϴ�����ʧ�ܣ�");
		}
		return vos;
	}

	
	private void addAttachPane(File[] files) {
		if (files != null && files.length > 0) {
			for (File file : files) {
				Attachment attch = new FileAttachment(file);
				addAttachPane(attch);
			}
		}
		getPanelAttach().updateUI();
	}

	private void addAttachPane(Attachment attch) {
		getPanelAttach().addNewBlock(attch.getName(), attch);
		attchlist.add(attch);
	}

	private UIRefPane getRefRemark() {
		if (refRemark == null) {
			refRemark = new UIRefPane();
			refRemark.setName("refRemark");
			refRemark.setVisible(false);
			String remarkRefName = "����ժҪ";
			refRemark.setRefNodeName(remarkRefName);
		}
		return refRemark;
	}

	private UICheckBox getChkAutoClose() {
		if (chkAutoClose == null) {
			chkAutoClose = new UICheckBox();
			chkAutoClose.setSelected(DEFAULT_CLOSE_AFTER_APPROVE);

			chkAutoClose.setText(ApproveLangUtil.getCloseBillAfterApprove());
			int w = chkAutoClose.getFontMetrics(chkAutoClose.getFont())
					.stringWidth(chkAutoClose.getText());

			chkAutoClose.setPreferredSize(new Dimension(w + 32, chkAutoClose
					.getSize().height));
		}
		return chkAutoClose;
	}
	
	private JFileChooser getChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			// �ļ�ѡ��ʽ,ֻ��ѡ���ļ�
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// �Ƿ��ṩ"�����ļ�"����
			fileChooser.setAcceptAllFileFilterUsed(true);
			// �����ļ�������
			fileChooser.setFileFilter(FileFilterFactoryAdapter
					.suffixFileFilter(new String[] { ".doc", ".txt", ".pdf",
							".xls" }));
			fileChooser.setApproveButtonText(ApproveLangUtil.getUploadAttach());
			fileChooser.setMultiSelectionEnabled(true);
		}
		return fileChooser;
	}
	
	private BlockListPanel getPanelCopySenders() {
		if (pnlCopySenders == null) {
			pnlCopySenders = new BlockListPanel(ApproveLangUtil.getCopySend());
			pnlCopySenders.setName("pnlCopySenders");
			pnlCopySenders.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		}
		return pnlCopySenders;
	}
	
	private BlockListPanel getPanelAttach() {
		if(pnlAttachs == null) {
			pnlAttachs = new BlockListPanel(ApproveLangUtil.getAttach());
			pnlAttachs.setName("pnlAttachs");
			pnlAttachs.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
		}
		return pnlAttachs;
	}
	
	/**
	 * ���ɵ� panel
	 */
	private AppointUserPanel getTransferUserPanel() {
		if (transferPanel == null) {
			transferPanel = new AppointUserPanel(getRoleUserParaVO());
			// transferPanel.getRoleUserTree().setRootVisible(false);
		}
		return transferPanel;
	}
	
	private UIPanel getPanelInnerOperations() {
		if(pnlInnerOperations == null) {
			pnlInnerOperations = new UIPanel();
			pnlInnerOperations.setName("pnlInnerOperations");
			
			JPanel panelContent = createPanelContentWithMargin(pnlInnerOperations, new FlowLayout(FlowLayout.LEFT, 20, 0), new Insets(5, 0, 0, 0));
			panelContent.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			panelContent.add(getChkTrack());
			panelContent.add(getChkSubmitToRejectTache());
			panelContent.add(getLblCopySend());
			panelContent.add(getLblAddAttach());
		}
		return pnlInnerOperations;
	}
	
	private ImageIcon getIconCopySend() {
		if (iconCopySend == null) {
			iconCopySend = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/approve/copysend.png");
		}
		return iconCopySend;
	}

	private ImageIcon getIconAttach() {
		if (iconAttach == null) {
			iconAttach = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/approve/close.png");
		}
		return iconAttach;
	}
	/**
	 * ҳǩ�ڵĵײ����
	 * @return
	 */
	private UIPanel getPanelInnerBottom() {
		if(pnlInnerBottom == null) {
			pnlInnerBottom = new UIPanel();
			pnlInnerBottom.setName("pnlInnerBottom");
			pnlInnerBottom.setLayout(new BoxLayout(pnlInnerBottom, BoxLayout.Y_AXIS));
			pnlInnerBottom.add(getPanelInnerOperations());
			pnlInnerBottom.add(getPanelCopySenders());
			pnlInnerBottom.add(getPanelAttach());
		}
		pnlInnerBottom.updateUI();
		return pnlInnerBottom;
	}
	
	private UICheckBox getChkTrack() {
		if(chkTrack == null) {
			chkTrack = new UICheckBox(ApproveLangUtil.getTrack());
			chkTrack.setName("chkTrack");
			setWfInstanceIsTracked(worknoteVO.getTaskInstanceVO().getPk_process_instance(), PfUtilUITools.getLoginUser());
			chkTrack.setSelected(isAllreadyTracked);

			int w = chkTrack.getFontMetrics(chkTrack.getFont())
					.stringWidth(chkTrack.getText());
			chkTrack.setPreferredSize(new Dimension(w + 32, 22));
		}
		return chkTrack;
	}
	
	private UICheckBox getChkSubmitToRejectTache() {
		if(chkSubmitToRejectTache == null) {
			chkSubmitToRejectTache = new UICheckBox("ֱ���ύ���ػ���");
			chkSubmitToRejectTache.setName("chkSubmitToRejectTache");
			int w = chkSubmitToRejectTache.getFontMetrics(chkSubmitToRejectTache.getFont())
					.stringWidth(chkSubmitToRejectTache.getText());
			Dimension size = new Dimension(w + 250, chkSubmitToRejectTache.getHeight());
			chkSubmitToRejectTache.setSize(size);
		}
		return chkSubmitToRejectTache;
	}
	
	private UILabel getLblCopySend() {
		if(lblCopySend == null) {
			lblCopySend = createLinkButton(ApproveLangUtil.getCopySend(), getIconCopySend());
			lblCopySend.setName("copySend");
		}
		return lblCopySend;
	}
	
	private UILabel getLblAddAttach() {
		if(lblAddAttach == null) {
			lblAddAttach = createLinkButton(ApproveLangUtil.getAddAttach(), getIconAttach());
			lblAddAttach.setName("lblAddAttach");
		}
		return lblAddAttach;
	}
	
	/**
	 * �õ�����ҳǩ���
	 * @return
	 */
	private UIPanel getPanelApprove() {
		if(pnlApprove == null) {
			// ����
			pnlApprove = new UIPanel();
			pnlApprove.setName("pnlApprove");
			
			Container panelContent = createPanelContentWithMargin(pnlApprove, new BorderLayout(), new Insets(10,10,10,10));
			panelContent.add(getPanelApproveCenter(), BorderLayout.CENTER);
		}
		return pnlApprove;
	}
	
	private UIPanel getPanelApproveCenter() {
		if (pnlApproveCenter == null) {
			pnlApproveCenter = new UIPanel(new BorderLayout());
			pnlApproveCenter.setName("pnlApproveCenter");
			
			UIPanel panelContent = new UIPanel(new GridBagLayout());
			pnlApproveCenter.add(getPaneTxtApprove(), BorderLayout.CENTER);
//			pnlApproveCenter.add(getPanelDispatch(), BorderLayout.SOUTH);
		}
		return pnlApproveCenter;
	}
	
	private UIScrollPane getPaneTxtApprove() {
		if(paneTxtApprove == null) {
			paneTxtApprove = createTextAreaScrollPane(getTxtApproveNote());
			paneTxtApprove.setName("paneTxtApprove");
		}
		return paneTxtApprove;
	}
	
	private UITextArea getTxtApproveNote() {
		if(txtApproveNote == null) {
			txtApproveNote = new UITextArea();
			txtApproveNote.setName("txtApproveNote");
			txtApproveNote.setLineWrap(true);
			txtApproveNote.setMaxLength(1024);
//			txtApproveNote.setBounds(-1, 23, 377, 157);
			txtApproveNote.setHitStr(getApproveNote());
		}
		return txtApproveNote;
	}
	
	
	/*private BranchPane getPanelPatch() {	
		
		//String taskPk=this.worknoteVO.getTaskInstanceVO().getPk_task();
		//IUserTaskInfo util = NCLocator.getInstance().lookup(IUserTaskInfo.class);
		//List<UserTaskPrepCtx>  ctx = util.retriveNextUserTaskInfo(taskPk, null);
		if(branch == null)
		branch=new BranchPane();
		return branch;
	}*/
	
	
	/**
	 * �õ��������
	 * @return
	 */
	private UIPanel getPanelReject() {	
		if(pnlReject == null) {		
			pnlReject = new UIPanel();
			pnlReject.setName("pnlReject");
			
			Container panelContent = createPanelContentWithMargin(pnlReject, new BorderLayout(), new Insets(0,0,0,0));
			panelContent.add(getSplitReject(), BorderLayout.CENTER);
		}
		return pnlReject;
	}
	
	private UISplitPane getSplitReject() {
		if (splitReject == null) {			
			splitReject = new UISplitPane(JSplitPane.VERTICAL_SPLIT, getPanelRejectFlow(), getPnlRejectDownHalf());
			splitReject.setName("splitReject");
			splitReject.setResizeWeight(0.5);
			splitReject.setSize(100, 10);
			splitReject.setDividerSize(10);
		}
		return splitReject;
	}
	
	private UIPanel getPnlRejectDownHalf() {
		if (pnlRejectDownHalf == null) {
			pnlRejectDownHalf = new UIPanel();
			pnlRejectDownHalf.setName("pnlRejectDownHalf");
			JPanel panelContent = createPanelContentWithMargin(pnlRejectDownHalf, new BorderLayout(), new Insets(10, 10, 5, 10));
			panelContent.add(getTxtRejectPane(), BorderLayout.CENTER);
			panelContent.add(getPanelInnerBottom(), BorderLayout.SOUTH);
		}
		return pnlRejectDownHalf;
	}
	
	
	private UIScrollPane getTxtRejectPane() {
		if (txtRejectPane == null) {
			txtRejectPane = createTextAreaScrollPane(getTxtRejectNote()); 
			txtRejectPane.setName("txtRejectPane");
		}
		return txtRejectPane;
	}
	
	private UITextArea getTxtRejectNote() {
		if (txtRejectNote == null) {
			txtRejectNote = new UITextArea();
			txtRejectNote.setHitStr(getRejectCheckNote());
		}
		return txtRejectNote;
	}

	private ApproveFlowRejectPanel getPanelRejectFlow() {
		if (rejectPanel == null) {
			String processInstancePK = null;
			String processDefPK=null;
			//if (isInWorkflow) {
				processInstancePK = worknoteVO.getTaskInstanceVO().getPk_process_instance();
				processDefPK=worknoteVO.getTaskInstanceVO().getPk_process_def();
			//}
			rejectPanel = new ApproveFlowRejectPanel(worknoteVO.getTaskInstanceVO().getPk_form_ins_version(),
					worknoteVO.getTaskInstanceVO().getPk_bizobject(), processInstancePK,processDefPK,
					WorkflowTypeEnum.Approveflow.getIntValue());
		}
		return rejectPanel;
	}
	/**
	 * �õ��������
	 * @return
	 */
	private UIPanel getPanelTransfer() {
		if(pnlTransfer == null) {
			pnlTransfer = new UIPanel();
			
			Container panelContent = createPanelContentWithMargin(pnlTransfer, new BorderLayout(), new Insets(14, 10, 10, 10));
			panelContent.add(getPnlTransferTop(), BorderLayout.NORTH);
			panelContent.add(getTxtTransferPane(), BorderLayout.CENTER);
		}
		return pnlTransfer;
	}

	
	
	private UIScrollPane getTxtTransferPane() {
		if (txtTransferPane == null) {
			txtTransferPane = createTextAreaScrollPane(getTxtTransferNote()); 
		}
		return txtTransferPane;
	}
	
	private UITextArea getTxtTransferNote() {
		if (txtTransferNote == null) {
			txtTransferNote = new UITextArea();
		}
		return txtTransferNote;
	}
	
	private UIPanel getPnlTransferTop() {
		if (pnlTransferTop == null) {
			pnlTransferTop = new UIPanel(new FlowLayout());
			pnlTransferTop.add(getLblTransfer(), FlowLayout.LEFT);
			pnlTransferTop.add(getPaneTransferPerson(), FlowLayout.CENTER);
		}
		return pnlTransferTop;
	}
	
	private UILabel getLblTransfer() {
		if (lblTransfer == null) {
			lblTransfer = new UILabel("������");
		}
		return lblTransfer;
	}
	
	private TextListPane getPaneTransferPerson() {
		if (paneTransferPerson == null) {
			paneTransferPerson = new TextListPane();
		}
		return paneTransferPerson;
	}
	
	/**
	 * �õ���ǩ���
	 * @return
	 */
	private UIPanel getPanelAddAssign() {
		if(pnlAddAssign == null) {			
			pnlAddAssign = new UIPanel();
			
			Container panelContent = createPanelContentWithMargin(pnlAddAssign, new BorderLayout(), new Insets(14, 10, 10, 10));
			panelContent.add(getPnlAddAssignTop(), BorderLayout.NORTH);
			panelContent.add(getTxtAddAssignPane(), BorderLayout.CENTER);
		}
		return pnlAddAssign;
	}
	
	private UIScrollPane getTxtAddAssignPane() {
		if (txtAddAssignPane == null) {
			txtAddAssignPane = createTextAreaScrollPane(getTxtAddAssignNote()); 
		}
		return txtAddAssignPane;
	}
	
	private UITextArea getTxtAddAssignNote() {
		if (txtAddAssignNote == null) {
			txtAddAssignNote = new UITextArea();
		}
		return txtAddAssignNote;
	}
	
	private UIPanel getPnlAddAssignTop() {
		if (pnlAddAssignTop == null) {
			pnlAddAssignTop = new UIPanel(new FlowLayout());
			pnlAddAssignTop.add(getLblAddAssign(), FlowLayout.LEFT);
			pnlAddAssignTop.add(getPaneAddAssignPerson(), FlowLayout.CENTER);
			serial=new JRadioButton("����",false);
			coexist=new JRadioButton("����",true);
			ButtonGroup SorC=new ButtonGroup();
			SorC.add(serial);
			SorC.add(coexist);
			pnlAddAssignTop.add(serial);
			pnlAddAssignTop.add(coexist);
		}
		return pnlAddAssignTop;
	}
	
	private TextListPane getPaneAddAssignPerson() {
		if (paneAddAssignPerson == null) {
			paneAddAssignPerson = new TextListPane();
		}
		return paneAddAssignPerson;
	}

	private UILabel getLblAddAssign() {
		if (lblAddAssign == null) {
			lblAddAssign = new UILabel("��ǩ��");
		}
		return lblAddAssign;
	}	
	
	/**
	 * �õ����Ӱ�ť��Ŀǰ��Labelʵ��
	 * @param text
	 * @return
	 */
	private UILabel createLinkButton(String text, Icon icon) {
		UILabel label = new UILabel(text, null, JLabel.LEFT);
		if(icon != null) {
			label.setIconTextGap(6);
		}
		label.setHyperlinkLabel(true);
		label.setSize(label.getFont().getSize() * text.length() + 10, label.getFont().getSize());
		return label;
	}
	
	private UILabel getLinkViewFlow() {
		if(lblViewFlow == null) {
			//�鿴������
			lblViewFlow = createLinkButton("�鿴������", null);
		}
		return lblViewFlow; 
	}
	
	private UIPanel getPanelBottom() {
		if(pnlBottom == null) {
			pnlBottom = new UIPanel();
			pnlBottom.setLayout(new BorderLayout());
			pnlBottom.add(getLinkViewFlow(), BorderLayout.WEST);
			
			pnlBottom.add(getButtonPanel(), BorderLayout.EAST);
		}
		return pnlBottom;
	}
	
	private UIButton getBtnApprovePass() {
		if(btnApprovePass == null) {
			btnApprovePass = new UIButton(ApproveLangUtil.getPass());
		}
		return btnApprovePass;
	}
	
	private UIButton getBtnApproveNoPass() {
		if(btnApproveNoPass == null) {
			btnApproveNoPass = new UIButton(ApproveLangUtil.getNoPass());
		}
		return btnApproveNoPass;
	}

	private UIButton getBtnReject() {
		if(btnReject == null) {
			btnReject = new UIButton(ApproveLangUtil.getReject());
		}
		return btnReject;
	}

	private UIButton getBtnTransfer() {
		if(btnTransfer == null) {
			btnTransfer = new UIButton(ApproveLangUtil.getTransfer());
		}
		return btnTransfer;
	}

	private UIButton getBtnBeforeAddAssign() {
		if(btnBeforeAddAssign == null) {
			btnBeforeAddAssign = new UIButton("ǰ��ǩ");
		}
		return btnBeforeAddAssign;
	}
	private UIButton getBtnAfterAddAssign() {
		if(btnAfterAddAssign == null) {
			btnAfterAddAssign = new UIButton("���ǩ");
		}
		return btnAfterAddAssign;
	}
	
	private UIButton getBtnCancel() {
		if(btnCancel == null) {
			btnCancel = new UIButton(ApproveLangUtil.getCancel());
		}
		return btnCancel;
	}
	
	private UIPanel getButtonPanel() {
		if(pnlButton == null) {
			//��ť
			allButtonList.add(getBtnApprovePass());
			allButtonList.add(getBtnApproveNoPass());
			allButtonList.add(getBtnReject());
			allButtonList.add(getBtnTransfer());
			allButtonList.add(getBtnBeforeAddAssign());
			allButtonList.add(getBtnAfterAddAssign());
			allButtonList.add(getBtnCancel());
			pnlButton = new UIPanel();
			pnlButton.setLayout(new FlowLayout());
		}
		return pnlButton;
	}
}

