package nc.ui.pub.bizflow;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uap.workflow.modeler.BpmnModelerStarter;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.uap.lock.PKLock;
import nc.funcnode.ui.action.CheckboxAction;
import nc.itf.uap.graph.IGraphPersist;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFMetaModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.SeparatorButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.desktop.GroupButtonObject;
import nc.ui.pub.desktop.PFToftPanel;
import nc.ui.pub.flowdesigner.editor.GraphPreviewDlgMaker;
import nc.ui.pub.graph.starter.GraphEditStarter;
import nc.ui.pub.hotkey.HotkeyFactory;
import nc.ui.pub.hotkey.IHotkeyTypes;
import nc.ui.wfengine.designer.FlowDefCellRender;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.hotkey.NCKey;
import nc.vo.wfengine.definition.WorkflowDefStatusEnum;

/**
 * 业务流定义节点，采用新的业务流编辑器定义业务流
 * 
 * @modidier yanke1 2011-7-25 按钮启用/停用不符合UE规范，需要采用GroupAction来实现该按钮组
 *           因此将ButtonObject[]适配为Action[]，采用ToftPanel的setMenuAction()方法设置菜单按钮
 *           见方法getActionsFromButtonGroup()
 */
public class BusitypeClientUI2 extends PFToftPanel implements
		ListSelectionListener {

	private ButtonObject m_boAdd = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "addBtnName")/*
													 * @ res "增加"
													 */, NCLangRes
			.getInstance().getStrByID("pfworkflow", "addBtnName")/*
																 * @res "增加"
																 */, 2, "Add"/* 增加 */);

	private ButtonObject m_boEdit = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/*
												 * @ res "修改"
												 */, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/*
												 * @res "修改"
												 */, 2, "Edit"/* 修改 */);

	private ButtonObject m_boDelete = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/*
												 * @res "删除"
												 */, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/*
												 * @res "删除"
												 */, 2, "Delete"/* 删除 */);

	protected ButtonObject m_boRefresh = new ButtonObject(NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/*
																 * @res "刷新"
																 */, NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/*
																 * @res* "刷新"
																 */, 2,
			"Refresh"/* 刷新 */);

	private ButtonObject m_btnFliter = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "BusitypeClientUI2-000000")/* 过滤 */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"BusitypeClientUI2-000000")/* 过滤 */, 2, "Fliter"/* 过滤 */);

	private ButtonObject m_btnShowSealedFlow = new ButtonObject(NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000004")/* 显示停用流程 */, NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000004")/* 显示停用流程 */, 2,
			"ShowSealedFlow"/* 显示停用流程 */);

	private GroupButtonObject m_btnUseOperate = new GroupButtonObject(NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* 启用 */, NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* 启用 */, 2, "UseOperate"/* 启用 */);

	private ButtonObject m_btnUse = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "UfWorkflowDesignerUI-000001")/* 启用 */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* 启用 */, 2, "Use"/* 启用 */);

	private ButtonObject m_btnDisUse = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "UfWorkflowDesignerUI-000002")/* 停用 */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000002")/* 停用 */, 2, "DisUse"/* 停用 */);

	private ButtonObject m_btnPreview = new ButtonObject(
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"BusitypeClientUI2-000001")/* 预览 */, NCLangRes
					.getInstance().getStrByID("pfworkflow",
							"BusitypeClientUI2-000001")/* 预览 */, "Preview"/* 预览 */);

	private CheckboxAction m_actShowSealedFlow = new CheckboxAction(
			m_btnShowSealedFlow.getCode(), m_btnShowSealedFlow.getName(),
			m_btnShowSealedFlow.getHint()) {
		@Override
		public void actionPerformed(ActionEvent e) {
			isShowSealFlow = !isShowSealFlow;
			onBtnRefresh();
		}
	};
	
	private SeparatorButtonObject m_btnSeparator = new SeparatorButtonObject();

	private ButtonObject[] m_MainButtonGroup = { m_boAdd, m_boEdit, m_boDelete, m_btnSeparator,
			m_boRefresh, m_btnFliter, m_btnSeparator, m_btnUseOperate, m_btnPreview };

	private UITablePane tablePane = null;

	private boolean isShowSealFlow = false;

	/** 当前编辑器中被加锁的流程定义PK */
	private ArrayList<String> alLockedProcessPKs = new ArrayList<String>();

	public BusitypeClientUI2() {
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

		add(getUITablePane(), "Center");

		initTableModel();
		initTableData(false);
		getTableBillBusi().getSelectionModel().addListSelectionListener(this);
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
	
	public void freeProcessPKs(String[] proc_def_pks) {
		if (proc_def_pks == null || proc_def_pks.length == 0) {
			return;
		}
		
		PKLock.getInstance().releaseBatchLock(proc_def_pks, PfUtilUITools.getLoginUser(), null);
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
			MessageDialog.showHintDlg(
					this,
					NCLangRes.getInstance().getStrByID("pfworkflow",
							"hintDialogTitle")/*
											 * @ res "提示"
											 */, NCLangRes.getInstance()
							.getStrByID("pfworkflow", "editCollision")/*
																	 * @ res
																	 * "流程定义正被其他人编辑！"
																	 */);

		}
		return isLockOK;
	}

	/**
	 * 初始化数据，查询出当前登陆集团下的所有业务流
	 * */
	private void initTableData(boolean isShowSealFlow) {
		getTableModel().clearTable();
		BusitypeVO[] m_busitypeVOs = null;
		String pk_group = PfUtilUITools.getLoginGroup();
		try {
			if (!isShowSealFlow) {
				m_busitypeVOs = ((IPFMetaModel) NCLocator.getInstance().lookup(
						IPFMetaModel.class.getName()))
						.queryAllBusitypes(pk_group);
			} else {
				m_busitypeVOs = ((IPFMetaModel) NCLocator.getInstance().lookup(
						IPFMetaModel.class.getName()))
						.queryAllBusitypesWithSealed(pk_group);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if (m_busitypeVOs != null)
			getTableModel().addVO(m_busitypeVOs);
		// getTableModel().refreshTable();
	}

	private UITable getTableBillBusi() {
		final UITable table = (UITable) getUITablePane().getTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private BusitypeModel getTableModel() {
		return (BusitypeModel) getTableBillBusi().getModel();
	}

	private void initTableModel() {
		BusitypeModel model = new BusitypeModel(BusitypeVO.class);
		getTableBillBusi().setModel(model);
		getTableBillBusi().setColumnWidth(model.getColumnWidth());
		getTableBillBusi()
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableBillBusi().getColumnModel().getColumn(11)
				.setCellRenderer(new FlowDefCellRender());
		getTableBillBusi().addSortListener();
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
		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		GraphPreviewDlgMaker.showGraphPreviewDlg(vo.getPk_busitype(),
				getDialogLocation(getTableBillBusi(), 2));
	}

	/**
	 * 启动时会校验业务流
	 * */
	private void onDisuse(ButtonObject bo) {
		if (!checkLineSelected())
			return;
		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		try {

			if (bo == m_btnUse) {
				// 启用时候需要判断是否正确，
				boolean isvalidity = NCLocator.getInstance()
						.lookup(IGraphPersist.class)
						.checkGraphValidity(vo.getPk_busitype());
				if (!isvalidity) {
					MessageDialog.showErrorDlg(
							null,
							null,
							NCLangRes.getInstance().getStrByID("pfworkflow",
									"BusitypeClientUI2-000003")/*
																 * 业务流定义错误，
																 * 无法启动！
																 */);
					return;
				}
				// 判断是否存在相同四要素的业务流
				String billtypecode = null;
				String trantypecode = null;
				BilltypeVO billvo = PfDataCache.getBillType(vo
						.getPrimarybilltype());
				if (billvo.getIstransaction() != null
						&& billvo.getIstransaction().booleanValue()) {
					billtypecode = billvo.getParentbilltype();
					trantypecode = vo.getPrimarybilltype();
				} else {
					billtypecode = vo.getPrimarybilltype();
				}
				boolean isDuplicate = NCLocator
						.getInstance()
						.lookup(IPFConfig.class)
						.existDuplicateBusiFlow(billtypecode, trantypecode,
								vo.getPk_orgs(), vo.getPrimaryKey())
						.booleanValue();
				if (isDuplicate) {
					MessageDialog.showErrorDlg(
							null,
							null,
							NCLangRes.getInstance().getStrByID("pfworkflow",
									"BusitypeClientUI2-000004")/*
																 * 不能存在四要素相同的业务流，
																 * 无法启动！
																 */);
					return;
				}
			}
			NCLocator
					.getInstance()
					.lookup(IPFConfig.class)
					.updateProcessValidation(vo.getPk_busitype(),
							bo == m_btnUse);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		vo.setValidity(bo == m_btnUse ? WorkflowDefStatusEnum.Valid
				.getIntValue() : WorkflowDefStatusEnum.Invalid.getIntValue());

		m_btnUse.setEnabled(bo == m_btnUse ? false : true);
		m_btnDisUse.setEnabled(bo == m_btnDisUse ? false : true);
		//停用的流程不能够修改
		m_boEdit.setEnabled(bo == m_btnUse?true:false);
	}

	public void onBtnRefresh() {
		List<SortItem> items = getTableModel().getSortColumns();
		int[] selectRows = getTableBillBusi().getSelectedRows();
		initTableData(isShowSealFlow);
		// 按原有顺序排列
		if (items != null)
			getTableModel().sortByColumns(items, selectRows);
	}

	private void onBtnAdd() {
 	    new BpmnModelerStarter().startModeler(null, BusitypeClientUI2.this);
		//new GraphEditStarter().startEditor(null, BusitypeClientUI2.this);
	}

	private void onBtnEdit() {
		if (!checkLineSelected())
			return;

		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		String pk_busitype = vo.getPk_busitype();
		// if (vo.getValidity() == WorkflowDefStatusEnum.Invalid.getIntValue())
		// {
		// MessageDialog.showErrorDlg(null, null, "停用的流程不能进行编辑！");
		// return;
		// }
		if (!checkIsLocked(pk_busitype)) {
			return;
		}
		getLockedProcessPKs().add(pk_busitype);
		new GraphEditStarter().startEditor(pk_busitype, BusitypeClientUI2.this);
	}

	private void onBtnDel() {
		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		String pk_busitype = vo.getPk_busitype();

		if (!checkLineSelected())
			return;

		if (!checkIsLocked(pk_busitype)) {
			return;
		}

		if (MessageDialog.showOkCancelDlg(
				BusitypeClientUI2.this,
				null,
				NCLangRes.getInstance().getStrByID("pfworkflow",
						"BusitypeClientUI2-000005")/* 是否确认删除? */) != MessageDialog.ID_OK) {
			// 释放锁
			freeProcessPK(pk_busitype);
			return;
		}

		try {
			NCLocator.getInstance().lookup(IPFMetaModel.class)
					.deleteBusitypeByVO(vo);
			getTableModel().removeVO(getTableBillBusi().getSelectedRow());
			getTableModel().refreshTable();

		} catch (BusinessException e) {

			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(BusitypeClientUI2.this, null,
					e.getMessage());

		} finally {
			freeProcessPK(pk_busitype);
		}

	}

	private boolean checkLineSelected() {
		int selectedRow = getTableBillBusi().getSelectedRow();
		if (selectedRow == -1) {
			MessageDialog.showHintDlg(
					this,
					NCLangRes.getInstance().getStrByID("_beans",
							"UPP_Beans-000053")/* 提示 */,
					NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000677")/*
													 * @res "请选择一条流程定义"
													 */);
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean funcletCloseing() {
		List<String> lockedPkList = getLockedProcessPKs();
		
		if (lockedPkList == null || lockedPkList.size() == 0) {
			return true;
		}
		
		String[] lockedPks = lockedPkList.toArray(new String[0]);
		freeProcessPKs(lockedPks);
		
		return true;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		if (vo != null) {
			if (vo.getValidity() == null) {
				// 初始状态
				m_btnUse.setEnabled(true);
				m_btnDisUse.setEnabled(false);
				m_boEdit.setEnabled(true);
			} else {
				m_btnUse.setEnabled(WorkflowDefStatusEnum.Valid.getIntValue() == vo
						.getValidity() ? false : true);
				m_btnDisUse.setEnabled(WorkflowDefStatusEnum.Valid
						.getIntValue() != vo.getValidity() ? false : true);
				m_boEdit.setEnabled(WorkflowDefStatusEnum.Invalid.getIntValue() == vo
						.getValidity() ? false : true);
			}
		}
	}

}
