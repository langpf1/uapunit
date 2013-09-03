package uap.workflow.modeler.bpmn.listeners;

import uap.workflow.modeler.sheet.Property;


public interface GraphBeanPropChangeListener {
	
	/**
	 * @param propValue
	 */
	public void valueChanged(Property srcProp, Property targetProp, Object userObject);

}
