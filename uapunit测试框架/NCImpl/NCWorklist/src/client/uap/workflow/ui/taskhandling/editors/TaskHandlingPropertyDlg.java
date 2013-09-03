package uap.workflow.ui.taskhandling.editors;

import java.awt.Container;
import java.beans.PropertyEditorSupport;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;

import nc.ui.pub.beans.UIPanel;

public class TaskHandlingPropertyDlg extends BpmnPropertyEditingDlg {
	private static final long serialVersionUID = 1098950625210213537L;
	private TaskHandlingPropertyTablePane listenerTablePane;
	
	public TaskHandlingPropertyDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		this.propertyEditor =propertyEditor;
	}
	
	public UIPanel getUIPanelMain(){
		if(listenerTablePane==null){
			listenerTablePane =new TaskHandlingPropertyTablePane(new TaskHandlingPropertyModel());
		}
		return listenerTablePane;
	}	
}
