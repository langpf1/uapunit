package uap.workflow.modeler.bpmn.bpmn2Diagram;
import java.util.ArrayList;
import java.util.List;

import uap.workflow.bpmn2.model.EventListener;
import uap.workflow.bpmn2.model.Process;
public class SequenceFlowModel {
	public String id;
	public String name;
	public String sourceRef;
	public String targetRef;
	public String conditionExpression;
	public List<EventListener> listenerList = new ArrayList<EventListener>();
	public Process parentProcess;
}