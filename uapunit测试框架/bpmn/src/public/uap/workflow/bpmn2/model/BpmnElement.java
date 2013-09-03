package uap.workflow.bpmn2.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BpmnElement")
public class BpmnElement implements Serializable {
	private static final long serialVersionUID = 2239420900685809785L;
	@XmlAttribute
	public String id;
	@XmlAttribute
	public String graphStyle;
	@XmlAttribute
	public String parentid;
	@XmlAttribute
	public boolean isVertex;
	@XmlAttribute
	public String targetRef;
	@XmlAttribute
	public String sourceRef;
	@XmlAttribute
	public boolean edge = false;	

	@XmlElement(name = "Bounds", namespace = NameSpaceConst.OMGDC_URL)
	public BpmnBounds bounds;
	
	public boolean isEdge() {
		return edge;
	}
	public void setEdge(boolean edge) {
		this.edge = edge;
	}

	public String getGraphStyle() {
		return graphStyle;
	}
	public void setGraphStyle(String graphStyle) {
		this.graphStyle = graphStyle;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public boolean isVertex() {
		return isVertex;
	}
	public void setVertex(boolean isVertex) {
		this.isVertex = isVertex;
	}
	public String getTargetRef() {
		return targetRef;
	}
	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}
	public String getSourceRef() {
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public BpmnBounds getBounds() {
		return bounds;
	}
	public void setBounds(BpmnBounds bounds) {
		this.bounds = bounds;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}