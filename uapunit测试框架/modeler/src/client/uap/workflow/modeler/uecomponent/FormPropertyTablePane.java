package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.bpmn2.model.FormProperty;


public class FormPropertyTablePane extends AbstractTablePane {
	
	public FormPropertyTablePane(FormPropertyModel model) {
		super(model,model.getColumnWidth());
	}
	
	protected void initCellEditor(){
		//Form Values
//		getTable().getColumnModel().getColumn(11).setCellEditor();
	}
	
	
	
	@Override
	protected void doEdit() {
	}

	@Override
	public Object assemberData() {
		Vector vector = ((AbstractBpmnTableModel)getTable().getModel()).getDataVector();
		List<FormProperty> list = new ArrayList<FormProperty>();
		for (int start = 0, end = ((AbstractBpmnTableModel)getTable().getModel()).getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			FormProperty formPorp = new FormProperty();
			formPorp.setId(rowVector.get(0).toString());
			formPorp.setName(rowVector.get(1).toString());
			formPorp.setType(rowVector.get(2).toString());
			formPorp.setValue(rowVector.get(3).toString());
			formPorp.setExpression(rowVector.get(4).toString());
			formPorp.setVariable(rowVector.get(5).toString());
			formPorp.setDefaultExpression(rowVector.get(6).toString());
			formPorp.setDatePattern(rowVector.get(7).toString());
			formPorp.setRequired(rowVector.get(8) == null ? false : Boolean.parseBoolean(rowVector.get(8).toString()));
			formPorp.setReadable(rowVector.get(9) == null ? false : Boolean.parseBoolean(rowVector.get(9).toString()));
			formPorp.setWriteable(rowVector.get(10) == null ? false : Boolean.parseBoolean(rowVector.get(10).toString()));
			formPorp.setFormValues(null);
			list.add(formPorp);
		}
		return list;
	}

	@Override
	public void unassemberData(Object intializeData) {
		((AbstractBpmnTableModel)getTable().getModel()).getDataVector().clear();
		if(intializeData==null)
			return;
		List list =(List) intializeData;
		for (Object obj : list) {
			Vector rowVector = new Vector();
			FormProperty formProp = (FormProperty) obj;
			rowVector.add(formProp.getId());
			rowVector.add(formProp.getName());
			rowVector.add(formProp.getType());
			rowVector.add(formProp.getValue());
			rowVector.add(formProp.getExpression());
			rowVector.add(formProp.getVariable());
			rowVector.add(formProp.getDefaultExpression());
			rowVector.add(formProp.getDatePattern());
			rowVector.add(formProp.getRequired());
			rowVector.add(formProp.getReadable());
			rowVector.add(formProp.getWriteable());
			rowVector.add(formProp.getFormValues());
			((AbstractBpmnTableModel)getTable().getModel()).getDataVector().add(rowVector);
		}
		
	}
}
