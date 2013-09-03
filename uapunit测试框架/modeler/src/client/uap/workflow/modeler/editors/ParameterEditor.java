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
import uap.workflow.modeler.uecomponent.ParameterEditorDlg;


public class ParameterEditor extends AbstractDailogPropertyEditor {

	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new ParameterEditorDlg(parent,ParameterEditor.this);
	}
	
	protected void doButtonClick(){
		((ParameterEditorDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
