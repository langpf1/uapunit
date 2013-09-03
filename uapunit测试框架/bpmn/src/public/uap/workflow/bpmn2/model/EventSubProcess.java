package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="EventSubProcess")
public class EventSubProcess extends SubProcess
{
	private static final long serialVersionUID = -489652105094115683L;
	@XmlAttribute
	public boolean triggeredByEvent = true;

	public boolean isTriggeredByEvent() {
		return triggeredByEvent;
	}

	public void setTriggeredByEvent(boolean triggeredByEvent) {
		this.triggeredByEvent = triggeredByEvent;
	}
}