package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.UserTask;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.bpmn.graph.itf.GraphListenerAdaptor;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.graph.itf.ListenerType;
import uap.workflow.modeler.uecomponent.UfGraphCell;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.ui.ls.MessageBox;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.ui.wfengine.flowchart.graph.ActivityCell;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uap.wfmonitor.ProcessRouteRes;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.activity.GenericActivityEx;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.pub.WfTaskOrInstanceStatus;

/**
 * 驳回 panel
 * 
 * @author dingxm
 * 
 */
public class ApproveFlowRejectPanel extends UIPanel implements ActionListener, IGraphListenerClaim{

	private ScrollPane m_tabbedPane;
	
	String m_elementId;
	
	String m_strBillID;
	
	String m_strBillType;

	String m_strProcessInstance;
	
	String m_strProcessDef;

	int m_iWorkflowtype;

	protected Activity selectedActivity;

	private UIPopupMenu m_popMenuFlow;

	private UIMenuItem rejectMenuItem;

	private Activity rejectToActivity;

	private UILabel descriptionlabel;

	public ApproveFlowRejectPanel(String m_strBillID, String m_strBillType,
			String m_strProcessInstance, String m_strProcessDef,int m_iWorkflowtype) {
		
		this.m_strBillID = m_strBillID;
		this.m_strBillType = m_strBillType;
		this.m_strProcessInstance = m_strProcessInstance;
		this.m_strProcessDef=m_strProcessDef;
		this.m_iWorkflowtype = m_iWorkflowtype;
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		add(constructGraph(), BorderLayout.CENTER);
		add(getDescriptionLabel(), BorderLayout.SOUTH);
	}

	private UILabel getDescriptionLabel() {
		if (descriptionlabel == null) {
			descriptionlabel = new UILabel();
		}
		return descriptionlabel;
	}

