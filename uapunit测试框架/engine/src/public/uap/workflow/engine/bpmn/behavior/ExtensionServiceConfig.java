package uap.workflow.engine.bpmn.behavior;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExtensionServiceConfig")
public class ExtensionServiceConfig implements Serializable{
	private static final long serialVersionUID = -7442583347381847969L;
	@XmlAttribute(name="name")
	public String serviceName;
	@XmlAttribute(name="class")
	public String implClass;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getImplClass() {
		return implClass;
	}
	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}
}
