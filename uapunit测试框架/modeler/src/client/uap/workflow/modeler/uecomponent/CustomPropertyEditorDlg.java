package uap.workflow.modeler.uecomponent;
import java.awt.Container;
import java.beans.PropertyEditorSupport;

import nc.ui.pub.beans.UIPanel;

public class CustomPropertyEditorDlg extends BpmnPropertyEditingDlg {

	private CustomPropertyTablePane customPropertyTablePane;
	
	public CustomPropertyEditorDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		this.propertyEditor =propertyEditor;
	}
	
	public UIPanel getUIPanelMain(){
		if(customPropertyTablePane==null){
			customPropertyTablePane = 
				new CustomPropertyTablePane(new CustomPropertyTableModel());
		}
		return customPropertyTablePane;
	}	
}
