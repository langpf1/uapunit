package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import uap.workflow.admin.IWorkflowDefine;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLNames;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.definition.WorkflowDefinitionVO;
import nc.vo.wfengine.pub.WFTask;

/**
 * 工作流启动 指派对话框(分支选择和用户指派)
 * @author dingxm 2008-9-3
 * @modifier leijun 2008-9 增加工作流的交互UI
 */
public class WFStartDispatchDialog extends UIDialog implements ActionListener {

	/*指派panel*/
	private WFDispatchPanel wfDispatchPanel = null;

	/*按钮panel*/
	private UIPanel btnPane = null;

	/*流程上下文VO */
	private WorkflownoteVO m_worknoteVO = null;

	/*流程缓存*/
	private Hashtable _hashGraphCache = new Hashtable();

	/*后继分支信息*/
	private Vector assignInfos = null;

	private Vector selectInfos = null;

	/*按钮*/
	private UIButton btnOK = null;

	private UIButton btnCancel = null;

	private UIButton btnPro = null;

	/*工作流交互UI*/
	//private AbstractInteractionPane aip = null;

	public WFStartDispatchDialog(Container parent, WorkflownoteVO wfVo) {
		super(parent);
		this.m_worknoteVO = wfVo;
		this.assignInfos = wfVo.getAssignableInfos();
		this.selectInfos = wfVo.getTransitionSelectableInfos();
		initUI();
	}

	private void initUI() {
		setTitle("工作流指派信息");
		setResizable(true);
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnPro().addActionListener(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(600, 350);
		setLayout(new BorderLayout());
		add(getWfDispatchPanel(), BorderLayout.CENTER);
		add(getBtnPane(), BorderLayout.SOUTH);

		//leijun@2008-9 增加工作流交互面板
		//if (getInteractionPane() != null)
		//	add(getInteractionPane(), BorderLayout.NORTH);
	}

	/**
	 * 获得指派panel
	 * @return
	 */
	public WFDispatchPanel getWfDispatchPanel() {
		if (wfDispatchPanel == null) {
			wfDispatchPanel = new WFDispatchPanel(assignInfos, selectInfos);
		}
		return wfDispatchPanel;
	}

	/**
	 * 按钮panel
	 * @return
	 */
	private UIPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new UIPanel();
			btnPane.add(getBtnOK(), null);
			btnPane.add(getBtnCancel(), null);
			btnPane.add(getBtnPro(), null);
		}
		return btnPane;
	}

	/**
	 * 获得确定按钮
	 * @return
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000253")/*@res "指派"*/);
		}
		return btnOK;
	}

	/**
	 * 获得取消按钮
	 * @return
	 */
	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
		}
		return btnCancel;
	}

	/**
	 * 获得流程按钮
	 * @return
	 */
	private UIButton getBtnPro() {
		if (btnPro == null) {
			btnPro = new UIButton();
			btnPro
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000252")/*@res "流程>>"*/);
		}
		return btnPro;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			onOK();
		} else if (e.getSource() == getBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getBtnPro()) {
			//流程
			onBtnGraphClicked();
		}
	}

	/**
	 * 确定
	 */
	protected void onOK() {
		//指派分支
		getTransitionSelectableInfo();

		//指派参与者
		getWfDispatchPanel().getAssignableInfo();

		//交互UI返回的变量值对，如果发生业务异常，则显示出来
		//if (getInteractionPane() != null)
		//	m_workFlow.setHmVariable(getInteractionPane().getHmVariable());

		this.closeOK();
	}

	/**
	 * 如果有分支选择，获得被选中的按钮的值（左边panel）
	 * @return
	 */
	private boolean getTransitionSelectableInfo() {
		return getWfDispatchPanel().selectTransition();
	}

	/**
	 * 指派对话框中显示待启动的工作流程图
	 */
	private void onBtnGraphClicked() {

		String defPK = m_worknoteVO.getTaskInstanceVO().getPk_process_def();

		JComponent centerComp = null;
		Object obj = _hashGraphCache.get(defPK);
		if (obj == null) {
			try {
				IProcessDefinition def_vo = NCLocator.getInstance().lookup(IWorkflowDefine.class)
						.findDefinitionByPrimaryKey(defPK);

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000237")/*@res "错误"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000494")/*查询单据的流程图出现异常："*/
						+ e.getMessage());
				return;
			}

			//缓存
			_hashGraphCache.put(defPK, centerComp);
		} else {
			//auditChart = (FlowChart) obj;
			centerComp = (JComponent) obj;
		}

		//显示
		PfUtilUITools.showDialog(this, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000138")/*@res "流程图"*/, centerComp);

	}

	private JComponent constructGraphTab(WorkflowProcess wp) {
		UITabbedPane tabPane = new UITabbedPane();

		//构造一个临时包
		XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
		pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

		wp.setPackage(pkg);

		//初始化Graph
		FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		//启用工具提示
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		//auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);

		//高亮起始活动
		HashSet hsRunningActivityDefIds = new HashSet();
		hsRunningActivityDefIds.add(wp.findStartActivity().getId());
		auditChart.setActivityRouteHighView(hsRunningActivityDefIds, new String[] { wp
				.findStartActivity().getId() }, new ArrayList(), Color.RED, Color.BLUE);

		UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
		JViewport vport = graphScrollPane.getViewport();
		vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

		String title = auditChart.getWorkflowProcess().getName() + " "
				+ ((BasicWorkflowProcess) auditChart.getWorkflowProcess()).getVersion();
		//graphScrollPane.setBorder(BorderFactory.createEtchedBorder());
		tabPane.addTab(title, graphScrollPane);
		return tabPane;
	}

}
