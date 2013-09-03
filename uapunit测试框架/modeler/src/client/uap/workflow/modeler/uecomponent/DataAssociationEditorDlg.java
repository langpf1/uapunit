package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.util.List;
import nc.ui.pub.beans.UIPanel;

public class DataAssociationEditorDlg extends BpmnPropertyEditingDlg {

	private DataAssociationTablePane associationTablePane;
	
	public DataAssociationEditorDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		setSize(540, 400);
		this.propertyEditor =propertyEditor;
	}
	
	public UIPanel getUIPanelMain(){
		if(associationTablePane==null){
			associationTablePane =new DataAssociationTablePane(new DataAssociationTableModel());
		}
		return associationTablePane;
	}	
}
