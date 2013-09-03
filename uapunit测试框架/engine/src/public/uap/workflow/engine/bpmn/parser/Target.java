package uap.workflow.engine.bpmn.parser;

import java.io.Serializable;

public class Target implements Serializable{
	private static final long serialVersionUID = -6291253625939719838L;
	public String value="";

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}