package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Gateway")
public class Gateway extends FlowNode {
	@XmlAttribute
	public String defaultFlow;
	@PropEditor("uap.workflow.modeler.editors.BpmnGateWayTypeEditor")
	@TypeChangeMonitor("gateWay")
	public int gatewayType;
	@PropEditor("uap.workflow.modeler.editors.GatewayDirectionEditor")
	@XmlAttribute
	public String gatewayDirection="Unspecified";
	
	public String getGatewayDirection() {
		return gatewayDirection;
	}

	public void setGatewayDirection(String gatewayDirection) {
		this.gatewayDirection = gatewayDirection;
	}

	public int getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(int gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getDefaultFlow() {
		return this.defaultFlow;
	}

	public void setDefaultFlow(String defaultFlow) {
		this.defaultFlow = defaultFlow;
	}
}
