package uap.workflow.ui.participant.editors;

import java.awt.Container;

import uap.workflow.modeler.editors.AbstractDailogPropertyEditor;


public class ParticipantPropertyEditor extends AbstractDailogPropertyEditor {

	protected ParticipantPropertyDlg initializeDlg(Container parent){
		return new ParticipantPropertyDlg(parent,ParticipantPropertyEditor.this);
	}
	
	protected void doButtonClick(){
		((ParticipantPropertyDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
