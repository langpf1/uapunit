package uap.workflow.modeler;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.lock.PKLock;
import nc.funcnode.ui.action.CheckboxAction;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.SeparatorButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.desktop.GroupButtonObject;
import nc.ui.pub.desktop.PFToftPanel;
import nc.ui.pub.hotkey.HotkeyFactory;
import nc.ui.pub.hotkey.IHotkeyTypes;
import nc.vo.pub.hotkey.NCKey;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.itf.IDeployService;
import uap.workflow.engine.itf.IProcessDefGroupQry;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ProcessDefGroupVO;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.modeler.editors.GraphPreviewDlgMaker;

public class WorkflowsManager extends PFToftPanel implements ListSelectionListener {

	private static final long serialVersionUID = 6876521989969103831L;
	

	private GroupMutableTreeNode processTreeRoot;
	
	private ButtonObject m_boAdd = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "addBtnName")/** @ res "增加"*/, NCLangRes
			.getInstance().getStrByID("pfworkflow", "addBtnName")/** @res "增加"*/, 2, "Add"/* 增加 */);

	private ButtonObject m_boEdit = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/** @ res "修改"*/, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/** @res "修改"*/, 2, "Edit"/* 修改 */);

	private ButtonObject m_boDelete = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/** @res "删除"*/, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/** @res "删除"*/, 2, "Delete"/* 删除 */);

	protected ButtonObject m_boRefresh = new ButtonObject(NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/** @res "刷新"*/, NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/** @res* "刷新"*/, 2,
			"Refresh"/* 刷新 */);

	private ButtonObject m_btnFliter = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "BusitypeClientUI2-000000")/* 过滤 */,
			NCLangRes.getInstance().getStrByID("pfworkflow","BusitypeClientUI2-000000")/* 过滤 */, 2, "Fliter"/* 过滤 */);

	private ButtonObject m_btnShowSealedFlow = new ButtonObject(NCLangRes
			.getInstance().getStrByID("pfworkflow","UfWorkflowDesignerUI-000004")/* 显示停用流程 */, NCLangRes
			.getInstance().getStrByID("pfworkflow","UfWorkflowDesignerUI-000004")/* 显示停用流程 */, 2,"ShowSealedFlow"/* 显示停用流程 */);

	private GroupButtonObject m_btnUseOperate = new GroupButtonObject(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* 启用 */, 
					NCLangRes.getInstance().getStrByID("pfworkflow","UfWorkflowDesignerUI-000001")/* 启用 */, 2, "UseOperate"/* 启用 */);

	private ButtonObject m_btnUse = new ButtonObject(NCLangRes.getInstance().getStrByID("pfworkflow", "UfWorkflowDesignerUI-000001")/* 启用 */,
			NCLangRes.getInstance().getStrByID("pfworkflow","UfWorkflowDesignerUI-000001")/* 启用 */, 2, "Use"/* 启用 */);

	private ButtonObject m_btnDisUse = new ButtonObject(NCLangRes.getInstance().getStrByID("pfworkflow", "UfWorkflowDesignerUI-000002")/* 停用 */,
			NCLangRes.getInstance().getStrByID("pfworkflow","UfWorkflowDesignerUI-000002")/* 停用 */, 2, "DisUse"/* 停用 */);

	private ButtonObject m_btnPreview = new ButtonObject(
			NCLangRes.getInstance().getStrByID("pfworkflow","BusitypeClientUI2-000001")/* 预览 */, 
			NCLangRes.getInstance().getStrByID("pfworkflow","BusitypeClientUI2-000001")/* 预览 */, "Preview"/* 预览 */);

	private CheckboxAction m_actShowSealedFlow = new CheckboxAction(
			m_btnShowSealedFlow.getCode(), m_btnShowSealedFlow.getName(),
			m_btnShowSealedFlow.getHint()) {
				private static final long serialVersionUID = -5564488498688677576L;
		@Override
		public void actionPerformed(ActionEvent e) {
			isShowSealFlow = !isShowSealFlow;
			onBtnRefresh();
		}
	};

	private SeparatorButtonObject m_btnSeparator = new SeparatorButtonObject();

	private ButtonObject[] m_MainButtonGroup = { m_boAdd, m_boEdit, m_boDelete,
			m_btnSeparator, m_boRefresh, m_btnFliter, m_btnSeparator,
			m_btnUseOperate, m_btnPreview };

	private JTree processTree=null;
	private ProcessDefinitionVO[] workflowsVOs = null;
	private ProcessDefGroupVO[] processDefGroupVOs=null;
	private DefaultTreeModel treeModel=null;
	private UITablePane tablePane = null;
	private HashMap<String, GroupMutableTreeNode> father;
	private boolean isShowSealFlow = false;

	/** 当前编辑器中被加锁的流程定义PK */
	private ArrayList<String> alLockedProcessPKs = new ArrayList<String>();

	public WorkflowsManager() {
		super();
	}

	@Override
	public void init() {
		initialize();
	}

	/**
	 * 初始化
	 * */
	private void initialize() {
		m_btnFliter.addChildButton(m_btnShowSealedFlow);
		m_btnFliter.setCheckboxGroup(true);
		m_btnUseOperate.setExclusiveMode(true);
		m_btnUseOperate.addChildButton(m_btnUse);
		m_btnUseOperate.addChildButton(m_btnDisUse);
		m_btnShowSealedFlow.setAction(m_actShowSealedFlow);

		setHotkeys();
		setButtons(m_MainButtonGroup);
		
		initTableModel();
		initTableData(false);
		getTable().getSelectionModel().addListSelectionListener(this);
		initTreeData();
        treeModel=new DefaultTreeModel(processTreeRoot);
		processTree=new JTree();
		processTree.setModel(treeModel);
		processTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeValueChanged(evt);
            }
        });
		processTree.addMouseListener(new treeMouseListener());
		JSplitPane all=new JSplitPane();
		all.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		all.setLeftComponent(processTree);
		all.setRightComponent(getUITablePane());
		all.setResizeWeight(1);
		all.setDividerSize(0);
		all.setBorder(null);
		all.setContinuousLayout(true);
		all.setMinimumSize(new Dimension(0, 0));
		all.setDividerLocation(180);	
		add(all);

	}
   //改变流程类别事件响应
	private void jTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {
		GroupMutableTreeNode selectedNode=(GroupMutableTreeNode) processTree.getLastSelectedPathComponent();//返回最后选定的节点
		getTableModel().clearTable();
		
		 ProcessDefinitionVO [] workflowsVOsInGroup=null;
		IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
		if(selectedNode!=null)
		workflowsVOsInGroup = proDefQry.getProcessDefVOByProdefGroup(selectedNode.getId());	
        if(workflowsVOsInGroup!=null&&workflowsVOsInGroup.length>0)
         getTableModel().addVO(workflowsVOsInGroup);
		 getTableModel().refreshTable();
	}
	     
	private void setHotkeys() {
		HotkeyFactory.fillBtnObjWithKotkey(m_boAdd, IHotkeyTypes.NEW);
		HotkeyFactory.fillBtnObjWithKotkey(m_boEdit, IHotkeyTypes.EDIT);
		HotkeyFactory.fillBtnObjWithKotkey(m_boDelete, IHotkeyTypes.DELETE);
		HotkeyFactory.fillBtnObjWithKotkey(m_boRefresh, IHotkeyTypes.REFRESH);
		HotkeyFactory.fillBtnObjWithKotkey(m_btnPreview, IHotkeyTypes.BROWSE);

		HotkeyFactory.fillCustomHotkey(m_btnUse, NCKey.MODIFIERS_CTRL
				+ NCKey.MODIFIERS_ALT, "E", "(Ctrl+Alt+E)");
		HotkeyFactory.fillCustomHotkey(m_btnDisUse, NCKey.MODIFIERS_CTRL
				+ NCKey.MODIFIERS_ALT, "D", "(Ctrl+Alt+D)");
		HotkeyFactory
				.fillActionWithCustomHotkey(m_actShowSealedFlow,
						NCKey.MODIFIERS_CTRL + NCKey.MODIFIERS_ALT, "S",
						"(Ctrl+Alt+S)");
	}

	
	public List<String> getLockedProcessPKs() {
		return this.alLockedProcessPKs;
	}

	public void freeProcessPK(String proc_def_pk) {
		if (proc_def_pk == null)
			return;
		PKLock.getInstance().releaseLock(proc_def_pk,
				PfUtilUITools.getLoginUser(), null);
	}

	public boolean lockProcessPK(String proc_def_pk) {
		if (proc_def_pk == null)
			return false;
		boolean bLockOK = PKLock.getInstance().acquireLock(proc_def_pk,
				PfUtilUITools.getLoginUser(), null);
		if (bLockOK)
			return true;

		return false;
	}

	/**
	 * 判断是否被加锁
	 * 
	 * @return
	 * */
	private boolean checkIsLocked(String pk_busitype) {
		boolean isLockOK = lockProcessPK(pk_busitype);
		if (!isLockOK) {
			MessageDialog
				.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow", "hintDialogTitle")/** @ res "提示"*/, 
				NCLangRes.getInstance().getStrByID("pfworkflow","editCollision")/** @ res "流程定义正被其他人编辑！"*/);

		}
		return isLockOK;
	}

	/**
	 * 初始化数据，查询出当前登陆集团下的所有业务流
	 * 
	 * 直接到数据库中查找  zhai注
	 * */
	private void initTableData(boolean isShowSealFlow) {
		getTableModel().clearTable();
		try {
			IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
			workflowsVOs = proDefQry.getAllProcessDef(InvocationInfoProxy.getInstance().getGroupId());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if (workflowsVOs != null)
			getTableModel().addVO(workflowsVOs);
		getTableModel().refreshTable();
	}

    //初始化流程分类树
	private void initTreeData() {
		try {
			IProcessDefGroupQry proDefGroupQry = NCLocator.getInstance().lookup(IProcessDefGroupQry.class);
			processDefGroupVOs = proDefGroupQry.getAllProcessDefGroup();  //读取分组信息
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if(processDefGroupVOs!=null)
		{
			father=new  HashMap<String, GroupMutableTreeNode>();
			ProcessDefGroupVO temp=null;
			processTreeRoot=new GroupMutableTreeNode("0000                ","");
			father.put("0000                ",processTreeRoot);
			for(int i=0;i<processDefGroupVOs.length;i++)
			{
				temp=processDefGroupVOs[i];
				GroupMutableTreeNode newNode=new GroupMutableTreeNode(temp.getPk_prodefgroup(),temp.getName());
				father.put(temp.getPk_prodefgroup(), newNode);
			}
			for(int i=0;i<processDefGroupVOs.length;i++)
			{
				temp=processDefGroupVOs[i];
				father.get(temp.getPk_parentgroup()).add(father.get(temp.getPk_prodefgroup()));
			}
		}
	}
	
	private UITable getTable() {
		final UITable table = (UITable) getUITablePane().getTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private WorkflowsTableModel getTableModel() {
		return (WorkflowsTableModel) getTable().getModel();
	}

	private void initTableModel() {
		WorkflowsTableModel model = new WorkflowsTableModel(ProcessDefinitionVO.class);
		getTable().setModel(model);
		getTable().setColumnWidth(model.getColumnWidth());
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//getTable().getColumnModel().getColumn(6).setCellRenderer(new ProcessStateRender());
		getTable().addSortListener();
	}

	private Point getDialogLocation(UITable table, int columIndex) {
		int width = 0;
		while (columIndex >= 0) {
			width += table.getColumnModel().getColumn(columIndex).getWidth();
			columIndex--;
		}
		return new Point(width, 30);
	}

	private UITablePane getUITablePane() {
		if (tablePane == null) {
			tablePane = new UITablePane();
			tablePane.setName("UITablePane1");
			tablePane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return tablePane;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return NCLangRes.getInstance().getStrByID("pfworkflow", "appTitle")/* 流程定义 */;
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if (bo == m_boAdd) {
			onBtnAdd();
		} else if (bo == m_boEdit) {
			onBtnEdit();
		} else if (bo == m_boDelete) {
			onBtnDel();
		} else if (bo == m_boRefresh) {
			onBtnRefresh();
		} else if (bo == m_btnUse || bo == m_btnDisUse) {
			onDisuse(bo);
		} else if (bo == m_btnPreview) {
			onBtnPrivew();
		}
	}

	/**
	 * 预览
	 * */
	private void onBtnPrivew() {
		if (!checkLineSelected())
			return;
		ProcessDefinitionVO vo = (ProcessDefinitionVO) getTableModel().getVO(getTable().getSelectedRow());
		GraphPreviewDlgMaker.showGraphPreviewDlg(vo.getPk_prodef(),getDialogLocation(getTable(), 2));
	}

	/**
	 * 启动时会校验业务流
	 * */
	private void onDisuse(ButtonObject bo) {
		if (!checkLineSelected())
			return;
		ProcessDefinitionVO vo = (ProcessDefinitionVO) getTableModel().getVO(
				getTable().getSelectedRow());
//		try {
//
//			if (bo == m_btnUse) {
//				// 启用时候需要判断是否正确
//				boolean isvalidity = NCLocator.getInstance().lookup(
//						IGraphPersist.class).checkGraphValidity(vo.getPk_busitype());
//				if (!isvalidity) {
//					MessageDialog.showErrorDlg(null, null, NCLangRes
//							.getInstance().getStrByID("pfworkflow",
//									"BusitypeClientUI2-000003")/** 业务流定义错误，无法启动！*/);
//					return;
//				}
//				// 判断是否存在相同四要素的业务流
//				String billtypecode = null;
//				String trantypecode = null;
//				BilltypeVO billvo = PfDataCache.getBillType(vo.getPrimarybilltype());
//				if (billvo.getIstransaction() != null && billvo.getIstransaction().booleanValue()) {
//					billtypecode = billvo.getParentbilltype();
//					trantypecode = vo.getPrimarybilltype();
//				} else {
//					billtypecode = vo.getPrimarybilltype();
//				}
//				boolean isDuplicate = NCLocator.getInstance().lookup(
//						IPFConfig.class).existDuplicateBusiFlow(billtypecode,
//						trantypecode, vo.getPk_orgs(), vo.getPrimaryKey())
//						.booleanValue();
//				if (isDuplicate) {
//					MessageDialog.showErrorDlg(null, null, NCLangRes
//							.getInstance().getStrByID("pfworkflow",
//									"BusitypeClientUI2-000004")/*不能存在四要素相同的业务流， 无法启动*/);
//					return;
//				}
//			}
//			NCLocator.getInstance().lookup(IPFConfig.class)
//					.updateProcessValidation(vo.getPk_busitype(),
//							bo == m_btnUse);
//		} catch (BusinessException e) {
//			Logger.error(e.getMessage(), e);
//		}
		vo.setValidity(bo == m_btnUse ? ProcessDefinitionStatusEnum.Valid
				.getIntValue() : ProcessDefinitionStatusEnum.Invalid.getIntValue());

		m_btnUse.setEnabled(bo == m_btnUse ? false : true);
		// 停用的流程不能够修改
		//m_boEdit.setEnabled(bo == m_btnUse ? true : false);
		m_boEdit.setEnabled(bo == m_btnDisUse ? true : false);
		m_btnDisUse.setEnabled(bo == m_btnDisUse ? false : true);
		ProcessDefinitionUtil.setProcessDefinitionStatus(vo.getPk_prodef(), 
				bo == m_btnUse ? ProcessDefinitionStatusEnum.Valid : ProcessDefinitionStatusEnum.Suspend);
	}

	public void onBtnRefresh() {
		List<SortItem> items = getTableModel().getSortColumns();
		int[] selectRows = getTable().getSelectedRows();
		initTableData(isShowSealFlow);
		// 按原有顺序排列
		if (items != null)
			getTableModel().sortByColumns(items, selectRows);
	}

	private void onBtnAdd() {
		new BpmnModelerStarter().startModeler(null, WorkflowsManager.this);
	}

	private void onBtnEdit() {
		if (!checkLineSelected())
			return;

		ProcessDefinitionVO vo = (ProcessDefinitionVO) getTableModel().getVO(
				getTable().getSelectedRow());
		String pkProDef = vo.getPk_prodef();
		if (vo.getValidity() != null && vo.getValidity() == ProcessDefinitionStatusEnum.Suspend.getIntValue()) {
			MessageDialog.showErrorDlg(null, null, "停用的流程不能进行编辑！");
			return;
		}
		if (!checkIsLocked(pkProDef)) {
//			return;
		}
		getLockedProcessPKs().add(pkProDef);
		new BpmnModelerStarter().startModeler(pkProDef, WorkflowsManager.this);
	}

	private void onBtnDel() {
		ProcessDefinitionVO vo = (ProcessDefinitionVO) getTableModel().getVO(
				getTable().getSelectedRow());
		String pkProcDef = vo.getPk_prodef();

		if (!checkLineSelected())
			return;

		if (!checkIsLocked(pkProcDef)) {
			return;
		}

		if (MessageDialog.showOkCancelDlg(WorkflowsManager.this, null,
				NCLangRes.getInstance().getStrByID("pfworkflow",
						"BusitypeClientUI2-000005")/* 是否确认删除? */) != MessageDialog.ID_OK) {
			// 释放锁
			freeProcessPK(pkProcDef);
			return;
		}

		try {
			NCLocator.getInstance().lookup(IDeployService.class).deleteProcessDefinition(pkProcDef);
			getTableModel().removeVO(getTable().getSelectedRow());
			getTableModel().refreshTable();

		} catch (Exception e) {

			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(WorkflowsManager.this, null, e
					.getMessage());

		} finally {
			freeProcessPK(pkProcDef);
		}

	}

	private boolean checkLineSelected() {
		int selectedRow = getTable().getSelectedRow();
		if (selectedRow == -1) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"_beans", "UPP_Beans-000053")/* 提示 */, 
					NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000677")/** @res "请选择一条流程定义"*/);
			return false;
		}
		return true;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ProcessDefinitionVO vo = (ProcessDefinitionVO) getTableModel().getVO(
				getTable().getSelectedRow());
		if (vo != null) {
			if (vo.getValidity() == null) {
				// 初始状态
				m_btnUse.setEnabled(true);
				m_btnDisUse.setEnabled(false);
				m_boEdit.setEnabled(true);
			} else {
				m_btnUse.setEnabled(ProcessDefinitionStatusEnum.Valid.getIntValue() == vo.getValidity() ? false : true);
				m_btnDisUse.setEnabled(ProcessDefinitionStatusEnum.Valid.getIntValue() != vo.getValidity() ? false : true);
				m_boEdit.setEnabled(ProcessDefinitionStatusEnum.Invalid.getIntValue() == vo.getValidity() ? true : false);
			}
		}
	}
    //鼠标右键事件响应
	private  class treeMouseListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.isMetaDown()){         //检测鼠标右键单击
                new InputDialog();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//新建类别对话框
	public class InputDialog extends UIDialog implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private JLabel nameLabel;
		private JTextArea nameText;
		private JButton ok;
		private JButton cancel;
		@SuppressWarnings("deprecation")
		public InputDialog()
		{
			nameLabel=new JLabel("类名：");
			nameLabel.setBounds( 35, 20,50, 20);
			nameText=new JTextArea();
			nameText.setBounds( 80, 20,70, 20);
			ok=new JButton("确定");
			ok.setBounds( 35, 56,40, 20);
			ok.addActionListener(this);
			cancel=new JButton("取消");
			cancel.setBounds( 94, 56,40, 20);
			this.setTitle("新建类别");
			this.setLocation(200,180);
			this.setSize(180, 115);
		    this.setLayout(null);
			this.add(nameLabel);
			this.add(nameText);
			this.add(ok);
			this.add(cancel);
			cancel.addActionListener(this);
			this.setVisible(true);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==ok)
			{
			  GroupMutableTreeNode selectedNode=(GroupMutableTreeNode) processTree.getLastSelectedPathComponent();//返回最后选定的节点
			  if(selectedNode!=null)
			  {
				   ProcessDefGroupVO vo=new ProcessDefGroupVO();
				   vo.setCode("aaa");
				   vo.setName(nameText.getText());
				   vo.setPk_parentgroup(selectedNode.getId());
				   vo.setPk_prodefgroup("123456");
				   WfmServiceFacility.getProcessDefGroup().insert(vo);   //流程分组表中插入一行
            		initTreeData();
                    treeModel=new DefaultTreeModel(processTreeRoot);
                    treeModel.reload();
            		processTree.setModel(treeModel);
			  }
			}
			this.dispose();
		}
		
	}

	public class GroupMutableTreeNode extends DefaultMutableTreeNode
	{
		private static final long serialVersionUID = 1L;
		private String id;
		public GroupMutableTreeNode(String id,String name)
		{
			this.id=id;
			this.name=name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private String name;
		public String toString()
		{
			return name;
		}
		
	}

}
