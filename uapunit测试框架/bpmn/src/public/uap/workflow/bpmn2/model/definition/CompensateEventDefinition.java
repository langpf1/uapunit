package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="CompensateEventDefinition")
public class CompensateEventDefinition extends EventDefinition {
	@XmlAttribute(name="waitForCompletion")
	public String waitForCompletion;
	@XmlAttribute(name="activityRef")
	public String activityReference;
	
	public String getWaitForCompletion() {
		return waitForCompletion;
	}


	public void setWaitForCompletion(String waitForCompletion) {
		this.waitForCompletion = waitForCompletion;
	}


	public String getActivityReference() {
		return activityReference;
	}


	public void setActivityReference(String activityReference) {
		this.activityReference = activityReference;
	}

}
