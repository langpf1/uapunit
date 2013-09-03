package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventListener")
public class EventListener extends BaseElement {

	private static final long serialVersionUID = -7393689651113730038L;
	@XmlAttribute
	protected String event;
	@XmlAttribute
	protected String implementationType;
	@XmlAttribute
	protected String implementation;
	@XmlAttribute
	protected String method;
	@XmlAttribute
	protected String scriptProcessor;
	@XmlElement(name="fieldExtension")
	protected List<FieldExtension> fieldExtensions = new ArrayList<FieldExtension>();

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

	public List<FieldExtension> getFieldExtensions() {
		return fieldExtensions;
	}

	public void setFieldExtensions(List<FieldExtension> fieldExtensions) {
		this.fieldExtensions = fieldExtensions;
	}

	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getImplementationType() {
		return this.implementationType;
	}

	public void setImplementationType(String implementationType) {
		this.implementationType = implementationType;
	}

	public String getImplementation() {
		return this.implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}




}
