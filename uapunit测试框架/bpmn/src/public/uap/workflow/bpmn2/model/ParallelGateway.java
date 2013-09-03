package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ParallelGateway")
public class ParallelGateway extends Gateway
{
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ParallelGatewayBeanInfo";
	}
	public int getGateWayType() {
		return 0;
	}
}
