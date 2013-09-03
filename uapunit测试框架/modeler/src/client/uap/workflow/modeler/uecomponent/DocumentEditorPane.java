package uap.workflow.modeler.uecomponent;

import java.awt.BorderLayout;

import uap.workflow.bpmn2.model.Documentation;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextArea;

public class DocumentEditorPane extends UIPanel implements IAssemberPropertyData{

	private static final long serialVersionUID = -676027552159548251L;
	
	private UITextArea  textArea;
	
	public DocumentEditorPane(){
		
		initialize();
	}

	private void initialize(){
		textArea = new UITextArea();
		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		setLayout(new BorderLayout());
		add(textArea, BorderLayout.CENTER);
	}
	
	@Override
	public Object assemberData() {
		//Documentation doc = new Documentation();
		//doc.setValue(textArea.getText());
		//return doc;
		return textArea.getText();
	}

	@Override
	public void unassemberData(Object intializeData) {
		//textArea.setText(((Documentation)intializeData).getValue());
		textArea.setText((String)intializeData);
	}
	

}
