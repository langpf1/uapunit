package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import uap.workflow.engine.core.XPDLNames;
import uap.workflow.engine.vos.AssignableInfo;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIListToList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.searcher.ListItemSearcher;
import nc.ui.pub.pf.PatternMather;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.definition.WorkflowDefinitionVO;

/**
 * �������ύ����������������ʱʹ�õ�ָ�ɶԻ���
 *
 * @author �׾� created on 2005-1-5
 * @modifier �׾� 2006-7-15 ��ȷ��ָ�ɵ����壬���ȡ��ָ�ɣ���ֹͣ����
 * @modifier leijun 2008-4 ���û��Ϊ�κλָ�ɣ�Ҳֹͣ����
 * @modifier leijun 2008-11 ָ�ɶԻ���������浱ǰ�����������׼����׼���仯
 */
public class DispatchDialog extends UIDialog implements ActionListener {
	private UITabbedPane UITabbedPane = null;

	private UIPanel mainPane = null; //  @jve:decl-index=0:visual-constraint="10,10"

	private UIPanel btnPane = null;

	private UIButton btnOK = null;

	private UIButton btnCancel = null;

	//ӳ��UIListToList��AssignableInfo����
	private Hashtable _hashListToInfo = new Hashtable();

	/*����������VO */
	private WorkflownoteVO m_worknoteVO = null;

	private Vector _assignableInfos = null;

	/**
	 * ����ͼ�Ļ���
	 */
	private Hashtable _hashGraphCache = new Hashtable();

	private UIButton _btnHistoryGraph;

	public DispatchDialog(Container parent) {
		super(parent);

		initialize();
	}

	/**
	 * ��ʼ��ָ�ɶԻ���
	 * @param wfVo
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo) {
		initByWorknoteVO(wfVo, AssignableInfo.CRITERION_NOTGIVEN);
	}

	/**
	 * ָ�ɶԻ���������浱ǰ�����������׼����׼���仯
	 * @param wfVo
	 * @param strCriterion
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo, String strCriterion) {
		this.m_worknoteVO = wfVo;
		_assignableInfos = wfVo.getAssignableInfos();

		//ֻ���ύʱָ�ɶԻ������Ҫ"����ͼ"��ť
		if (PfUtilBaseTools.isSaveAction(m_worknoteVO.getActiontype(), m_worknoteVO.getPk_billtype()))
			getBtnPane().add(getBtnHistoryGraph(), null);

		//��ʼ��ָ�ɵĸ���ҳǩ
		if (_assignableInfos != null) {
			getUITabbedPane().removeAll();
			for (Iterator iter = _assignableInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				String s = ainfo.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_NOTGIVEN.equals(s) || s.equals(strCriterion))
					getUITabbedPane().addTab(ainfo.getDesc(), null, newUIListToList(ainfo), null);
			}
		}
	}

	private UITabbedPane getUITabbedPane() {
		if (UITabbedPane == null) {
			UITabbedPane = new UITabbedPane();
			//UITabbedPane.addTab("�׾�", null, getUIListToList(), null);
		}
		return UITabbedPane;
	}

	/**
	 * �²���һ��ListToList�ؼ�,�����г�ʼ��
	 * <li>1.����ұ��б�
	 * <li>2.�������б�
	 * <li>3.�����
	 * @param ainfo ���ݽ����Ŀ�ָ�ɶ���
	 * @return
	 */
	private UIListToList newUIListToList(AssignableInfo ainfo) {
		// ���ݴ��ݽ�����ָ����Ϣ,��ʼ������
		/*********************
		 * 1.����ұ��б�
		 *********************/
		Vector<OrganizeUnit> ouAssignedUsers = ainfo.getOuAssignedUsers();
		UIListToList ltl = new UIListToList();
		ListItemSearcher lis = new ListItemSearcher(ltl.getLstLeft());
		lis.setMatcher(new PatternMather());
		lis.setSearchHint(NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000139"))/* @res "������������:" */;
		_hashListToInfo.put(ltl, ainfo);

		int numRightUsers = ouAssignedUsers.size();
		ltl.setRightData(ouAssignedUsers.toArray(new OrganizeUnit[0]));

		/****************
		 * 2.���������������б�
		 ****************/
		Vector<OrganizeUnit> vecLeftUsers = new Vector<OrganizeUnit>();
		Iterator iter = ainfo.getOuUsers().iterator();
		while (iter.hasNext()) {
			OrganizeUnit element = (OrganizeUnit) iter.next();
			boolean hasAssigned = false; //�ж���ߴ�ָ���û��Ƿ����ұߴ���
			for (int i = 0; i < numRightUsers; i++) {
				if (ouAssignedUsers.get(i).getPk().equals(element.getPk())) {
					hasAssigned = true;
					break;
				}
			}
			if (!hasAssigned)
				vecLeftUsers.add(element);
		}

		//��ߵĴ�ѡ�û�����
		if (vecLeftUsers.size() > 0) {
			Collections.sort(vecLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//�����ַ�������
					return String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
				}
			});
		}

