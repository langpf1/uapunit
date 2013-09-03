package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Collaboration")
public class Collaboration extends BaseElement {
	private static final long serialVersionUID = 4669077081838430135L;

	@XmlElement(name="participant")
	public List<Participant> participants = new ArrayList<Participant>();

	@XmlElement(name="messageFlow")
	public List<MessageFlow> messageFlows = new ArrayList<MessageFlow>();

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public List<MessageFlow> getMessageFlows() {
		return messageFlows;
	}

	public void setMessageFlows(List<MessageFlow> messageFlows) {
		this.messageFlows = messageFlows;
	}
	
}
