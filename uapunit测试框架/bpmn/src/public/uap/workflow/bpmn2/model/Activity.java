package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.model.event.BoundaryEvent;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Activity")
public class Activity extends FlowNode implements ISynchronization {
	private static final long serialVersionUID = 2239420900685809785L;
	@XmlAttribute(name="async")
	public boolean asynchronous = false;
	@XmlAttribute(name="isForCompensation")
	public boolean forCompensation = false;
	@XmlTransient
	public String defaultFlow;
	@PropEditor("uap.workflow.modeler.editors.MultiInstanceLoopEditor")
	@XmlElement(name="multiInstanceLoopCharacteristics")
	public MultiInstanceLoopCharacteristics loopCharacteristics;// = new MultiInstanceLoopCharacteristics();
	@XmlTransient
	public List<BoundaryEvent> boundaryEvents = new ArrayList<BoundaryEvent>();
	@uap.workflow.bpmn2.annotation.PropEditor1(value="uap.workflow.modeler.editors.BpmnListenerEditor",type=ExecutionListener.class)
	@XmlTransient
	public List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();
	public ExtensionElements extensionElements = new ExtensionElements();
	@XmlTransient
	@PropEditor("uap.workflow.modeler.editors.CustomPropertyEditor")
	public List<CustomProperty> customProperties = new ArrayList<CustomProperty>();

	
	public boolean isCanBound(){
		return true;
	}
	public boolean isAsynchronous() {
		return this.asynchronous;
	}
	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}
	public List<BoundaryEvent> getBoundaryEvents() {
		return this.boundaryEvents;
	}
	public void setBoundaryEvents(List<BoundaryEvent> boundaryEvents) {
		this.boundaryEvents = boundaryEvents;
	}
	public String getDefaultFlow() {
		return this.defaultFlow;
	}
	public void setDefaultFlow(String defaultFlow) {
		this.defaultFlow = defaultFlow;
	}
	public List<ExecutionListener> getExecutionListeners() {
		return this.executionListeners;
	}
	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
	public MultiInstanceLoopCharacteristics getLoopCharacteristics() {
		return this.loopCharacteristics;
	}
	public void setLoopCharacteristics(MultiInstanceLoopCharacteristics loopCharacteristics) {
		this.loopCharacteristics = loopCharacteristics;
	}
	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}
	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}
	public List<CustomProperty> getCustomProperties() {
		return customProperties;
	}
	public void setCustomProperties(List<CustomProperty> customProperties) {
		this.customProperties = customProperties;
	}
	public boolean isForCompensation() {
		return forCompensation;
	}
	public void setForCompensation(boolean forCompensation) {
		this.forCompensation = forCompensation;
	}
	@Override
	public void marshal() {
//		if (!loopCharacteristics.isSequential() &&			//避免没有输入内容时序列化为空元素
//				StringUtil.isEmptyOrNull(loopCharacteristics.getElementVariable()) &&
//				StringUtil.isEmptyOrNull(loopCharacteristics.getLoopCardinality()) &&
//				StringUtil.isEmptyOrNull(loopCharacteristics.getCompletionCondition()))
//			loopCharacteristics.setInputDataItem(null);
		getExtensionElements().setExecutionListeners(getExecutionListeners());
		getExtensionElements().setCustomProperties(getCustomProperties());
	}
	@Override
	public void unmarshal() {
		setExecutionListeners(getExtensionElements().getExecutionListeners());
		setCustomProperties(getExtensionElements().getCustomProperties());
	}
}