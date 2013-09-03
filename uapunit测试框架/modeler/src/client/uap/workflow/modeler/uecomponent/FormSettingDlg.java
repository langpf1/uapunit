package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.util.List;

import nc.ui.pub.beans.UIPanel;

public class FormSettingDlg extends BpmnPropertyEditingDlg {

	private FormPropertyTablePane formTablePane;
	
	
	public FormSettingDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		this.propertyEditor =propertyEditor;
	}
	
	public UIPanel getUIPanelMain(){
		if(formTablePane==null){
			formTablePane =new FormPropertyTablePane(new FormPropertyModel());
		}
		return formTablePane;
	}	

}
