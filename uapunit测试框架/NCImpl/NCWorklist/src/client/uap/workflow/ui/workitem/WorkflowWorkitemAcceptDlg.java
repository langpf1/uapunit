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
 * �������Ĺ������Ի���ָ�ɡ����
 * 
 * @author dingxm 2008-9-3
 * @since 5.5
 * @modifier leijun 2009-9 ���䵽V6
 */
public class WorkflowWorkitemAcceptDlg extends UIDialog implements ActionListener {

	/*��Panel*/
	private UIPanel mainPane;

	/*ָ�ɶԻ���Panel*/
	private WFDispatchPanel passPane;

	/*�Ƿ���ʾָ�ɶԻ���*/
	private boolean isShow = false;

	/*ָ�ɵ��û���Ϣ*/
	private Vector<AssignableInfo> assignUser = null;

	/*ָ�ɵķ�֧��Ϣ*/
	private Vector<TransitionSelectableInfo> tSelectInfos = null;

	/*��ʾ����Panel*/
	private UIPanel contentPane;

	/*�����ı���*/
	private UITextArea ivjtaCheckNote = null;

	private UIScrollPane textScroll = null;

	private WorkflownoteVO m_workFlow = null;

	/*��ť*/
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
		//	���ô� dialog �Ƿ�������û�������С
		setResizable(true);
		setName("wfTest");
		if (isShow()) {
			setSize(600, 650);
		} else {
			setSize(600, 450);
		}

		setTitle("ִ�й�����");

		//	���õ��û��ڴ˶Ի����Ϸ��� "close" ʱĬ��ִ�еĲ���
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//	���������
		addEventListener();
		//	����panel
		setContentPane(getMainPane());
	}

	public WorkflownoteVO getWorkFlow() {
		return m_workFlow;
	}

	/**
	 * UI��panel
	 */
	public UIPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new UIPanel();
			mainPane.setName("��pane");
			mainPane.setLayout(new BorderLayout());

			//			�����Ի����Ƿ���ʾ
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
	 * ָ�ɶԻ���panel
	 */
	public WFDispatchPanel getPassPane() {
		if (passPane == null) {
			passPane = new WFDispatchPanel(assignUser, tSelectInfos);
		}
		return passPane;
	}

	/**
	 * �Ƿ���ʾָ�ɶԻ���
	 * @return
	 */
	private boolean isShow() {
		if (!isShow) {
			isShow = getPassPane().isShow();
		}
		return isShow;
	}

	/**
	 * ��ʾ����Panel
	 */
	public UIPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new UIPanel();
			contentPane.setBorder(BorderFactory.createTitledBorder(null, NCLangRes.getInstance()
					.getStrByID("102220", "UPP102220-000154")/*@res "��ʾ���ݣ�"*/, TitledBorder.CENTER,
					TitledBorder.TOP));
			contentPane.setPreferredSize(new Dimension(300, 200));
			contentPane.setLayout(new BorderLayout());

			//			����ı���
			contentPane.add(getScrollAssignText(), BorderLayout.CENTER);
			//			��Ӱ�ť
			UIPanel panel = new UIPanel();
			panel.add(getBtnOK());
			panel.add(getBtnCancel());
			panel.add(getBtnPro());
			contentPane.add(panel, BorderLayout.SOUTH);
		}
		return contentPane;
	}

	/**
	 * �ı����ScrollPane
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
	 * �ı���
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
	 * ��ð�ť
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setName("btnOK");
			btnOK.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "ȷ��"*/);

		}
		return btnOK;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("btnCancel");
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);

		}
		return btnCancel;
	}

	private UIButton getBtnPro() {
		if (btnPro == null) {
			btnPro = new UIButton();
			btnPro.setName("btnPro");
			btnPro
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000252")/*@res "����>>"*/);
		}
		return btnPro;
	}

	/**
	 * �¼������� ע��
	 */
	private void addEventListener() {
		//		��ťע��
		getBtnPro().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnOK().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == getBtnPro()) {
			//			����
			showFlowinfo();
		} else if (obj == getBtnCancel()) {
			//			ȡ��
			this.closeCancel();
		} else if (obj == getBtnOK()) {
			//			ȷ��
			onOk();
		}

	}

	/**
	 * "ȷ��"   ��Ϊ������VO���������Ϣ
	 */
	protected void onOk() {
		//��ȡ�������-ͨ��/��ͨ��/����
		m_workFlow.setApproveresult("Y");
		//��ȡ����
		m_workFlow.setChecknote(gettaCheckNote().getText());
		//��ȡ����ʱ��
		//m_workFlow.setDealDate(ClientEnvironment.getServerTime());
		//ѡ���֧
		getPassPane().selectTransition();
		//ѡ�� ��������Ϣ
		getPassPane().getAssignableInfo();

		//����UI���صı���ֵ�ԣ��������ҵ���쳣������ʾ����
		//if (getInteractionPane() != null)
		//	m_workFlow.setHmVariable(getInteractionPane().getHmVariable());

		this.closeOK();
	}
	
	/**
	 * ��ʾ������Ϣ
	 * <li>����Ϊ������
	 * <li>Ҳ����Ϊ������
	 */
	private void showFlowinfo() {
		FlowStateDlg dlg = new FlowStateDlg(this, m_workFlow.getPk_billtype(), m_workFlow
				.getBillVersionPK(), m_workFlow.getWorkflow_type().intValue());
		dlg.setVisible(true);
	}

	/**
	 * "����"
	 * <li>�����Ի�������ʾ����ͼ
	 * <li>����������,������ʾ�����̵Ļ
	 */
