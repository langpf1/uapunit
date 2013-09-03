package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import nc.bs.framework.common.NCLocator;
import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.UserTask;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.engine.actsgy.IActorService;
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
import uap.workflow.vo.WorkflownoteVO;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;

@SuppressWarnings("serial")
public class AssignDlg extends UIDialog {
	private WorkflownoteVO worknoteVO = null;
	private JPanel branchPanel;
	private ApproveFlowDesignatePanel designateFlowPanel;
	private JPanel buttonPanel;
	private JLabel showBranch;
	private JButton ok;
	private JButton cancel;
	private HashMap<String, List<String>> activeUserNames;
	private BranchTableModel branchModel;
	private JTable branchTable;
	@SuppressWarnings("deprecation")
	public  AssignDlg (WorkflownoteVO worknoteVO) {
		this.setTitle("指派");
		this.setResizable(true);
		this.worknoteVO=worknoteVO;
		showBranch = new JLabel("分支选择");
		branchPanel = new JPanel(new BorderLayout());
		branchPanel.add(showBranch,BorderLayout.NORTH);
		String processDefPK=null;
		processDefPK=worknoteVO.getTaskInstanceVO().getPk_process_def();
		designateFlowPanel=new ApproveFlowDesignatePanel(worknoteVO.getTaskInstanceVO().getPk_form_ins_version(),processDefPK);
		branchPanel.add(designateFlowPanel,BorderLayout.CENTER);
		activeUserNames = new HashMap<String, List<String>>();	
		DefaultTableModel headline = new DefaultTableModel(new String[] { "是否并行","活动", "活动执行者"}, 0);
		JTable taskProcess = new JTable(headline);
		headline.addRow(new String[]{"是否并行","活动", "活动执行者"});
		TableColumn  column=null;
		for(int i=0;i<taskProcess.getColumnCount();i++)
		{
			if(i==taskProcess.getColumnCount()-1)
			{
				 column=taskProcess.getColumnModel().getColumn(i);
				 column.setPreferredWidth(800);
			}   
			else
			{
			  column=taskProcess.getColumnModel().getColumn(i);
			  column.setPreferredWidth(200);
			  column.setMaxWidth(200);
			}        
		}
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		taskProcess.setDefaultRenderer(Object.class, tcr);
		
		branchModel = new BranchTableModel();
		branchTable = new JTable(branchModel);
		branchTable.setDefaultRenderer(Object.class, tcr);
		branchTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		for(int i=0;i<branchTable.getColumnCount();i++)
		{
			if(i==branchTable.getColumnCount()-1)
			{
				 column=branchTable.getColumnModel().getColumn(i);
				 column.setPreferredWidth(800);
			}   
			else
			{
			  column=branchTable.getColumnModel().getColumn(i);
			  column.setPreferredWidth(200);
			  column.setMaxWidth(200);
			}        
		}
		JPanel tablePanel=new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(taskProcess,BorderLayout.NORTH);
		tablePanel.add(branchTable,BorderLayout.CENTER);	
		JSplitPane all=new JSplitPane();
		all.setOrientation(JSplitPane.VERTICAL_SPLIT);
		all.setLeftComponent(branchPanel);
		all.setRightComponent(tablePanel);
		all.setResizeWeight(1);
		all.setDividerSize(6);
		all.setBorder(null);
		all.setContinuousLayout(true);
		all.setOneTouchExpandable(true);
		all.setMinimumSize(new Dimension(0, 0));
		all.setDividerLocation(600);
		buttonPanel=new JPanel(new FlowLayout());
		ok=new JButton(" 指      派 ");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OnOK();
			}
		});
		cancel=new JButton(" 取     消 ");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
				fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
				return;
			}
		});
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		
		JSplitPane total=new JSplitPane();
		total.setOrientation(JSplitPane.VERTICAL_SPLIT);
		total.setLeftComponent(all);
		total.setRightComponent(buttonPanel);
		total.setResizeWeight(1);
		total.setDividerSize(6);
		total.setBorder(null);
		total.setContinuousLayout(true);
		//total.setOneTouchExpandable(true);
		total.enable(false);
		total.setMinimumSize(new Dimension(0, 0));
		total.setDividerLocation(730);		
		this.add(total);
		//this.setVisible(true);
	}
		
	public HashMap<String, List<String>> getActiveUserNames() {
		return activeUserNames;
	}
    
	public void OnOK()
	{
		worknoteVO.setAssignInfoMap(getActiveUserNames());
		HashMap<String,Boolean> activeUsermodel=new HashMap<String,Boolean>();
		for(int i=0;i<branchModel.getRowCount();i++)
			activeUsermodel.put(branchModel.getRow(i).getId(), branchModel.getRow(i).isSelected());	
		worknoteVO.setAssignModeInfoMap(activeUsermodel);

		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
		return;
	}
	
	public class ApproveFlowDesignatePanel extends UIPanel implements IGraphListenerClaim{
		String m_elementId=null;
		String m_activityName=null;
		
		String m_strBillID;

		String m_strProcessDef;

		private UILabel descriptionlabel;
		private ScrollPane m_tabbedPane;
		
		public ApproveFlowDesignatePanel(String m_strBillID,String m_strProcessDef) {

			this.m_strBillID = m_strBillID;
			this.m_strProcessDef=m_strProcessDef;
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
			String procInstance=null;
			ProcessInstanceVO[] vos = ProcessInstanceUtil.getProcessInstanceVOs(m_strBillID);
			if(vos!=null)
			procInstance = vos[0].getPk_proins();
			IWorkflowInstanceQry actInsQry = NCLocator.getInstance().lookup(IWorkflowInstanceQry.class);
			ActivityInstanceVO[] actInsVos = actInsQry.getActInsVoByProInsPk(procInstance);
			IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
			ProcessDefinitionVO proDef = proDefQry.getProDefVoByPk(m_strProcessDef);
			Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDef.getProcessStr()));
			BpmnGraphComponent graphComponent = new BpmnGraphComponent(new mxGraph());
			graphComponent.setGridVisible(false);
			graphComponent.getGraph().setCellsMovable(false);
			Bpmn2MemoryModel.addToGraphModel(graphComponent, model);
			mxCell[] cells = model.getClipboard().toArray(new mxCell[0]);
	        if(actInsVos!=null)
	        {
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
	        }
	        else
			{
				graphComponent.getGraph().setSelectionCell(cells[0]);
				graphComponent.getGraph().setCellStyles("fillColor", "red");
			}
			graphComponent.getGraph().getSelectionModel().addListener("change", new GraphListenerAdaptor(this));
			m_tabbedPane = new ScrollPane();
			m_tabbedPane.add(graphComponent);
			return m_tabbedPane;	
		}
		@ListenerType(eventType = mxEvent.CHANGE)
		public void invokeChange(Object sender, mxEventObject evt) {
			((mxGraphSelectionModel)sender).setSingleSelection(true);
			String[] users=null;
			Object addedCells = evt.getProperty("removed");         //重新选中的节点
			if(addedCells != null && ((List)addedCells).size() > 0)
			{
				Object cell=((List)addedCells).get(0);
				if(cell != null){
					BaseElement userTask = (BaseElement)((mxCell)cell).getValue(); 
					if (userTask instanceof UserTask){
						m_activityName=((UserTask)userTask).getName();
						m_elementId = userTask.getId();
						users=NCLocator.getInstance().lookup(IActorService.class).getActivityActors(new BizObjectImpl(), m_elementId, m_strProcessDef);
						selectPeopleDlg selectpeople;
						if(users!=null&&users.length!=0)
						{
							selectpeople=new  selectPeopleDlg(users);
							selectpeople.setBounds(500, 300, 400, 320);
							selectpeople.setVisible(true);
							getActiveUserNames().put(m_elementId, selectpeople.getActiveUserNames());   
							String peopleSelect="";
							if(selectpeople!=null&&selectpeople.getActiveUserNames()!=null&&selectpeople.getActiveUserNames().size()>0)
							{
							for(int i=0;i<selectpeople.getActiveUserNames().size();i++)
							 {
								peopleSelect=peopleSelect+"  "+selectpeople.getActiveUserNames().get(i);
							 }
							BranchSelection temp = new BranchSelection(m_elementId,peopleSelect, true);
							branchModel.addRow(temp);
							}
						}
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

	
	
	class BranchSelection {
		private String id;
		private String name;
		private boolean selected = false;

		public BranchSelection(String id, String name, boolean selected) {
			this.id = id;
			this.name = name;
			this.selected = selected;
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

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

	}
	
	class BranchTableModel extends AbstractTableModel {

		List<BranchSelection> rowList = new ArrayList<BranchSelection>();

		String[] columnNames = new String[] { "分支选择", "第二列","第三列" };

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 0;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public int getRowCount() {
			return rowList.size();
		}

		public void addRow(BranchSelection row) {
			rowList.add(row);
		}

		public BranchSelection getRow(int rowIdx) {
			return rowList.get(rowIdx);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			BranchSelection row = getRow(rowIndex);

			switch (columnIndex) {
			case 0:
				return row.isSelected();
			case 1:
				return row.getId();
			case 2:
				return row.getName();
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			BranchSelection row = getRow(rowIndex);

			switch (columnIndex) {
			case 0:
				row.setSelected((Boolean) value);
				break;
			case 1:
				row.setId((String)value);
				break;
			case 2:
				row.setName((String) value);

			default:
				break;
			}
		}
	}
	
}

