package uap.workflow.modeler.editors;


import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;

public class DefaultBpmnPropertyEditor extends  PropertyEditorSupport {
	
	protected Component editor;
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private Object ownerObject; 
	private String propName;
	private BpmnGraphComponent graphComponent = null;
	//是否是参照类型的editor
	private boolean isRefEditor;
	
	public boolean isRefEditor() {
		return isRefEditor;
	}
	
	public void setRefEditor(boolean isRefEditor) {
		this.isRefEditor = isRefEditor;
	}

	public boolean isPaintable() {
		return true;
	}

	public boolean supportsCustomEditor() {
		return true;
	}

	public Component getCustomEditor() {
		return editor;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(Object oldValue, Object newValue) {
		listeners.firePropertyChange("value", oldValue, newValue);
	}


	public String[] getTags() {
		return null;
	}
	
	public void setOwnerObject(Object ownerObject) {
		this.ownerObject = ownerObject;
	}

	public Object getOwnerObject() {
		return ownerObject;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}
	
	public BpmnGraphComponent getGraphComponent() {
		return graphComponent;
	}

	public void setGraphComponent(BpmnGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}


}

