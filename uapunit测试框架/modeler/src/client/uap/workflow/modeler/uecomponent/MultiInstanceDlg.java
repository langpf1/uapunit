package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;

import uap.workflow.bpmn2.model.MultiInstanceLoopCharacteristics;


import nc.ui.pub.beans.UIPanel;

public class MultiInstanceDlg extends BpmnPropertyEditingDlg {


	private static final long serialVersionUID = -3392147665500600668L;



	private MultiInstanceLoopCharacteristics multiInstanceFeature;
	
	
	
	private MultiInstancePanel multi_panel;
	
	public MultiInstanceDlg(PropertyEditorSupport editor){
		this(null,editor,null);
		
	}
	
	public MultiInstanceDlg(Container parent,PropertyEditorSupport editor) {
		this(parent,editor,null);
	}
	
	
	public MultiInstanceDlg(Container parent,PropertyEditorSupport editor,MultiInstanceLoopCharacteristics multiInstanceFeature) {
		super(parent);
		this.setSize(550,280);
		this.propertyEditor =editor;
		this.multiInstanceFeature =multiInstanceFeature;
	}
	
	
	
	public UIPanel getUIPanelMain(){
		if(multi_panel==null){
			multi_panel =new MultiInstancePanel(multiInstanceFeature);
		}
		return multi_panel;
	}

}