		ltl.setLeftData(vecLeftUsers.toArray(new OrganizeUnit[vecLeftUsers.size()]));
		return ltl;
	}

	private UIPanel getJPanel() {
		if (mainPane == null) {
			mainPane = new UIPanel();
			mainPane.setLayout(new BorderLayout());
			mainPane.setSize(57, 13);
			mainPane.add(getUITabbedPane(), BorderLayout.CENTER);
			mainPane.add(getBtnPane(), BorderLayout.SOUTH);
		}
		return mainPane;
	}

	private UIPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new UIPanel();
			btnPane.add(getBtnOK(), null);
			btnPane.add(getBtnCancel(), null);
		}
		return btnPane;
	}

	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			//btnOK.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "ȷ��"*/);
			btnOK
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000253")/*@res "ָ��"*/);
		}
		return btnOK;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);
		}
		return btnCancel;
	}

	private UIButton getBtnHistoryGraph() {
		if (_btnHistoryGraph == null) {
			_btnHistoryGraph = new UIButton();
			_btnHistoryGraph.setName("historyGraph");
			_btnHistoryGraph.setText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000252")/*@res "����>>"*/);
		}
		return _btnHistoryGraph;
	}

	private void initialize() {
		this.setContentPane(getJPanel());
		this
				.setTitle(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "ָ����һ���������ڵĲ�����"*/);
		this.setSize(450, 350);
		this.setResizable(true);

		//action listener
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
		getBtnHistoryGraph().addActionListener(this);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			if (!hasDispatched()) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000227")/*@res "��ʾ"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000492")/*@res "��Ϊ�ָ�ɲ����ߣ�"*/);
				return;
			}
			//���㱾��ָ�ɵĽ��
			calculateResults();
			this.closeOK();

		} else if (e.getSource() == getBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getBtnHistoryGraph()) {
			showGraph();
		}
	}

	/**
	 * ָ�ɶԻ�������ʾ����������������ͼ
	 */
	private void showGraph() {
		TaskInstanceVO task = m_worknoteVO.getTaskInstanceVO();
		String defPK = task.getPk_process_def();

		JComponent centerComp = null;
		Object obj = _hashGraphCache.get(defPK);
		if (obj == null) {
			WorkflowProcess wp = null;
			try {
				WorkflowDefinitionVO def_vo = NCLocator.getInstance().lookup(IWorkflowDefine.class)
						.findDefinitionByPrimaryKey(defPK);

				wp = UfXPDLParser.getInstance().parseProcess((String) def_vo.getContent());
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000237")/*@res "����"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000494")/*��ѯ���ݵ�����ͼ�����쳣��"*/
						+ e.getMessage());
				return;
			}
			centerComp = constructGraphTab(wp);

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

	private boolean hasDispatched() {
		Component[] comps = getUITabbedPane().getComponents();
		for (int i = 0; i < comps.length; i++) {
			UIListToList ltl = (UIListToList) comps[i];
			Object[] rightData = ltl.getRightData();
			int iAssigned = rightData == null ? 0 : rightData.length;
			if (iAssigned > 0)
				return true;
		}
		return false;
	}

	/**
	 * ����û������"ȷ��"��ť,���ȡ����ָ����Ϣ
	 */
	private void calculateResults() {
		Component[] comps = getUITabbedPane().getComponents();
		for (int i = 0; i < comps.length; i++) {
			UIListToList ltl = (UIListToList) comps[i];
			AssignableInfo ainfo = (AssignableInfo) _hashListToInfo.get(ltl);

			Object[] rightData = ltl.getRightData();
			int iAssigned = rightData == null ? 0 : rightData.length;
			if (iAssigned == 0) {
				//return false;
				//����
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
			} else {
				//������
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
				for (int j = 0; j < iAssigned; j++) {
					ainfo.getAssignedOperatorPKs().addElement(((OrganizeUnit) rightData[j]).getPk());
					ainfo.getOuAssignedUsers().addElement((OrganizeUnit) rightData[j]);
				}
			}
		}
	}
} //  @jve:decl-index=0:visual-constraint="10,10"