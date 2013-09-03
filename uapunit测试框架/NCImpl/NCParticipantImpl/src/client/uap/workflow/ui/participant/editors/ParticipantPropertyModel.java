package uap.workflow.ui.participant.editors;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;

import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ParticipantPropertyModel extends AbstractBpmnTableModel {
	
	UIRefPane refPane = null;

	public ParticipantPropertyModel(){
		super();
	}
	
	@Override
	public Class getColumnClass(int col){
		return String.class;
	}
	
	public int[] getColumnWidth(){
		return new int[]{
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_12WIDTH,
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_6WIDTH};
	}

	@Override
	public void setValueAt(Object objValue, int row, int column)
	{
		if (m_dataVector == null)
			return;
		Vector rowVector = (Vector) m_dataVector.elementAt(row);
		rowVector.setElementAt(objValue, column);
		
		
		
		if (column == 1) {
			
			String pk = refPane.getRefPK();
			String code = refPane.getRefCode();
			String name = refPane.getRefName();
			rowVector.setElementAt(pk, 3);
			rowVector.setElementAt(code, 4);
		}
	}
	

	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("参与者类型");
		m_columnIdentifiers.add("参与者名称");
		m_columnIdentifiers.add("参与者过滤类型");
		m_columnIdentifiers.add("参与者Key");
		m_columnIdentifiers.add("参与者Code");
	}

	public UIRefPane getRefPane() {
		return refPane;
	}

	public void setRefPane(UIRefPane refPane) {
		this.refPane = refPane;
	}

}
