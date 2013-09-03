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
@XmlType(name = "BpmnPlane")
public class BpmnPlane implements Serializable {
	private static final long serialVersionUID = 2239420900685809785L;
	@XmlAttribute
	public String id;
	@XmlAttribute
	public String bpmnElement;
	@XmlElement(name = "BPMNShape", namespace = NameSpaceConst.BPMNDI_URL)
	public List<BpmnShape> bpmnShapes = new ArrayList<BpmnShape>();
	@XmlElement(name = "BPMNEdge", namespace = NameSpaceConst.BPMNDI_URL)
	public List<BpmnEdge> bpmnEdges = new ArrayList<BpmnEdge>();

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

	public List<BpmnShape> getBpmnShapes() {
		return bpmnShapes;
	}

	public void setBpmnShapes(List<BpmnShape> bpmnShapes) {
		this.bpmnShapes = bpmnShapes;
	}

	public List<BpmnEdge> getBpmnEdges() {
		return bpmnEdges;
	}

	public void setBpmnEdges(List<BpmnEdge> bpmnEdges) {
		this.bpmnEdges = bpmnEdges;
	}

	public BpmnShape getBpmnShape(BaseElement baseElement)
	{
		for (BpmnShape bpmnShape : this.bpmnShapes) {
			if(bpmnShape.getBpmnElement().equals(baseElement.getId())){
				return bpmnShape;
			}
		}
		return null;
	}
	
	public BpmnEdge getBpmnEdge(Connector oldSequenceFlow)
	{
		for (BpmnEdge bpmnEdge : this.bpmnEdges) {
			if(bpmnEdge.getBpmnElement().equals(oldSequenceFlow.getId())){
				return bpmnEdge;
			}
		}
		return null;
	}
}