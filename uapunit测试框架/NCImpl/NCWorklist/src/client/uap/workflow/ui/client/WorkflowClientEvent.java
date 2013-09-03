package uap.workflow.ui.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorkflowClientEvent implements Serializable {

	private String eventType;
	private Map<String, Object> eventArgs = new HashMap<String, Object>();
	private static final long serialVersionUID = 8511228636199919933L;

	public WorkflowClientEvent(String eventType){
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public void setEventArg(String key, Object value){
		eventArgs.put(key, value);
	}
}
