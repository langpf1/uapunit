package uap.workflow.ui.taskhandling.editors;

import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class TaskHandlingPropertyModel extends AbstractBpmnTableModel {

	public TaskHandlingPropertyModel(){
		super();
	}
	
	@Override
	public Class getColumnClass(int col){
		return String.class;
	}
	
	public int[] getColumnWidth(){
		return new int[]{
				BpmnModelerConstants.COLUMN_12WIDTH};
	}
	


	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("任务处理方式");
	}

}
