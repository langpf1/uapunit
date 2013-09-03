package uap.workflow.modeler.bpmn2.beaninfos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.model.event.IntermediateThrowEvent;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="IntermediateThrowEventBeanInfo")
public class IntermediateThrowEventBeanInfo extends EventBeanInfo {

	public IntermediateThrowEventBeanInfo() {
		super(IntermediateThrowEvent.class);
	}
}
