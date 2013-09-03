package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.model.BaseElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="MessageRef")
public class MessageRef extends BaseElement {

	@XmlAttribute
	public String name="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
