package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import uap.workflow.bpmn2.model.event.BoundaryEvent;
import uap.workflow.bpmn2.model.event.EndEvent;
import uap.workflow.bpmn2.model.event.IntermediateCatchEvent;
import uap.workflow.bpmn2.model.event.IntermediateThrowEvent;
import uap.workflow.bpmn2.model.event.StartEvent;

public class SubProcess extends Activity implements IFlowElementsContainer {

	private static final long serialVersionUID = -5134340739990002986L;

	@XmlAttribute
	public boolean triggeredByEvent = false;

	@XmlElements({ 
		@XmlElement(name = "startEvent", type = StartEvent.class),

		@XmlElement(name = "endEvent", type = EndEvent.class),

		@XmlElement(name = "userTask", type = UserTask.class),
		@XmlElement(name = "sendTask", type = SendTask.class),
		@XmlElement(name = "manualTask", type = ManualTask.class),
		@XmlElement(name = "receiveTask", type = ReceiveTask.class),
		@XmlElement(name = "scriptTask", type = ScriptTask.class),
		@XmlElement(name = "serviceTask", type = ServiceTask.class),
		@XmlElement(name = "businessRuleTask", type = BusinessRuleTask.class),
		@XmlElement(name = "callActivity", type = CallActivity.class),
		@XmlElement(name = "subProcess", type = SubProcess.class),
//		@XmlElement(name = "subProcess", type = EventSubProcess.class),

		@XmlElement(name = "exclusiveGateway", type = ExclusiveGateway.class),
		@XmlElement(name = "inclusiveGateway", type = InclusiveGateway.class),
		@XmlElement(name = "parallelGateway", type = ParallelGateway.class),
		@XmlElement(name = "complexGateway", type = ComplexGateway.class),
		@XmlElement(name = "eventBasedGateway", type = EventGateway.class),

		@XmlElement(name = "intermediateCatchEvent", type = IntermediateCatchEvent.class),
		
		@XmlElement(name = "intermediateThrowEvent", type = IntermediateThrowEvent.class),
		@XmlElement(name = "boundaryEvent", type = BoundaryEvent.class),

		@XmlElement(name = "annotation", type = Annotation.class),
		@XmlElement(name = "group", type = Group.class),
//		@XmlElement(name = "lane", type = Lane.class),
		
		@XmlElement(name = "sequenceFlow", type = SequenceFlow.class), 
		@XmlElement(name = "messageFlow", type = MessageFlow.class), 
		@XmlElement(name = "association", type = Association.class) 
	})
	public List<FlowElement> flowElements = new ArrayList<FlowElement>();

	public List<FlowElement> getFlowElements() {
		return this.flowElements;
	}
	public void setFlowElements(List<FlowElement> flowElements) {
		this.flowElements = flowElements;
	}
	public boolean isContainer() {
		return true;
	}
	public boolean isCanBound(){
		return true;
	}

	public boolean isTriggeredByEvent() {
		return triggeredByEvent;
	}

	public void setTriggeredByEvent(boolean triggeredByEvent) {
		this.triggeredByEvent = triggeredByEvent;
	}
	
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.SubProcessBeanInfo";
	}
}
