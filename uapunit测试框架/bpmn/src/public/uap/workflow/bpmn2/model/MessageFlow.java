package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="MessageFlow")
public class MessageFlow extends Connector {

	private static final long serialVersionUID = 6521284780515475706L;
	
	public String messageRef;
	
	public String getMessageRef() {
		return messageRef;
	}

	public void setMessageRef(String messageRef) {
		this.messageRef = messageRef;
	}

	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.MessageFlowBeanInfo";
	}

}
