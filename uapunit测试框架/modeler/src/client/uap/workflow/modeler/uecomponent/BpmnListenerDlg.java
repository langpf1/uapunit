package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.awt.event.WindowEvent;
import java.beans.PropertyEditorSupport;
import java.util.List;
import nc.ui.pub.beans.UIPanel;

public class BpmnListenerDlg extends BpmnPropertyEditingDlg {

	private BpmnListenerTablePane listenerTablePane;
	private boolean taskListenerType; 
	
	public void setTaskListenerType(boolean isTaskListener) {
		this.taskListenerType = isTaskListener;
		((BpmnListenerTablePane)getUIPanelMain()).setTaskListenerType(taskListenerType);
	}

	public BpmnListenerDlg(Container parent,PropertyEditorSupport propertyEditor) {
		super(parent);
		setSize(740, 480);
		setResizable(true);
		this.propertyEditor =propertyEditor;
	}
	
	public BpmnListenerDlg(Container parent,PropertyEditorSupport propertyEditor, boolean taskListenerType) {
		this(parent, propertyEditor);
		this.taskListenerType = taskListenerType;		
	}

	public UIPanel getUIPanelMain(){
		if(listenerTablePane==null){
			listenerTablePane =new BpmnListenerTablePane(new ListenerPropertyModel(), taskListenerType);
		}
		return listenerTablePane;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		super.windowOpened(e);
		listenerTablePane.setTaskListenerType(taskListenerType);
		listenerTablePane.SetCellEditor();
	}
}
