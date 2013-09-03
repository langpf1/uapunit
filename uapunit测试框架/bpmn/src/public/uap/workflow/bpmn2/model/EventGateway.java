package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import uap.workflow.bpmn2.annotation.PropEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="EventGateway")
public class EventGateway extends Gateway
{
	private static final long serialVersionUID = -4229671595050383640L;
	@XmlAttribute
	public boolean instantiate;
	@XmlAttribute
	@PropEditor("uap.workflow.modeler.editors.EventGatewayTypeEditor")
	public String eventGatewayType;
	
	public boolean isInstantiate() {
		return instantiate;
	}
	public void setInstantiate(boolean instantiate) {
		this.instantiate = instantiate;
	}
	public String getEventGatewayType() {
		return eventGatewayType;
	}
	public void setEventGatewayType(String eventGatewayType) {
		this.eventGatewayType = eventGatewayType;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.EventGatewayBeanInfo";
	}
	public int getGateWayType() {
		return 3;
	}
}
