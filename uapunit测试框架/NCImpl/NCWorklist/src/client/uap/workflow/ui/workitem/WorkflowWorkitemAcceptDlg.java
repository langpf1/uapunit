package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import uap.workflow.vo.WorkflownoteVO;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.pub.pf.TransitionSelectableInfo;

/**
 * 工作流的工作项处理对话框（指派、批语）
 * 
 * @author dingxm 2008-9-3
 * @since 5.5
 * @modifier leijun 2009-9 适配到V6
 */
public class WorkflowWorkitemAcceptDlg extends UIDialog implements ActionListener {

	/*主Panel*/
	private UIPanel mainPane;

	/*指派对话框Panel*/
	private WFDispatchPanel passPane;

	/*是否显示指派对话框*/
	private boolean isShow = false;

	/*指派的用户信息*/
	private Vector<AssignableInfo> assignUser = null;

	/*指派的分支信息*/
	private Vector<TransitionSelectableInfo> tSelectInfos = null;

	/*批示内容Panel*/
	private UIPanel contentPane;

	/*批语文本域*/
	private UITextArea ivjtaCheckNote = null;

	private UIScrollPane textScroll = null;

	private WorkflownoteVO m_workFlow = null;

	/*按钮*/
	private UIButton btnCancel = null;

	private UIButton btnOK = null;

	private UIButton btnPro = null;

	public WorkflowWorkitemAcceptDlg(Container parent, WorkflownoteVO wfVo) {
		super(parent);
		assignUser = wfVo.getAssignableInfos();
		tSelectInfos = wfVo.getTransitionSelectableInfos();
		this.m_workFlow = wfVo;

		initUI();
	}

	private void initUI() {
		//	设置此 dialog 是否可以由用户调整大小
		setResizable(true);
		setName("wfTest");
		if (isShow()) {
			setSize(600, 650);
		} else {
			setSize(600, 450);
		}

		setTitle("执行工作流");

		//	设置当用户在此对话框上发起 "close" 时默认执行的操作
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//	添加侦听器
		addEventListener();
		//	设置panel
		setContentPane(getMainPane());
	}

	public WorkflownoteVO getWorkFlow() {
		return m_workFlow;
	}

	/**
	 * UI主panel
	 */
	public UIPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new UIPanel();
			mainPane.setName("主pane");
			mainPane.setLayout(new BorderLayout());

			//			审批对话框是否显示
			if (isShow()) {
				mainPane.add(getPassPane(), BorderLayout.CENTER);
				mainPane.add(getContentPane(), BorderLayout.SOUTH);
			} else {
				mainPane.add(getContentPane(), BorderLayout.CENTER);
			}

		}
		return mainPane;
	}

	/**
	 * 指派对话框panel
	 */
	public WFDispatchPanel getPassPane() {
		if (passPane == null) {
			passPane = new WFDispatchPanel(assignUser, tSelectInfos);
		}
		return passPane;
	}

	/**
	 * 是否显示指派对话框
	 * @return
	 */
	private boolean isShow() {
		if (!isShow) {
			isShow = getPassPane().isShow();
		}
		return isShow;
	}

	/**
	 * 批示内容Panel
	 */
	public UIPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new UIPanel();
			contentPane.setBorder(BorderFactory.createTitledBorder(null, NCLangRes.getInstance()
					.getStrByID("102220", "UPP102220-000154")/*@res "批示内容："*/, TitledBorder.CENTER,
					TitledBorder.TOP));
			contentPane.setPreferredSize(new Dimension(300, 200));
			contentPane.setLayout(new BorderLayout());

			//			添加文本域
			contentPane.add(getScrollAssignText(), BorderLayout.CENTER);
			//			添加按钮
			UIPanel panel = new UIPanel();
			panel.add(getBtnOK());
			panel.add(getBtnCancel());
			panel.add(getBtnPro());
			contentPane.add(panel, BorderLayout.SOUTH);
		}
		return contentPane;
	}

	/**
	 * 文本域的ScrollPane
	 * @return
	 */
	private UIScrollPane getScrollAssignText() {
		if (textScroll == null) {
			textScroll = new UIScrollPane();
			textScroll.setViewportView(gettaCheckNote());
		}
		return textScroll;
	}

	/**
	 * 文本域
	 */
	private UITextArea gettaCheckNote() {
		if (ivjtaCheckNote == null) {
			ivjtaCheckNote = new UITextArea(5, 5);
			ivjtaCheckNote.setName("taCheckNote");
			ivjtaCheckNote.setLineWrap(true);
			ivjtaCheckNote.setMaxLength(1024);
			ivjtaCheckNote.setBounds(-1, 23, 377, 157);
		}
		return ivjtaCheckNote;
	}

	/**
	 * 获得按钮
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setName("btnOK");
			btnOK.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);

		}
		return btnOK;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("btnCancel");
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);

		}
		return btnCancel;
	}

	private UIButton getBtnPro() {
		if (btnPro == null) {
			btnPro = new UIButton();
			btnPro.setName("btnPro");
			btnPro
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000252")/*@res "流程>>"*/);
		}
		return btnPro;
	}

	/**
	 * 事件侦听器 注册
	 */
	private void addEventListener() {
		//		按钮注册
		getBtnPro().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnOK().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == getBtnPro()) {
			//			流程
			showFlowinfo();
		} else if (obj == getBtnCancel()) {
			//			取消
			this.closeCancel();
		} else if (obj == getBtnOK()) {
			//			确定
			onOk();
		}

	}

	/**
	 * "确定"   将为工作流VO对象填充信息
	 */
	protected void onOk() {
		//获取处理意见-通过/不通过/驳回
		m_workFlow.setApproveresult("Y");
		//获取批语
		m_workFlow.setChecknote(gettaCheckNote().getText());
		//获取处理时间
		//m_workFlow.setDealDate(ClientEnvironment.getServerTime());
		//选择分支
		getPassPane().selectTransition();
		//选择 参与者信息
		getPassPane().getAssignableInfo();

		//交互UI返回的变量值对，如果发生业务异常，则显示出来
		//if (getInteractionPane() != null)
		//	m_workFlow.setHmVariable(getInteractionPane().getHmVariable());

		this.closeOK();
	}
	
	/**
	 * 显示流程信息
	 * <li>可能为审批流
	 * <li>也可能为工作流
	 */
	private void showFlowinfo() {
		FlowStateDlg dlg = new FlowStateDlg(this, m_workFlow.getPk_billtype(), m_workFlow
				.getBillVersionPK(), m_workFlow.getWorkflow_type().intValue());
		dlg.setVisible(true);
	}

	/**
	 * "流程"
	 * <li>弹出对话框来显示流程图
	 * <li>对于子流程,还需显示主流程的活动
	 */
