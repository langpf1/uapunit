package uap.workflow.ui.participant.editors;

import java.awt.Container;
import java.beans.PropertyEditorSupport;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;

import nc.ui.pub.beans.UIPanel;

public class ParticipantPropertyDlg extends BpmnPropertyEditingDlg {
	private static final long serialVersionUID = 1098950625210213537L;
	private ParticipantPropertyTablePane listenerTablePane;
	
	public ParticipantPropertyDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		this.propertyEditor =propertyEditor;
		setSize(600, 400);
	}
	
	public UIPanel getUIPanelMain(){
		if(listenerTablePane==null){
			listenerTablePane =new ParticipantPropertyTablePane(new ParticipantPropertyModel());
		}
		return listenerTablePane;
	}	
}