	private ScrollPane constructGraph() {
		ProcessInstanceVO[] vos = ProcessInstanceUtil.getProcessInstanceVOs(m_strBillID);
		String procInstance = vos[0].getPk_proins();
		IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO proDef = proDefQry.getProDefVoByPk(m_strProcessDef);
		Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDef.getProcessStr()));
		BpmnGraphComponent graphComponent = new BpmnGraphComponent(new mxGraph());
		graphComponent.setGridVisible(false);
		graphComponent.getGraph().setCellsMovable(false);
		Bpmn2MemoryModel.addToGraphModel(graphComponent, model);
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance().lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO[] actInsVos = actInsQry.getActInsVoByProInsPk(procInstance);
		mxCell[] cells = model.getClipboard().toArray(new mxCell[0]);
		for (mxCell cell : cells) {
			 for (int j = 0; j < actInsVos.length; j++) {
					ActivityInstanceVO temp2=actInsVos[j];
					 if(temp2.getPort_id().equals(((BaseElement)cell.getValue()).getId())&&(temp2.getIspass().booleanValue())){
						graphComponent.getGraph().setSelectionCell(cell);
						graphComponent.getGraph().setCellStyles("fillColor", "gray");
						graphComponent.getGraph().setCellStyles("gradientColor", "gray");
					 }
					 if (temp2.getPort_id().equals(((BaseElement)cell.getValue()).getId())&&(!temp2.getIspass().booleanValue())){
							graphComponent.getGraph().setSelectionCell(cell);
							graphComponent.getGraph().setCellStyles("fillColor", "red");
							graphComponent.getGraph().setCellStyles("gradientColor", "red");
						}
				}
			
		}
		// install default listeners
		graphComponent.getGraph().getSelectionModel().addListener("change", new GraphListenerAdaptor(this));

		m_tabbedPane = new ScrollPane();
		m_tabbedPane.add(graphComponent);
		return m_tabbedPane;
	}

	/**
	 * 构造流程图页签
	 * 
	 * @param lhm
	 * @return
	 */
	private void constructGraphTab(ProcessRouteRes processRoute,
			UITabbedPane tabPane, XpdlPackage pkg) {
		String def_xpdl = null;
		ProcessRouteRes currentRoute = processRoute;
		if (currentRoute.getXpdlString() != null)
			def_xpdl = currentRoute.getXpdlString().toString();

		WorkflowProcess wp = null;
		try {
			// 前台解析XML串为对象
			wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
		} catch (XPDLParserException e) {
			Logger.error(e.getMessage(), e);
			return;
		}
		wp.setPackage(pkg);

		// 初始化Graph
		final FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		// 启用工具提示
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		// auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);
		// auditChart.setBorder(BorderFactory.createEtchedBorder());
		final ActivityInstance[] allActInstances = currentRoute.getActivityInstance();
		String[] startedActivityDefIds = new String[allActInstances.length];

		// 当前正运行的活动
		HashSet hsRunningActs = new HashSet();
		for (int i = 0; i < allActInstances.length; i++) {
			startedActivityDefIds[i] = allActInstances[i].getActivityID();
			if (allActInstances[i].getStatus() == WfTaskOrInstanceStatus.Started
					.getIntValue())
				hsRunningActs.add(startedActivityDefIds[i]);
		}
		auditChart.setActivityRouteHighView(hsRunningActs,
				startedActivityDefIds, currentRoute.getActivityRelations(),
				Color.RED, Color.BLUE);

		UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
		JViewport vport = graphScrollPane.getViewport();
		vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

		HashMap<String, String> nameMap = new HashMap<String, String>();
		Object[] cells = auditChart.getRoots();
		for (Object c : cells) {
			if (c instanceof ActivityCell) {
				Object o = ((ActivityCell) c).getUserObject();
				if (o instanceof Activity) {
					nameMap.put(((Activity) o).getId(),
							((Activity) o).getName());
				}
			}
		}

		auditChart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					Object[] cells = auditChart.getRoots();
					for (Object cell : cells) {
						if ((cell instanceof ActivityCell)
								&& auditChart.getCellBounds(cell).contains(
										e.getPoint())) {
							Object o = ((ActivityCell) cell).getUserObject();
							if (o instanceof Activity) {//
								if(checkRejectValidity(allActInstances,((Activity) o).getId())){
									selectedActivity = ((Activity) o);
									Component srcComp = (Component) e.getSource();
									getFlowPopup()
											.show(srcComp, e.getX(), e.getY());
								}
								
							}
							break;
						}
					}
				}
				//

			}
		});

		String title = auditChart.getWorkflowProcess().getName() + " "
				+ processRoute.getProcessDefVersion();

		tabPane.addTab(title, graphScrollPane);

		ProcessRouteRes[] subRoutes = processRoute.getSubProcessRoute();
		for (int i = 0; i < (subRoutes == null ? 0 : subRoutes.length); i++) {
			currentRoute = subRoutes[i]; // 取子流程，继续循环
			constructGraphTab(currentRoute, tabPane, pkg);
		}
	}
	
	
	private boolean checkRejectValidity(ActivityInstance[] allActInstances,String actid){
		ArrayList<String> finishActId =new ArrayList<String>();
		for(int start =0,end =allActInstances==null?0:allActInstances.length;start<end;start++){
			if(allActInstances[start].getStatus()==WfTaskOrInstanceStatus.Finished.getIntValue()){
				finishActId.add(allActInstances[start].getActivityID());
			}
		}
		boolean isValidity =finishActId.contains(actid);
		if(!isValidity){
			MessageDialog.showWarningDlg(null, null, NCLangRes.getInstance().getStrByID("pfworkflow", "ApproveFlowRejectPanel-000000")/*不可驳回到当前环节的后续环节！*/);
		}		
		return isValidity;
	}

	private UIPopupMenu getFlowPopup() {
		// 驳回至该环节
		if (m_popMenuFlow == null) {
			m_popMenuFlow = new UIPopupMenu();
			rejectMenuItem = new UIMenuItem(NCLangRes.getInstance().getStrByID(
					"pfgraph", "ApproveFlowRejectPanel-000000")/* 驳回至该环节 */);
			rejectMenuItem.addActionListener(this);
			m_popMenuFlow.add(rejectMenuItem);

		}
		return m_popMenuFlow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rejectMenuItem) {
			if (!checkValidation(selectedActivity)) {
				return;
			}
			rejectToActivity = selectedActivity;
			getDescriptionLabel().setText(
					NCLangRes.getInstance().getStrByID("pfgraph",
							"ApproveFlowRejectPanel-000002", null,
							new String[] { rejectToActivity.getName() })/*
																		 * 驳回到：{0
																		 * }
																		 */);
		}
	}

	/**
	 * 不能驳回到加签环节,不可驳回给NC系统用户
	 * */
	private boolean checkValidation(Activity activity) {
		if(activity instanceof GenericActivityEx){
			if(((GenericActivityEx)activity).getOrganizeTransferObj().getOrgUnitPK().equals(INCSystemUserConst.NC_USER_PK)){
				MessageDialog.showHintDlg(ApproveFlowRejectPanel.this, null, NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000003")/*不可驳回到NC用户，请重新选择其它环节！*/);
				return false;
			}			
		}
		return true;
	}

	public Activity getRejectToActivity() {
		return rejectToActivity;
	}
	
	@ListenerType(eventType = mxEvent.CHANGE)
	public void invokeChange(Object sender, mxEventObject evt) {
		Object addedCells = evt.getProperty("removed");
		m_elementId = null;
		if(addedCells != null && ((List)addedCells).size() > 0)
		{
			Object cell=((List)addedCells).get(0);
			if(cell != null){
				BaseElement userTask = (BaseElement)((mxCell)cell).getValue(); 
				if (userTask instanceof UserTask){
					m_elementId = userTask.getId();
				}else{
					MessageBox.showMessageDialog("提示", "只能选中人工节点！");
				}
			}
		}
	}

	@Override
	public String[] getListenTargetType() {
		return new String[] {mxEvent.CHANGE};
	}
	
	public String getM_elementId() {
		return m_elementId;
	}

	public void setM_elementId(String id) {
		m_elementId = id;
	}
}
