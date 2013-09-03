package uap.workflow.engine.el;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExtensionELResolveConfig")
public class ExtensionELResolveConfig implements Serializable {
	private static final long serialVersionUID = -4144421732711293679L;
	@XmlAttribute(name="class")
	public String implementationClass;

	public String getImplementationClass() {
		return implementationClass;
	}

	public void setImplementationClass(String implementationClass) {
		this.implementationClass = implementationClass;
	}
}
