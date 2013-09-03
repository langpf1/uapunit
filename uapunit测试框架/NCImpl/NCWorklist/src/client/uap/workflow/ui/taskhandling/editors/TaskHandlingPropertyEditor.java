package uap.workflow.ui.taskhandling.editors;

import java.awt.Container;

import uap.workflow.modeler.editors.AbstractDailogPropertyEditor;


public class TaskHandlingPropertyEditor extends AbstractDailogPropertyEditor {

	protected TaskHandlingPropertyDlg initializeDlg(Container parent){
		return new TaskHandlingPropertyDlg(parent,TaskHandlingPropertyEditor.this);
	}
	
	protected void doButtonClick(){
		((TaskHandlingPropertyDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
