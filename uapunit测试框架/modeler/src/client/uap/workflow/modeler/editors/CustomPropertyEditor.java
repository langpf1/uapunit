package uap.workflow.modeler.editors;

import java.awt.Component;
import java.awt.Container;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import uap.workflow.bpmn2.model.ExecutionListener;
import uap.workflow.modeler.uecomponent.BpmnListenerDlg;
import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.CustomPropertyEditorDlg;
import uap.workflow.modeler.uecomponent.ParameterEditorDlg;


public class CustomPropertyEditor extends AbstractDailogPropertyEditor {

	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new CustomPropertyEditorDlg(parent,CustomPropertyEditor.this);
	}
	
	protected void doButtonClick(){
		((CustomPropertyEditorDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
