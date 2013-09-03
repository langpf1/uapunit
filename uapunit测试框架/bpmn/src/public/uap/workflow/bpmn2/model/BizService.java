package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class BizService implements Serializable{

	private static final long serialVersionUID = -8220537202698339135L;
	@XmlAttribute
	public String implementation;
	@XmlAttribute
	public String implementationType;
	@XmlAttribute
	public String resultVariableName;
	@XmlElement
	public List<FieldExtension> fieldExtensions = new ArrayList<FieldExtension>();
	@XmlAttribute
	public List<CustomProperty> customProperties = new ArrayList<CustomProperty>();

	public String getImplementation() {
		return this.implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getImplementationType() {
		return this.implementationType;
	}

	public void setImplementationType(String implementationType) {
		this.implementationType = implementationType;
	}

	public String getResultVariableName() {
		return this.resultVariableName;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}

	public List<FieldExtension> getFieldExtensions() {
		return this.fieldExtensions;
	}

	public void setFieldExtensions(List<FieldExtension> fieldExtensions) {
		this.fieldExtensions = fieldExtensions;
	}

	public List<CustomProperty> getCustomProperties() {
		return this.customProperties;
	}

	public void setCustomProperties(List<CustomProperty> customProperties) {
		this.customProperties = customProperties;
	}

}
