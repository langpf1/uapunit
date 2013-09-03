package uap.workflow.ui.notice.editors;

import java.awt.Container;

import uap.workflow.modeler.editors.AbstractDailogPropertyEditor;


public class NoticePropertyEditor extends AbstractDailogPropertyEditor {

	protected NoticePropertyDlg initializeDlg(Container parent){
		return new NoticePropertyDlg(parent,NoticePropertyEditor.this);
	}
	
	protected void doButtonClick(){
		((NoticePropertyDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
