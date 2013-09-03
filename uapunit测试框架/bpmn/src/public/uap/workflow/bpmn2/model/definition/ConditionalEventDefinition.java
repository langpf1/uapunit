package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ConditionalEventDefinition")
public class ConditionalEventDefinition extends EventDefinition {
	private static final long serialVersionUID = 7222464530861209072L;
	@XmlElement(name="condition")
	public Condition condition = new Condition();

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
}
