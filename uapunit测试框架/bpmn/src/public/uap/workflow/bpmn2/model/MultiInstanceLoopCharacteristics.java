package uap.workflow.bpmn2.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="multiInstanceLoopCharacteristics")
public class MultiInstanceLoopCharacteristics implements Serializable{
	
	private static final long serialVersionUID = 148719481460202734L;

	@XmlElement(name="inputDataItem")
	public InputDataItem inputDataItem = new InputDataItem();
	@XmlElement(name="loopCardinality")
	public String loopCardinality;
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	@XmlElement(name="completionCondition")
	public String completionCondition;
	@XmlElement(name="loopDataInputRef")
	public String loopDataInputRef;
	@XmlAttribute
	public boolean sequential=false;

	public InputDataItem getInputDataItem() {
		return this.inputDataItem;
	}

	public void setInputDataItem(InputDataItem inputDataItem) {
		this.inputDataItem = inputDataItem;
	}

	public String getLoopCardinality() {
		return this.loopCardinality;
	}

	public void setLoopCardinality(String loopCardinality) {
		this.loopCardinality = loopCardinality;
	}

	public String getCompletionCondition() {
		return this.completionCondition;
	}

	public void setCompletionCondition(String completionCondition) {
		this.completionCondition = completionCondition;
	}

	public String getLoopDataInputRef() {
		return loopDataInputRef;
	}

	public void setLoopDataInputRef(String loopDataInputRef) {
		this.loopDataInputRef = loopDataInputRef;
	}

	public boolean isSequential() {
		return this.sequential;
	}

	public void setSequential(boolean sequential) {
		this.sequential = sequential;
	}
}


