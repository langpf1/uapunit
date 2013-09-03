package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowElement", propOrder = { "name" })
public class FlowElement extends BaseElement {
	
	private static final long serialVersionUID = 5964455577899828530L;
	
	@TypeChangeMonitor("name")
	@XmlAttribute
	public String name;
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}