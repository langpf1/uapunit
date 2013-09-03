package uap.workflow.bpmn2.model;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowNode")
public class FlowNode extends FlowElement {
	private static final long serialVersionUID = -4021562837862875641L;
	@XmlTransient
	public List<Connector> incoming = new ArrayList<Connector>();
	@XmlTransient
	public List<Connector> outgoing = new ArrayList<Connector>();
	public List<Connector> getIncoming() {
		return this.incoming;
	}
	public void setIncoming(List<Connector> incoming) {
		this.incoming = incoming;
	}
	public List<Connector> getOutgoing() {
		return this.outgoing;
	}
	public void setOutgoing(List<Connector> outgoing) {
		this.outgoing = outgoing;
	}
	public void addInComing(Connector bsf) {
		if (this.incoming == null) {
			this.incoming = new ArrayList<Connector>();
		}
		this.incoming.add(bsf);
	}
	public void addOutGoing(Connector bsf) {
		if (this.outgoing == null) {
			this.outgoing = new ArrayList<Connector>();
		}
		this.outgoing.add(bsf);
	}
}
