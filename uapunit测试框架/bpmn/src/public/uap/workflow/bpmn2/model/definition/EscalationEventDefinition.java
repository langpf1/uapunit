package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="EscalationEventDefinition")
public class EscalationEventDefinition extends EventDefinition {

	@XmlAttribute
	public String escalationRef;

	public String getEscalationRef() {
		return escalationRef;
	}

	public void setEscalationRef(String escalationRef) {
		this.escalationRef = escalationRef;
	}
	
}
