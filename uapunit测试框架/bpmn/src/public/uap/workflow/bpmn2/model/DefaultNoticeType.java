package uap.workflow.bpmn2.model;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.notice.INoticeType;
/**
 * 通知类型
 * @author
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultNoticeType")
public class DefaultNoticeType implements Serializable,INoticeType {
	private static final long serialVersionUID = -4663767156478195341L;
	@XmlAttribute
	String code="";
	@XmlTransient
	String name="";
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public void setCode(String code) 
	{
		this.code = code;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
}