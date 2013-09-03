package uap.workflow.bpmn2.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="IntermediateThrowEvent")
public class IntermediateThrowEvent extends ThrowEvent {
	
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.IntermediateThrowEventBeanInfo";
	}

}
