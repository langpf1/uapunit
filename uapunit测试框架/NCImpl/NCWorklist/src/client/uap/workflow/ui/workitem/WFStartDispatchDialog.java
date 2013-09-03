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
 * ���������� ָ�ɶԻ���(��֧ѡ����û�ָ��)
 * @author dingxm 2008-9-3
 * @modifier leijun 2008-9 ���ӹ������Ľ���UI
 */
public class WFStartDispatchDialog extends UIDialog implements ActionListener {

	/*ָ��panel*/
	private WFDispatchPanel wfDispatchPanel = null;

	/*��ťpanel*/
	private UIPanel btnPane = null;

	/*����������VO */
	private WorkflownoteVO m_worknoteVO = null;

	/*���̻���*/
	private Hashtable _hashGraphCache = new Hashtable();

	/*��̷�֧��Ϣ*/
	private Vector assignInfos = null;

	private Vector selectInfos = null;

	/*��ť*/
	private UIButton btnOK = null;

	private UIButton btnCancel = null;

	private UIButton btnPro = null;

	/*����������UI*/
	//private AbstractInteractionPane aip = null;

	public WFStartDispatchDialog(Container parent, WorkflownoteVO wfVo) {
		super(parent);
		this.m_worknoteVO = wfVo;
		this.assignInfos = wfVo.getAssignableInfos();
		this.selectInfos = wfVo.getTransitionSelectableInfos();
		initUI();
	}

	private void initUI() {
		setTitle("������ָ����Ϣ");
		setResizable(true);
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnPro().addActionListener(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(600, 350);
		setLayout(new BorderLayout());
		add(getWfDispatchPanel(), BorderLayout.CENTER);
		add(getBtnPane(), BorderLayout.SOUTH);

		//leijun@2008-9 ���ӹ������������
		//if (getInteractionPane() != null)
		//	add(getInteractionPane(), BorderLayout.NORTH);
	}

	/**
	 * ���ָ��panel
	 * @return
	 */
	public WFDispatchPanel getWfDispatchPanel() {
		if (wfDispatchPanel == null) {
			wfDispatchPanel = new WFDispatchPanel(assignInfos, selectInfos);
		}
		return wfDispatchPanel;
	}

	/**
	 * ��ťpanel
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
	 * ���ȷ����ť
	 * @return
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000253")/*@res "ָ��"*/);
		}
		return btnOK;
	}

	/**
	 * ���ȡ����ť
	 * @return
	 */
	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);
		}
		return btnCancel;
	}

	/**
	 * ������̰�ť
	 * @return
	 */
	private UIButton getBtnPro() {
		if (btnPro == null) {
			btnPro = new UIButton();
			btnPro
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000252")/*@res "����>>"*/);
		}
		return btnPro;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			onOK();
		} else if (e.getSource() == getBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getBtnPro()) {
			//����
			onBtnGraphClicked();
		}
	}

	/**
	 * ȷ��
	 */
	protected void onOK() {
		//ָ�ɷ�֧
		getTransitionSelectableInfo();

		//ָ�ɲ�����
		getWfDispatchPanel().getAssignableInfo();

		//����UI���صı���ֵ�ԣ��������ҵ���쳣������ʾ����
		//if (getInteractionPane() != null)
		//	m_workFlow.setHmVariable(getInteractionPane().getHmVariable());

		this.closeOK();
	}

	/**
	 * ����з�֧ѡ�񣬻�ñ�ѡ�еİ�ť��ֵ�����panel��
	 * @return
	 */
	private boolean getTransitionSelectableInfo() {
		return getWfDispatchPanel().selectTransition();
	}

	/**
	 * ָ�ɶԻ�������ʾ�������Ĺ�������ͼ
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
						"UPPpfworkflow-000237")/*@res "����"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000494")/*��ѯ���ݵ�����ͼ�����쳣��"*/
						+ e.getMessage());
				return;
			}

			//����
			_hashGraphCache.put(defPK, centerComp);
		} else {
			//auditChart = (FlowChart) obj;
			centerComp = (JComponent) obj;
		}

		//��ʾ
		PfUtilUITools.showDialog(this, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000138")/*@res "����ͼ"*/, centerComp);

	}

	private JComponent constructGraphTab(WorkflowProcess wp) {
		UITabbedPane tabPane = new UITabbedPane();

		//����һ����ʱ��
		XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
		pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

		wp.setPackage(pkg);

		//��ʼ��Graph
		FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		//���ù�����ʾ
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		//auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);

		//������ʼ�
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
