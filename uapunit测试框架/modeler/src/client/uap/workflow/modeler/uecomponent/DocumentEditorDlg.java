package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;

import nc.ui.pub.beans.UIPanel;

public class DocumentEditorDlg extends BpmnPropertyEditingDlg {

	private static final long serialVersionUID = 14599367639038993L;

	private DocumentEditorPane documentEditorPane;

	public DocumentEditorDlg(Container parent, PropertyEditorSupport propertyEditor) {
		super(parent);
		setSize(500, 300);
		setResizable(true);
		//setContentPane(getUIDialogContentPane());
		this.propertyEditor =propertyEditor;
		
	}

	public UIPanel getUIPanelMain(){
		if(documentEditorPane==null){
			documentEditorPane =new DocumentEditorPane();
		}
		return documentEditorPane;
	}	

}
