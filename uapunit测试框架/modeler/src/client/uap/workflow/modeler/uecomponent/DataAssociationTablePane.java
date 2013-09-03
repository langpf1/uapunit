package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.bpmn2.model.DataAssociation;
import uap.workflow.modeler.editors.ExpressionEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;

import nc.uap.ws.gen.util.StringUtil;

public class DataAssociationTablePane extends AbstractTablePane {

	private static final long serialVersionUID = -5895870735831042686L;
	
	public DataAssociationTablePane(DataAssociationTableModel model) {
		super(model,model.getColumnWidth());
	}


	protected void initCellEditor(){
		getTable().getColumnModel().getColumn(1).setCellEditor(new CellEditorAdapter( new ExpressionEditor()));
	}
	
	@Override
	protected void doNew() {
		((DataAssociationTableModel)getTable().getModel()).addRow(1);
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
			if (StringUtil.isEmptyOrNull(rowVector.get(0).toString()) ||
				StringUtil.isEmptyOrNull(rowVector.get(1).toString()) || 
				StringUtil.isEmptyOrNull(rowVector.get(2).toString())){
				throw new RuntimeException("单元内容不能为空");
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
			DataAssociation assc = new DataAssociation();
			assc.setSource(rowVector.get(0).toString());
			assc.setSourceExpression(rowVector.get(1).toString());
			assc.setTarget(rowVector.get(2).toString());
			list.add(assc);
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

		List<DataAssociation> list = (List<DataAssociation>) intializeData;
		for (Object obj : list) {
			Vector<Object> rowVector = new Vector<Object>();
			DataAssociation assc = (DataAssociation) obj;
			rowVector.add(assc.getSource());
			rowVector.add(assc.getSourceExpression());
			rowVector.add(assc.getTarget());
			((AbstractBpmnTableModel) getTable().getModel()).addRow(rowVector);
		}
	}

}
