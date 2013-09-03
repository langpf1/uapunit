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
 * ҵ��������ڵ㣬�����µ�ҵ�����༭������ҵ����
 * 
 * @modidier yanke1 2011-7-25 ��ť����/ͣ�ò�����UE�淶����Ҫ����GroupAction��ʵ�ָð�ť��
 *           ��˽�ButtonObject[]����ΪAction[]������ToftPanel��setMenuAction()�������ò˵���ť
 *           ������getActionsFromButtonGroup()
 */
public class BusitypeClientUI2 extends PFToftPanel implements
		ListSelectionListener {

	private ButtonObject m_boAdd = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "addBtnName")/*
													 * @ res "����"
													 */, NCLangRes
			.getInstance().getStrByID("pfworkflow", "addBtnName")/*
																 * @res "����"
																 */, 2, "Add"/* ���� */);

	private ButtonObject m_boEdit = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/*
												 * @ res "�޸�"
												 */, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000045")/*
												 * @res "�޸�"
												 */, 2, "Edit"/* �޸� */);

	private ButtonObject m_boDelete = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/*
												 * @res "ɾ��"
												 */, NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000039")/*
												 * @res "ɾ��"
												 */, 2, "Delete"/* ɾ�� */);

	protected ButtonObject m_boRefresh = new ButtonObject(NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/*
																 * @res "ˢ��"
																 */, NCLangRes
			.getInstance().getStrByID("common", "UC001-0000009")/*
																 * @res* "ˢ��"
																 */, 2,
			"Refresh"/* ˢ�� */);

	private ButtonObject m_btnFliter = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "BusitypeClientUI2-000000")/* ���� */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"BusitypeClientUI2-000000")/* ���� */, 2, "Fliter"/* ���� */);

	private ButtonObject m_btnShowSealedFlow = new ButtonObject(NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000004")/* ��ʾͣ������ */, NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000004")/* ��ʾͣ������ */, 2,
			"ShowSealedFlow"/* ��ʾͣ������ */);

	private GroupButtonObject m_btnUseOperate = new GroupButtonObject(NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* ���� */, NCLangRes
			.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* ���� */, 2, "UseOperate"/* ���� */);

	private ButtonObject m_btnUse = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "UfWorkflowDesignerUI-000001")/* ���� */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000001")/* ���� */, 2, "Use"/* ���� */);

	private ButtonObject m_btnDisUse = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("pfworkflow", "UfWorkflowDesignerUI-000002")/* ͣ�� */,
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"UfWorkflowDesignerUI-000002")/* ͣ�� */, 2, "DisUse"/* ͣ�� */);

	private ButtonObject m_btnPreview = new ButtonObject(
			NCLangRes.getInstance().getStrByID("pfworkflow",
					"BusitypeClientUI2-000001")/* Ԥ�� */, NCLangRes
					.getInstance().getStrByID("pfworkflow",
							"BusitypeClientUI2-000001")/* Ԥ�� */, "Preview"/* Ԥ�� */);

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

	/** ��ǰ�༭���б����������̶���PK */
	private ArrayList<String> alLockedProcessPKs = new ArrayList<String>();

	public BusitypeClientUI2() {
		super();
	}

	@Override
	public void init() {
		initialize();
	}

	/**
	 * ��ʼ��
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
	 * �ж��Ƿ񱻼���
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
											 * @ res "��ʾ"
											 */, NCLangRes.getInstance()
							.getStrByID("pfworkflow", "editCollision")/*
																	 * @ res
																	 * "���̶������������˱༭��"
																	 */);

		}
		return isLockOK;
	}

	/**
	 * ��ʼ�����ݣ���ѯ����ǰ��½�����µ�����ҵ����
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
		return NCLangRes.getInstance().getStrByID("pfworkflow", "appTitle")/* ���̶��� */;
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
	 * Ԥ��
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
	 * ����ʱ��У��ҵ����
	 * */
	private void onDisuse(ButtonObject bo) {
		if (!checkLineSelected())
			return;
		BusitypeVO vo = (BusitypeVO) getTableModel().getVO(
				getTableBillBusi().getSelectedRow());
		try {

			if (bo == m_btnUse) {
				// ����ʱ����Ҫ�ж��Ƿ���ȷ��
				boolean isvalidity = NCLocator.getInstance()
						.lookup(IGraphPersist.class)
						.checkGraphValidity(vo.getPk_busitype());
				if (!isvalidity) {
					MessageDialog.showErrorDlg(
							null,
							null,
							NCLangRes.getInstance().getStrByID("pfworkflow",
									"BusitypeClientUI2-000003")/*
																 * ҵ�����������
																 * �޷�������
																 */);
					return;
				}
				// �ж��Ƿ������ͬ��Ҫ�ص�ҵ����
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
																 * ���ܴ�����Ҫ����ͬ��ҵ������
																 * �޷�������
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
		//ͣ�õ����̲��ܹ��޸�
		m_boEdit.setEnabled(bo == m_btnUse?true:false);
	}

	public void onBtnRefresh() {
		List<SortItem> items = getTableModel().getSortColumns();
		int[] selectRows = getTableBillBusi().getSelectedRows();
		initTableData(isShowSealFlow);
		// ��ԭ��˳������
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
		// MessageDialog.showErrorDlg(null, null, "ͣ�õ����̲��ܽ��б༭��");
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
						"BusitypeClientUI2-000005")/* �Ƿ�ȷ��ɾ��? */) != MessageDialog.ID_OK) {
			// �ͷ���
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
							"UPP_Beans-000053")/* ��ʾ */,
					NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000677")/*
													 * @res "��ѡ��һ�����̶���"
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
				// ��ʼ״̬
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
