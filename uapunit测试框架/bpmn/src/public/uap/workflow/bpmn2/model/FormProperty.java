package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="FormProperty")
public class FormProperty extends BaseElement {
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected String value;
	@XmlAttribute
	protected String expression;
	@XmlAttribute
	protected String variable;
	@XmlAttribute
	protected String type;
	@XmlAttribute
	protected String defaultExpression;
	@XmlAttribute
	protected String datePattern;
	@XmlAttribute
	protected Boolean readable;
	@XmlAttribute
	protected Boolean writeable;
	@XmlAttribute
	protected Boolean required;
	@XmlElement(name="formValue")
	protected List<FormValue> formValues = new ArrayList<FormValue>();

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getVariable() {
		return this.variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getType() {
		return this.type;
	}

	public String getDefaultExpression() {
		return this.defaultExpression;
	}

	public void setDefaultExpression(String defaultExpression) {
		this.defaultExpression = defaultExpression;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatePattern() {
		return this.datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public Boolean getReadable() {
		return this.readable;
	}

	public void setReadable(Boolean readable) {
		this.readable = readable;
	}

	public Boolean getWriteable() {
		return this.writeable;
	}

	public void setWriteable(Boolean writeable) {
		this.writeable = writeable;
	}

	public Boolean getRequired() {
		return this.required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public List<FormValue> getFormValues() {
		return this.formValues;
	}

	public void setFormValues(List<FormValue> formValues) {
		this.formValues = formValues;
	}
}