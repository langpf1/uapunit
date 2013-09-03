package uap.workflow.app.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/** 
   参与者限定模式类型配置项
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParticipantFilterTypeConfig")
public class ParticipantFilterTypeConfig implements Serializable{
	private static final long serialVersionUID = 5806462807399503881L;
	@XmlAttribute
	private String code;
	@XmlAttribute(name="class")
	private String className;
	@XmlAttribute(name="impl")
	private String implClassName;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	public String getImplClassName() {
		return implClassName;
	}
	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}
}