package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.bpmn2.model.CustomProperty;
import uap.workflow.bpmn2.model.EventListener;
import uap.workflow.bpmn2.model.FieldExtension;
import uap.workflow.modeler.editors.ExpressionEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;

import nc.uap.ws.gen.util.StringUtil;
import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.table.NCTableModel;

public class ParameterTablePane extends AbstractTablePane {

	private static final long serialVersionUID = -5895870735831042686L;
	
	public ParameterTablePane(ParameterTableModel model) {
		super(model,model.getColumnWidth());
	}


	protected void initCellEditor(){
		getTable().getColumnModel().getColumn(2).setCellEditor(new CellEditorAdapter( new ExpressionEditor()));
	}
	
	@Override
	protected void doNew() {
		((ParameterTableModel)getTable().getModel()).addRow(1);
	}

	@Override
	protected void doEdit() {

	}
	@Override
	protected boolean doAfterOKButtonClicked() {
		super.doAfterOKButtonClicked();
		//检查正确性
		Vector  vector= ((AbstractBpmnTableModel)getTable().getModel()).getDataVector();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			if (StringUtil.isEmptyOrNull(rowVector.get(2).toString()) && StringUtil.isEmptyOrNull(rowVector.get(1).toString())){
				throw new RuntimeException("第"+start+"行名称和表达式不能都为空");
			}
		}
		return true;
	}

	@Override
	public Object assemberData() {
		Vector  vector= ((AbstractBpmnTableModel)getTable().getModel()).getDataVector();
		List<Object> list =new ArrayList<Object>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			FieldExtension field = new FieldExtension();
			field.setFieldType(rowVector.get(0).toString());
			field.setFieldName(rowVector.get(1).toString());
			field.setExpression(rowVector.get(2).toString());
			list.add(field);
		}		
		return list;	

	}

	@SuppressWarnings("unchecked")
	@Override
	public void unassemberData(Object intializeData) {
		 if (intializeData == null)
			return;
		if (intializeData instanceof String)
			return ;
		((AbstractBpmnTableModel) getTable().getModel()).getDataVector().clear();

		List<FieldExtension> list = (List<FieldExtension>) intializeData;
		for (Object obj : list) {
			Vector<Object> rowVector = new Vector<Object>();
			FieldExtension field = (FieldExtension) obj;
			rowVector.add(field.getFieldType());
			rowVector.add(field.getFieldName());
			rowVector.add(field.getExpression());
			((AbstractBpmnTableModel) getTable().getModel()).addRow(rowVector);
		}
	}

}
