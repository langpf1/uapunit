package uap.workflow.modeler.editors;

import java.awt.Container;
import java.util.List;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.FormSettingDlg;


public class FormPropertyEditor extends  AbstractDailogPropertyEditor{
	
	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new FormSettingDlg(parent,FormPropertyEditor.this);
	}
	
	protected void doButtonClick(){
		((FormSettingDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
