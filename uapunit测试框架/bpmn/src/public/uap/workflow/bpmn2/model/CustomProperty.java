package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomProperty")
public class CustomProperty extends BaseElement {

	private static final long serialVersionUID = 5803688467709178850L;
	@XmlAttribute
	protected String name;
	@XmlAttribute(name = "value")
	protected String simpleValue;
	@XmlTransient
	protected ComplexDataType complexValue;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleValue() {
		return this.simpleValue;
	}

	public void setSimpleValue(String simpleValue) {
		this.simpleValue = simpleValue;
	}

	public ComplexDataType getComplexValue() {
		return this.complexValue;
	}

	public void setComplexValue(ComplexDataType complexValue) {
		this.complexValue = complexValue;
	}
}