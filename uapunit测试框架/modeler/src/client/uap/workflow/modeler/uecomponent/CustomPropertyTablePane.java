package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.bpmn2.model.CustomProperty;
import uap.workflow.modeler.editors.ExpressionEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;

public class CustomPropertyTablePane extends AbstractTablePane {

	private static final long serialVersionUID = -5895870735831042686L;
	
	public CustomPropertyTablePane(CustomPropertyTableModel model) {
		super(model,model.getColumnWidth());
	}


	
	protected void initCellEditor(){
		getTable().getColumnModel().getColumn(1).setCellEditor(new CellEditorAdapter( new ExpressionEditor()));
	}
	
	@Override
	protected void doNew() {
		((CustomPropertyTableModel)getTable().getModel()).addRow(1);
	}

	@Override
	protected void doEdit() {

	}

	@Override
	public Object assemberData() {
		Vector  vector= ((AbstractBpmnTableModel)getTable().getModel()).getDataVector();
		List<Object> list =new ArrayList<Object>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			CustomProperty property = new CustomProperty();
			property.setName(rowVector.get(0).toString());
			property.setSimpleValue(rowVector.get(1).toString());
			list.add(property);
		}		
		return list;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unassemberData(Object intializeData) {
		((AbstractBpmnTableModel) getTable().getModel()).getDataVector().clear();
		if (intializeData == null)
			return;
		List<CustomProperty> list = (List<CustomProperty>) intializeData;
		for (Object obj : list) {
			Vector<Object> rowVector = new Vector<Object>();
			CustomProperty property = (CustomProperty) obj;
			rowVector.add(property.getName());
			rowVector.add(property.getSimpleValue());
			((AbstractBpmnTableModel) getTable().getModel()).addRow(rowVector);
		}
	}
}
