package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.util.List;
import nc.ui.pub.beans.UIPanel;

public class ParameterEditorDlg extends BpmnPropertyEditingDlg {

	private ParameterTablePane listenerTablePane;
	
	public ParameterEditorDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		setSize(540, 400);
		this.propertyEditor =propertyEditor;
	}
	
	public UIPanel getUIPanelMain(){
		if(listenerTablePane==null){
			listenerTablePane =new ParameterTablePane(new ParameterTableModel());
		}
		return listenerTablePane;
	}	
}
