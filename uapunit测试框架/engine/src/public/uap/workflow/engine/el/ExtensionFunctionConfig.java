package uap.workflow.engine.el;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExtensionFunctionConfig")
public class ExtensionFunctionConfig implements Serializable{

	private static final long serialVersionUID = -4348076387651205964L;
	@XmlAttribute(name="namespace")
	public String namespace;
	@XmlAttribute(name="name")
	public String name;
	@XmlAttribute(name="method")
	public String method;
	@XmlAttribute(name="parameter")
	public String parameter;
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
