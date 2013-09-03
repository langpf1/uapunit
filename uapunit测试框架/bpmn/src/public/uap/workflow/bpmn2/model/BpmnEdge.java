package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BpmnElement")
public class BpmnEdge implements Serializable {
	private static final long serialVersionUID = 2239420900685809785L;
	@XmlAttribute
	public String id;
	@XmlAttribute
	public String bpmnElement;
	@XmlAttribute
	public String graphStyle;
	@XmlAttribute
	public String parentId;

	@XmlElement(name = "waypoint", namespace = NameSpaceConst.OMGDI_URL)
	public List<BpmnWayPoint> waypoints = new ArrayList<BpmnWayPoint>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getBpmnElement() {
		return bpmnElement;
	}
	
	public void setBpmnElement(String bpmnElement) {
		this.bpmnElement = bpmnElement;
	}

	public List<BpmnWayPoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<BpmnWayPoint> waypoints) {
		this.waypoints = waypoints;
	}

	public String getGraphStyle() {
		return graphStyle;
	}

	public void setGraphStyle(String graphStyle) {
		this.graphStyle = graphStyle;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}