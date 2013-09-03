package uap.workflow.bpmn2.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.participant.IParticipantFilterType;

/** 
 * 参与者过滤类型的默认实现
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultParticipantFilterType")
public class DefaultParticipantFilterType implements Serializable,IParticipantFilterType{
	private static final long serialVersionUID = 7996295676021092741L;
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

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}
}