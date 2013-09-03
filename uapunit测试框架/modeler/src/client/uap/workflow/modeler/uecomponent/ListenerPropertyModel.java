package uap.workflow.modeler.uecomponent;


import java.util.ArrayList;
import java.util.List;

import uap.workflow.bpmn2.model.FieldExtension;
import uap.workflow.modeler.utils.BpmnModelerConstants;
import uap.workflow.modeler.utils.ListenerServiceTypeEnum;

public class ListenerPropertyModel extends AbstractBpmnTableModel {

	public ListenerPropertyModel(){
		super();
	}
	
	@Override
	public Class<?> getColumnClass(int col){
		if (col == 4)
			return (new ArrayList<FieldExtension>()).getClass();
		else
			return String.class;
	}
	
	public int[] getColumnWidth(){
		return new int[]{
			BpmnModelerConstants.COLUMN_6WIDTH,
			BpmnModelerConstants.COLUMN_6WIDTH,
			BpmnModelerConstants.COLUMN_12WIDTH,
			BpmnModelerConstants.COLUMN_4WIDTH,
			BpmnModelerConstants.COLUMN_12WIDTH,
			0
			};
	}
	
	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("Event Type");
		m_columnIdentifiers.add("Implementation Type");
		m_columnIdentifiers.add("Implementation");
		m_columnIdentifiers.add("Method");
		m_columnIdentifiers.add("Fields");
		m_columnIdentifiers.add("object");
	}

	@Override 
    public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex > 4)
			return false;
		Object obj = getValueAt(rowIndex, 1);
		if (obj.equals(ListenerServiceTypeEnum.Standard.toString())){	//非标准类型的都需要编辑
			if (columnIndex > 2)
				return false;
		}
        return true;
    }
}
