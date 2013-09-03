package uap.workflow.ui.notice.editors;

import java.awt.Container;
import java.beans.PropertyEditorSupport;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;

import nc.ui.pub.beans.UIPanel;

public class NoticePropertyDlg extends BpmnPropertyEditingDlg {
	private static final long serialVersionUID = 1098950625210213537L;
	private NoticePropertyTablePane listenerTablePane;
	
	public NoticePropertyDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		this.propertyEditor =propertyEditor;
		setSize(950, 500);
	}
	
	public UIPanel getUIPanelMain(){
		if(listenerTablePane==null){
			listenerTablePane =new NoticePropertyTablePane(new NoticePropertyModel());
		}
		return listenerTablePane;
	}	
}
