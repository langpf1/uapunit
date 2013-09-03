package uap.workflow.modeler.editors;

import java.awt.Container;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.DataAssociationEditorDlg;


public class DataAssociationEditor extends AbstractDailogPropertyEditor {

	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new DataAssociationEditorDlg(parent,DataAssociationEditor.this);
	}
	
	protected void doButtonClick(){
		((DataAssociationEditorDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
