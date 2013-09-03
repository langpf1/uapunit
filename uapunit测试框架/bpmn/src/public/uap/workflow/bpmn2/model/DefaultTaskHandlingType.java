package uap.workflow.bpmn2.model;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.taskhandling.ITaskHandlingType;
/**
 * 任务处理类型
 * @author
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultTaskHandlingType")
public class DefaultTaskHandlingType implements Serializable,ITaskHandlingType {
	private static final long serialVersionUID = 6812557936745268533L;
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