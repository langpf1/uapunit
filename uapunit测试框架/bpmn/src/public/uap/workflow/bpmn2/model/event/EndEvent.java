package uap.workflow.bpmn2.model.event;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EndEvent")
public class EndEvent extends ThrowEvent {
	private static final long serialVersionUID = 7029294191264395039L;
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.EndEventBeanInfo";
	}
}
