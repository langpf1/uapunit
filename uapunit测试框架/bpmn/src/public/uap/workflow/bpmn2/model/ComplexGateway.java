package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ComplexGateway")
public class ComplexGateway extends Gateway {

	private static final long serialVersionUID = -2588123257235852528L;

	@XmlAttribute
	@TypeChangeMonitor("defaultSequenceFlow")
	public String defaultSequenceFlow;
	@XmlAttribute
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	public String activationCondition;

	public String getActivationCondition() {
		return activationCondition;
	}

	public void setActivationCondition(String activationCondition) {
		this.activationCondition = activationCondition;
	}
	
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ComplexGatewayBeanInfo";
	}

	public String getDefaultSequenceFlow() {
		return defaultSequenceFlow;
	}

	public void setDefaultSequenceFlow(String defaultSequenceFlow) {
		this.defaultSequenceFlow = defaultSequenceFlow;
	}

}
