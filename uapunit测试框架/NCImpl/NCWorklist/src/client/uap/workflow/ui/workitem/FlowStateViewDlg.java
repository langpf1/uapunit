package uap.workflow.ui.workitem;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.StringReader;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIDialog;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
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

public class FlowStateViewDlg extends UIDialog {
	
	private static final long serialVersionUID = 1L;
	private JTable taskProcess;
	private ScrollPane myGraph;

	@SuppressWarnings("deprecation")
	public FlowStateViewDlg(Container parent, String strBillType, String strBillID, int iWorkflowtype) {
		if(strBillID!=null)
	 {
		ProcessInstanceVO[] vos = ProcessInstanceUtil.getProcessInstanceVOs(strBillID);
		if(vos!=null&&vos[0]!=null&&vos[0].getPk_prodef()!=null)
	  {
		String proDefPk = vos[0].getPk_prodef();
		String procInstance = vos[0].getPk_proins();
		final List<TaskInstanceVO> task = TaskUtil.getTaskByProcessInstancePk(procInstance);
		
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance().lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO[] actInsVos = actInsQry.getActInsVoByProInsPk(procInstance);
		
		setBounds(230, 100, 950, 770); // 设置窗口大小及位置
		DefaultTableModel taskTableModel = new DefaultTableModel(new String[] { "pk_creater","Pk_owner", "Activity_id", "Activity_name", "Pk_executor", "Opinion", "Begindate", "Signdate", "Finishdate", "Dutedate",
				"Standingdate" }, 0);
		taskProcess = new JTable(taskTableModel);
		taskProcess.setAutoResizeMode(0);
		if(task!=null)
		for(int i=0;i<task.size();i++)
		{
			UFDateTime early=task.get(i).getTs(); 
			int position=i;
			for(int j=i+1;j<task.size();j++)
			{
				if(task.get(j).getTs().before(early))
				{
					early=task.get(j).getTs();
					position=j;
				}
			}
			TaskInstanceVO temp=task.get(position);
			task.set(position, task.get(i));
			task.set(i, temp);
			taskTableModel.addRow(new Object[] {temp.getPk_creater(), temp.getPk_owner(), temp.getActivity_id(), temp.getActivity_name(), temp.getPk_executer(), temp.getOpinion(), temp.getBegindate(), temp.getSigndate(),
					temp.getFinishdate(), temp.getDutedate(), temp.getStandingdate() });
		}
		int h = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		int v = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
		JScrollPane taskTableScroll = new JScrollPane(taskProcess, v, h);
		taskTableScroll.setMinimumSize(new Dimension(0, 0));
		myGraph = new ScrollPane();
		myGraph.setMinimumSize(new Dimension(0, 0));
		
		IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO proDef = proDefQry.getProDefVoByPk(proDefPk);
		if(proDef!=null&&proDef.getProcessStr()!=null)
		{
		 Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDef.getProcessStr()));
		 model.getProcesses().get(0).setProcessDefinitionPk(proDefPk);
		 BpmnGraphComponent graphComponent = new BpmnGraphComponent(new mxGraph());
		 Bpmn2MemoryModel.addToGraphModel(graphComponent, model);
		 mxCell[] cells = model.getClipboard().toArray(new mxCell[0]);
		 for (mxCell cell : cells) {
			 for (int j = 0; j < actInsVos.length; j++) {
				//	TaskInstanceVO temp2 = task.get(j);
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
		graphComponent.getGraph().setCellsMovable(false);
		myGraph.add(graphComponent);
		}
		JSplitPane all = new JSplitPane(JSplitPane.VERTICAL_SPLIT, taskTableScroll, myGraph);
		all.setDividerLocation(200);
		all.setResizeWeight(1);
		all.setDividerSize(6);
		all.setBorder(null);
		all.setOneTouchExpandable(true);
		all.setContinuousLayout(true);
		this.getContentPane().add(all);
		all.setDividerLocation(155);
	  }
		else
		{
			setBounds(230, 100, 950, 770); // 设置窗口大小及位置
			DefaultTableModel taskTableModel = new DefaultTableModel(new String[] { "pk_creater","Pk_owner", "Activity_id", "Activity_name", "Pk_executer", "Opinion", "Begindate", "Signdate", "Finishdate", "Dutedate",
					"Standingdate" }, 0);
			taskProcess = new JTable(taskTableModel);
			int h = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
			int v = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
			JScrollPane taskTableScroll = new JScrollPane(taskProcess, v, h);
			myGraph = new ScrollPane();
			JSplitPane all = new JSplitPane(JSplitPane.VERTICAL_SPLIT, taskTableScroll, myGraph);
			all.setDividerLocation(200);
			all.setResizeWeight(1);
			all.setDividerSize(6);
			all.setBorder(null);
			all.setOneTouchExpandable(true);
			all.setContinuousLayout(true);
			this.getContentPane().add(all);
			all.setDividerLocation(155);
		}
	 }
	}
}
