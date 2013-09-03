package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="InclusiveGateway")
public class InclusiveGateway extends Gateway
{
	private static final long serialVersionUID = -5637456647565486548L;
	@XmlAttribute(name="default")
	public String defaultSequenceFlow;
	
	public String getDefaultSequenceFlow() {
		return defaultSequenceFlow;
	}
	public void setDefaultSequenceFlow(String defaultSequenceFlow) {
		this.defaultSequenceFlow = defaultSequenceFlow;
	}
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.InclusiveGatewayBeanInfo";
	}
	public int getGateWayType() {
		return 2;
	}
}
