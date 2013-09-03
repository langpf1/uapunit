package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SequenceFlow")
public class SequenceFlow extends Connector implements ISynchronization{
	private static final long serialVersionUID = -6967374229686561506L;
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	@XmlElement(name = "conditionExpression")
	@TypeChangeMonitor("conditionExpression")
	public String conditionExpression;
	@XmlAttribute
	@TypeChangeMonitor("defaultSequenceFlow")
	public boolean defaultSequenceFlow = false;
	@XmlAttribute(name="isImmediate")
	public boolean immediate = false; 
	@uap.workflow.bpmn2.annotation.PropEditor1(value="uap.workflow.modeler.editors.BpmnListenerEditor",type=ExecutionListener.class)
	public List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();
	public ExtensionElements extensionElements = new ExtensionElements();

	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}
	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}
	public String getConditionExpression() {
		return this.conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	public List<ExecutionListener> getExecutionListeners() {
		return this.executionListeners;
	}
	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
	public boolean isDefaultSequenceFlow() {
		return defaultSequenceFlow;
	}
	public void setDefaultSequenceFlow(boolean defaultSequenceFlow) {
		this.defaultSequenceFlow = defaultSequenceFlow;
	}
	public boolean isImmediate() {
		return immediate;
	}
	public void setImmediate(boolean immediate) {
		this.immediate = immediate;
	}
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.SequenceFlowBeanInfo";
	}
	@Override
	public void marshal() {
		getExtensionElements().setExecutionListeners(getExecutionListeners());
	}
	@Override
	public void unmarshal() {
		setExecutionListeners(getExtensionElements().getExecutionListeners());
	}
}
