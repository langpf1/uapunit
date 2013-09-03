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
 * 单据在提交启动审批流或审批时使用的指派对话框
 *
 * @author 雷军 created on 2005-1-5
 * @modifier 雷军 2006-7-15 明确了指派的语义，如果取消指派，则停止送审
 * @modifier leijun 2008-4 如果没有为任何活动指派，也停止送审
 * @modifier leijun 2008-11 指派对话框的内容随当前审批意见（批准或不批准）变化
 */
public class DispatchDialog extends UIDialog implements ActionListener {
	private UITabbedPane UITabbedPane = null;

	private UIPanel mainPane = null; //  @jve:decl-index=0:visual-constraint="10,10"

	private UIPanel btnPane = null;

	private UIButton btnOK = null;

	private UIButton btnCancel = null;

	//映射UIListToList到AssignableInfo对象
	private Hashtable _hashListToInfo = new Hashtable();

	/*流程上下文VO */
	private WorkflownoteVO m_worknoteVO = null;

	private Vector _assignableInfos = null;

	/**
	 * 流程图的缓存
	 */
	private Hashtable _hashGraphCache = new Hashtable();

	private UIButton _btnHistoryGraph;

	public DispatchDialog(Container parent) {
		super(parent);

		initialize();
	}

	/**
	 * 初始化指派对话框
	 * @param wfVo
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo) {
		initByWorknoteVO(wfVo, AssignableInfo.CRITERION_NOTGIVEN);
	}

	/**
	 * 指派对话框的内容随当前审批意见（批准或不批准）变化
	 * @param wfVo
	 * @param strCriterion
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo, String strCriterion) {
		this.m_worknoteVO = wfVo;
		_assignableInfos = wfVo.getAssignableInfos();

		//只有提交时指派对话框才需要"流程图"按钮
		if (PfUtilBaseTools.isSaveAction(m_worknoteVO.getActiontype(), m_worknoteVO.getPk_billtype()))
			getBtnPane().add(getBtnHistoryGraph(), null);

		//初始化指派的各个页签
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
			//UITabbedPane.addTab("雷军", null, getUIListToList(), null);
		}
		return UITabbedPane;
	}

	/**
	 * 新产生一个ListToList控件,并进行初始化
	 * <li>1.填充右边列表
	 * <li>2.填充左边列表
	 * <li>3.计算差
	 * @param ainfo 传递进来的可指派对象
	 * @return
	 */
	private UIListToList newUIListToList(AssignableInfo ainfo) {
		// 根据传递进来的指派信息,初始化界面
		/*********************
		 * 1.填充右边列表
		 *********************/
		Vector<OrganizeUnit> ouAssignedUsers = ainfo.getOuAssignedUsers();
		UIListToList ltl = new UIListToList();
		ListItemSearcher lis = new ListItemSearcher(ltl.getLstLeft());
		lis.setMatcher(new PatternMather());
		lis.setSearchHint(NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000139"))/* @res "输入名称搜索:" */;
		_hashListToInfo.put(ltl, ainfo);

		int numRightUsers = ouAssignedUsers.size();
		ltl.setRightData(ouAssignedUsers.toArray(new OrganizeUnit[0]));

		/****************
		 * 2.计算差后，再填充左边列表
		 ****************/
		Vector<OrganizeUnit> vecLeftUsers = new Vector<OrganizeUnit>();
		Iterator iter = ainfo.getOuUsers().iterator();
		while (iter.hasNext()) {
			OrganizeUnit element = (OrganizeUnit) iter.next();
			boolean hasAssigned = false; //判断左边待指派用户是否在右边存在
			for (int i = 0; i < numRightUsers; i++) {
				if (ouAssignedUsers.get(i).getPk().equals(element.getPk())) {
					hasAssigned = true;
					break;
				}
			}
			if (!hasAssigned)
				vecLeftUsers.add(element);
		}

		//左边的待选用户排序
		if (vecLeftUsers.size() > 0) {
			Collections.sort(vecLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//按照字符串升序
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
			//btnOK.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
			btnOK
					.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000253")/*@res "指派"*/);
		}
		return btnOK;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
		}
		return btnCancel;
	}

	private UIButton getBtnHistoryGraph() {
		if (_btnHistoryGraph == null) {
			_btnHistoryGraph = new UIButton();
			_btnHistoryGraph.setName("historyGraph");
			_btnHistoryGraph.setText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000252")/*@res "流程>>"*/);
		}
		return _btnHistoryGraph;
	}

	private void initialize() {
		this.setContentPane(getJPanel());
		this
				.setTitle(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "指派下一个审批环节的参与者"*/);
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
						"UPPpfworkflow-000227")/*@res "提示"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000492")/*@res "请为活动指派参与者！"*/);
				return;
			}
			//计算本次指派的结果
			calculateResults();
			this.closeOK();

		} else if (e.getSource() == getBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getBtnHistoryGraph()) {
			showGraph();
		}
	}

	/**
	 * 指派对话框中显示待启动的审批流程图
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
						"UPPpfworkflow-000237")/*@res "错误"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000494")/*查询单据的流程图出现异常："*/
						+ e.getMessage());
				return;
			}
			centerComp = constructGraphTab(wp);

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
	 * 如果用户点击了"确定"按钮,则获取本次指派信息
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
				//先清
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
			} else {
				//先清后加
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