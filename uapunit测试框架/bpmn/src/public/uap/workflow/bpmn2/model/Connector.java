package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Connector")
public class Connector extends FlowElement {

	private static final long serialVersionUID = 7602764851891449854L;
	@XmlAttribute
	public String sourceRef;
	@XmlTransient
	public FlowNode source;
	@XmlAttribute
	public String targetRef;
	@XmlTransient
	public FlowNode target;

	
	public FlowNode getSource() {
		return source;
	}
	public void setSource(FlowNode source) {
		this.source = source;
		if(this.source!=null)
		{
			this.sourceRef = this.source.getId();
		}
	}
	public FlowNode getTarget() {
		return target;
	}
	
	public void setTarget(FlowNode target) {
		this.target = target;
		if(this.target!=null)
		{
			this.targetRef = this.target.getId();
		}
	}

	public String getSourceRef() {
		if (sourceRef == null && source != null) {
			sourceRef = source.getId();
		}
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public String getTargetRef() {
		return targetRef;
	}
	public void setTargetRef(String targetRef) {
		if (targetRef == null && target != null) {
			targetRef = target.getId();
		}
		this.targetRef = targetRef;
	}
}
