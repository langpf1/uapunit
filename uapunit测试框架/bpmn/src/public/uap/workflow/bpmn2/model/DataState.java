package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="DataState")
public class DataState extends BaseElement {
	private static final long serialVersionUID = -3770595613232051339L;
	@TypeChangeMonitor("name")
	@XmlAttribute
	public String name;
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
