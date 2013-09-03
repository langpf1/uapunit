package uap.workflow.engine.bpmn.parser;

import java.io.Serializable;

public class LinkEventDefinition implements Serializable {
	private static final long serialVersionUID = 3225677415375692821L;

	public Target target = new Target();
    public String activityId;
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public String getActivityId(){
		return activityId;
	}
	public void setActivityId(String activityId){
		this.activityId=activityId;
	}
	
	public boolean catches(String value) {
	    return value == null || this.target.getValue() == null || this.target.getValue().equals(value);
    }

}