//	private void onBtnGraphClicked() {
//		WFTask task = m_workFlow.getTaskInfo().getTask();
//		String defPK = task.getWfProcessDefPK();
//		String procInstancePK = task.getWfProcessInstancePK();
//
//		JComponent centerComp = null;
//		Object obj = _hashGraphCache.get(defPK);
//		if (obj == null) {
//			//�ҵ�WFTask�����Ļ�������̶��壬���������̶���
//			ActivityRouteRes activityRoute = null;
//			IWorkflowDefine wfDefine = (IWorkflowDefine) NCLocator.getInstance().lookup(
//					IWorkflowDefine.class.getName());
//			try {
//				activityRoute = wfDefine.queryActivityRoute(procInstancePK, task.getActivityID());
//			} catch (BusinessException e) {
//				//e.printStackTrace();
//				Logger.error(e.getMessage(), e);
//				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000237")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
//						"pfworkflow", "UPPpfworkflow-000494")/*@res "��ѯ���ݵ���������ͼ����"*/);
//				return;
//			}
//			centerComp = constructGraphTab(activityRoute);
//
//			//����
//			_hashGraphCache.put(defPK, centerComp);
//		} else {
//			centerComp = (JComponent) obj;
//		}
//		//��ʾ
//		PfUtilUITools.showDialog(this, NCLangRes.getInstance().getStrByID("pfworkflow",
//				"UPPpfworkflow-000138")/*@res "����ͼ"*/, centerComp);
//	}

	/**
	 * ��������ͼҳǩ
	 * @param lhm
	 * @return
	 */
//	private JComponent constructGraphTab(ActivityRouteRes activityRoute) {
//		UITabbedPane tabPane = new UITabbedPane();
//		//����һ����ʱ��
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
//				//ǰ̨����XML��Ϊ����
//				wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
//			} catch (XPDLParserException e) {
//				Logger.error(e.getMessage(), e);
//				continue;
//			}
//			wp.setPackage(pkg);
//
//			//��ʼ��Graph
//			FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
//			//���ù�����ʾ
//			ToolTipManager.sharedInstance().registerComponent(auditChart);
//			//auditChart.setEnabled(false);
//			auditChart.populateByWorkflowProcess(wp, false);
//			//auditChart.setBorder(BorderFactory.createEtchedBorder());
//
//			//��ǰ����ʵ���е����л
//			ActivityInstance[] acts = currentRoute.getActivityInstance();
//			String[] startedActivityDefIds = new String[acts.length];
//			for (int i = 0; i < acts.length; i++) {
//				startedActivityDefIds[i] = acts[i].getActivityID();
//			}
//
//			//��ǰ�
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
//			currentRoute = currentRoute.getParentActivityRoute();//ȡ�����̣�����ѭ��
//		}
//
//		return tabPane;
//	}

}