//	private void onBtnGraphClicked() {
//		WFTask task = m_workFlow.getTaskInfo().getTask();
//		String defPK = task.getWfProcessDefPK();
//		String procInstancePK = task.getWfProcessInstancePK();
//
//		JComponent centerComp = null;
//		Object obj = _hashGraphCache.get(defPK);
//		if (obj == null) {
//			//找到WFTask所属的活动及其流程定义，包括父流程定义
//			ActivityRouteRes activityRoute = null;
//			IWorkflowDefine wfDefine = (IWorkflowDefine) NCLocator.getInstance().lookup(
//					IWorkflowDefine.class.getName());
//			try {
//				activityRoute = wfDefine.queryActivityRoute(procInstancePK, task.getActivityID());
//			} catch (BusinessException e) {
//				//e.printStackTrace();
//				Logger.error(e.getMessage(), e);
//				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
//						"pfworkflow", "UPPpfworkflow-000494")/*@res "查询单据的审批流程图错误！"*/);
//				return;
//			}
//			centerComp = constructGraphTab(activityRoute);
//
//			//缓存
//			_hashGraphCache.put(defPK, centerComp);
//		} else {
//			centerComp = (JComponent) obj;
//		}
//		//显示
//		PfUtilUITools.showDialog(this, NCLangRes.getInstance().getStrByID("pfworkflow",
//				"UPPpfworkflow-000138")/*@res "流程图"*/, centerComp);
//	}

	/**
	 * 构造流程图页签
	 * @param lhm
	 * @return
	 */
//	private JComponent constructGraphTab(ActivityRouteRes activityRoute) {
//		UITabbedPane tabPane = new UITabbedPane();
//		//构造一个临时包
//		XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
//		pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");
//
//		String def_xpdl = null;
//		ActivityRouteRes currentRoute = activityRoute;
//		while (currentRoute != null) {
//			WfProcessParentMap ppm = (WfProcessParentMap) currentRoute.getProcessParentMap();
//			def_xpdl = ppm.getXpdlString().toString();
//
//			WorkflowProcess wp = null;
//			try {
//				//前台解析XML串为对象
//				wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
//			} catch (XPDLParserException e) {
//				Logger.error(e.getMessage(), e);
//				continue;
//			}
//			wp.setPackage(pkg);
//
//			//初始化Graph
//			FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
//			//启用工具提示
//			ToolTipManager.sharedInstance().registerComponent(auditChart);
//			//auditChart.setEnabled(false);
//			auditChart.populateByWorkflowProcess(wp, false);
//			//auditChart.setBorder(BorderFactory.createEtchedBorder());
//
//			//当前流程实例中的所有活动
//			ActivityInstance[] acts = currentRoute.getActivityInstance();
//			String[] startedActivityDefIds = new String[acts.length];
//			for (int i = 0; i < acts.length; i++) {
//				startedActivityDefIds[i] = acts[i].getActivityID();
//			}
//
//			//当前活动
//			HashSet hsRunningActivityDefIds = new HashSet();
//			hsRunningActivityDefIds.add(ppm.getActivityDefId());
//			auditChart.setActivityRouteHighView(hsRunningActivityDefIds, startedActivityDefIds,
//					currentRoute.getActivityRelations(), Color.RED, Color.BLUE);
//
//			UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
//			JViewport vport = graphScrollPane.getViewport();
//			vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
//
//			String title = auditChart.getWorkflowProcess().getName() + " "
//					+ ((BasicWorkflowProcess) auditChart.getWorkflowProcess()).getVersion();
//			//graphScrollPane.setBorder(BorderFactory.createEtchedBorder());
//			tabPane.addTab(title, graphScrollPane);
//
//			currentRoute = currentRoute.getParentActivityRoute();//取父流程，继续循环
//		}
//
//		return tabPane;
//	}

}
