package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Participant")
public class Participant extends BaseElement {

	@XmlAttribute
	public String processRef = "";

	public String getProcessRef() {
		return processRef;
	}

	public void setProcessRef(String processRef) {
		this.processRef = processRef;
	}
}
