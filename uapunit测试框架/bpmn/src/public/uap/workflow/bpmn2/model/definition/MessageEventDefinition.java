package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="MessageEventDefinition")
public class MessageEventDefinition extends EventDefinition {
	private static final long serialVersionUID = -5375075936849072077L;

//	@XmlElement(name="operationRef")
//	public OperationRef operationRef = new OperationRef();
//	
//	@XmlElement(name="messageRef")
//	public MessageRef messageRef = new MessageRef();
//
//	public OperationRef getOperationRef() {
//		return operationRef;
//	}
//
//	public void setOperationRef(OperationRef operationRef) {
//		this.operationRef = operationRef;
//	}
//
//	public MessageRef getMessageRef() {
//		return messageRef;
//	}
//
//	public void setMessageRef(MessageRef messageRef) {
//		this.messageRef = messageRef;
//	}

	@XmlElement(name="operationRef")
	public String operationRef;
	
	@XmlAttribute(name="messageRef")
	public String messageRef;

	public String getOperationRef() {
		return operationRef;
	}

	public void setOperationRef(String operationRef) {
		this.operationRef = operationRef;
	}

	public String getMessageRef() {
		return messageRef;
	}

	public void setMessageRef(String messageRef) {
		this.messageRef = messageRef;
	}
}
