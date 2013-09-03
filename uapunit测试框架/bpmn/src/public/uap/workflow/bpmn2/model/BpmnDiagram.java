package uap.workflow.bpmn2.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BpmnDiagram")
public class BpmnDiagram implements Serializable{
	private static final long serialVersionUID = 2239420900685809785L;

	@XmlAttribute
	public String id;
	@XmlElement(name="BPMNPlane",namespace=NameSpaceConst.BPMNDI_URL)
	public BpmnPlane bpmnPlane;
	
	public BpmnPlane getBpmnPlane() {
		return bpmnPlane;
	}

	public void setBpmnPlane(BpmnPlane bpmnPlane) {
		this.bpmnPlane = bpmnPlane;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}	
}