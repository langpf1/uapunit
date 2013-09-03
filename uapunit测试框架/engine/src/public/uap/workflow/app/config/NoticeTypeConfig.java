package uap.workflow.app.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/** 
 * ֪ͨ����������
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoticeTypeConfig")
public class NoticeTypeConfig implements Serializable{
	private static final long serialVersionUID = 2299600740297406195L;
	@XmlAttribute
	private String code;
	@XmlAttribute(name="class")
	private String className;
	@XmlAttribute(name="impl")
	private String implClassName;

	public String getImplClassName() {
		return implClassName;
	}
	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}
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

}