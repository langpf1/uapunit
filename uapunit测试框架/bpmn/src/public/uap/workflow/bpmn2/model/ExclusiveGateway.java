package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExclusiveGateway")
public class ExclusiveGateway extends Gateway
{
	private static final long serialVersionUID = -635115500283408326L;
	@XmlAttribute(name="default")
	public String defaultSequenceFlow;
	
	public String getDefaultSequenceFlow() {
		return defaultSequenceFlow;
	}
	public void setDefaultSequenceFlow(String defaultSequenceFlow) {
		this.defaultSequenceFlow = defaultSequenceFlow;
	}
	
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ExclusiveGatewayBeanInfo";
	}
	public int getGateWayType() {
		return 1;
	}
}
