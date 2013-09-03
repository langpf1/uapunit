package uap.workflow.engine.invocation;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExtensionListenerConfig")
public class ExtensionListenerConfig implements Serializable{
	private static final long serialVersionUID = 4481484008326299778L;
	@XmlAttribute(name="event")
	public String event; 
	@XmlAttribute(name="listenerType")
	public String listenerType;
	@XmlAttribute(name="name")
	public String name;
	@XmlAttribute(name="description")
	public String description;
	@XmlAttribute(name="implementation")
	public String implementation;
	@XmlAttribute(name="method")
	public String method;
	@XmlAttribute(name="parameters")
	public String parameters;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getListenerType() {
		return listenerType;
	}
	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
