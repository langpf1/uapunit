package uap.workflow.bizimpl.listener;

import java.util.ArrayList;
import java.util.List;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;

public class ListenerDefinition {
	protected String listenerType;
	protected String event;
	protected String implementationType;
	protected String implementation;
	protected String method;
	protected String scriptProcessor;
	protected List<FieldDeclaration> fieldExtensions = new ArrayList<FieldDeclaration>();

	public String getListenerType() {
		return listenerType;
	}
	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getImplementationType() {
		return implementationType;
	}
	public void setImplementationType(String implementationType) {
		this.implementationType = implementationType;
	}
	public String getImplementation() {
		return implementation;
	}
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getScriptProcessor() {
		return scriptProcessor;
	}
	public void setScriptProcessor(String scriptProcessor) {
		this.scriptProcessor = scriptProcessor;
	}
	public List<FieldDeclaration> getFieldExtensions() {
		return fieldExtensions;
	}
	public void setFieldExtensions(List<FieldDeclaration> fieldExtensions) {
		this.fieldExtensions = fieldExtensions;
	}
}
