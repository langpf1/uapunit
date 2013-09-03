package uap.workflow.bpmn2.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="IntermediateCatchEvent")
public class IntermediateCatchEvent extends CatchEvent
{
	public IntermediateCatchEvent(){
	}

	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.IntermediateCatchEventBeanInfo";
	}
	
	@Override
	public void marshal() {
		super.marshal();
	}
	
	@Override
	public void unmarshal() {
		super.unmarshal();
	}
}