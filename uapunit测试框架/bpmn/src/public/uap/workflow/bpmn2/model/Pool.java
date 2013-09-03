package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pool")
public class Pool extends BaseElement
{
	private static final long serialVersionUID = 1L;
	
	@TypeChangeMonitor("name")
	@XmlAttribute
	public String name;
	@XmlAttribute
	protected String processRef;
	
	public String getName()
	{
		return this.name;
	}
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean isContainer() {
		return true;
	}
	public String getProcessRef() {
		return this.processRef;
	}
	
	public void setProcessRef(String processRef) {
		this.processRef = processRef;
	}
	
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.PoolBeanInfo";
	}	
}
