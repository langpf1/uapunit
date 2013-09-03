package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lane")
public class Lane extends Artifact
{
	private static final long serialVersionUID = 1L;

	@TypeChangeMonitor("name")
	@XmlAttribute
	public String name;
	@XmlTransient
	public Process parentProcess;
	@XmlElement(name = "flowNodeRef")
	public List<String> flowReferences = new ArrayList<String>();

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}
	public boolean isContainer() {
		return true;
	}
	public Process getParentProcess() {
		return this.parentProcess;
	}
	
	public void setParentProcess(Process parentProcess) {
		this.parentProcess = parentProcess;
	}

	public List<String> getFlowReferences() {
		return this.flowReferences;
	}

	public void setFlowReferences(List<String> flowReferences) {
		this.flowReferences = flowReferences;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.LaneBeanInfo";
	}		   
}
