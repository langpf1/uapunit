package uap.workflow.modeler;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import uap.workflow.modeler.uecomponent.BpmnMenuBar;
import nc.ui.pub.beans.UIPanel;

public class BpmnEditorPanel extends UIPanel{
	private static final long serialVersionUID = 1L;
	private BpmnGraphEditorFrame frame = null;
	public BpmnEditorPanel(BasicBpmnGraphEditor bpmnEditor,BpmnMenuBar menubar)
	{
		this.setLayout(new BorderLayout());
		this.add(bpmnEditor,BorderLayout.CENTER);
		this.add(menubar,BorderLayout.NORTH);
	}
	public BpmnEditorPanel()
	{
		BpmnModeler editor = new BpmnModeler("");    //流程设计器完整版
		this.setLayout(new BorderLayout());
		this.add(editor,BorderLayout.CENTER);
		this.add(new BpmnMenuBar(editor.getGraphComponent()),BorderLayout.NORTH);
	}
	public JFrame createFrame(Container containter) {
		if (frame == null) {
			frame = new BpmnGraphEditorFrame(containter, this);
		}
		return frame;
	}

}