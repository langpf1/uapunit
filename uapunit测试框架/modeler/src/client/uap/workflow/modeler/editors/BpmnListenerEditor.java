package uap.workflow.modeler.editors;

import java.awt.Container;

import uap.workflow.bpmn2.model.TaskListener;
import uap.workflow.modeler.uecomponent.BpmnListenerDlg;
import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;

public class BpmnListenerEditor extends AbstractDailogPropertyEditor {

	private Class<?> editedType = null; 
	
	public void setType(Class<?> type) {
		this.editedType = type;
	}

	public BpmnListenerEditor(){
	}

	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		boolean isTaskListenerType = editedType != null && editedType.equals(TaskListener.class); 
		BpmnListenerDlg dlg = new BpmnListenerDlg(parent,BpmnListenerEditor.this, isTaskListenerType);
		return dlg;
	}
	
	protected void doButtonClick(){
		((BpmnListenerDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
