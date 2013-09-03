package uap.workflow.engine.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExtensionPropertyConfig")
public class ExtensionPropertyConfig implements Serializable {

	private static final long serialVersionUID = -5482728571088904353L;
	@XmlAttribute(name="notationType")
	public String notationType;
	@XmlAttribute(name="classImpl")
	public String classImpl;
	public String getNotationType() {
		return notationType;
	}
	public void setNotationType(String notationType) {
		this.notationType = notationType;
	}
	public String getClassImpl() {
		return classImpl;
	}
	public void setClassImpl(String classImpl) {
		this.classImpl = classImpl;
	}
	
	
}
