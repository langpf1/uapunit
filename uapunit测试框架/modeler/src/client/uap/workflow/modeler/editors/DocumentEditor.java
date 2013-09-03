package uap.workflow.modeler.editors;

import java.awt.Container;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.DocumentEditorDlg;


public class DocumentEditor extends AbstractDailogPropertyEditor {

	@Override
	protected void doButtonClick() {
		((DocumentEditorDlg)dlg).SetPropertys(getValue());
		//		dlg.setVisible(true);
		dlg.showModal();
	}

	@Override
	protected BpmnPropertyEditingDlg initializeDlg(Container parent) {
		return new DocumentEditorDlg(parent,DocumentEditor.this);
	}

}
