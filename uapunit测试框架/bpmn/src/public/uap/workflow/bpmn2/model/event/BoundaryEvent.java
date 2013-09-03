package uap.workflow.bpmn2.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoundaryEvent")
public class BoundaryEvent extends CatchEvent
{
	@XmlAttribute(name = "attachedToRef")
	public String attachedToRef;
	@XmlAttribute
	public boolean cancelActivity;

	
	public String getAttachedToRef()
	{
		return this.attachedToRef;
	}

	public void setAttachedToRef(String attachedToRef) {
		this.attachedToRef = attachedToRef;
	}

	public boolean isCancelActivity() {
		return this.cancelActivity;
	}

	public void setCancelActivity(boolean cancelActivity) {
		this.cancelActivity = cancelActivity;
	}

	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.BoundaryEventBeanInfo";
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

/*
 * Location: E:\dev
 * tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer
 * .model_5.9.1.jar Qualified Name:
 * org.activiti.designer.bpmn2.model.BoundaryEvent JD-Core Version: 0.5.4
 */