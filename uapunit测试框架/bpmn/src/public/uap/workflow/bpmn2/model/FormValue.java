package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAttribute;

public class FormValue extends BaseElement {
	@XmlAttribute
	protected String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}