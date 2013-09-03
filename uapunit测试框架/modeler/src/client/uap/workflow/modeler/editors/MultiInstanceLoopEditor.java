package uap.workflow.modeler.editors;

import java.awt.Container;

import uap.workflow.bpmn2.model.MultiInstanceLoopCharacteristics;
import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.MultiInstanceDlg;



public class MultiInstanceLoopEditor extends AbstractDailogPropertyEditor {
	
	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new MultiInstanceDlg(parent,MultiInstanceLoopEditor.this);
	}
	
	protected void doButtonClick(){
		((MultiInstanceDlg)dlg).SetPropertys( getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
	
}
