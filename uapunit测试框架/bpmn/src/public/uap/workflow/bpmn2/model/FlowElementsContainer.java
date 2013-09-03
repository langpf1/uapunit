package uap.workflow.bpmn2.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;
import uap.workflow.bpmn2.model.event.BoundaryEvent;
import uap.workflow.bpmn2.model.event.EndEvent;
import uap.workflow.bpmn2.model.event.IntermediateCatchEvent;
import uap.workflow.bpmn2.model.event.IntermediateThrowEvent;
import uap.workflow.bpmn2.model.event.StartEvent;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowElementsContainer")
public class FlowElementsContainer extends BaseElement implements ISynchronization, IFlowElementsContainer {
	private static final long serialVersionUID = 3516155372165002584L;

	@TypeChangeMonitor("name")
	@XmlAttribute
	public String name;

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
		//@XmlElement(name = "subProcess", type = EventSubProcess.class),

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
		
		@XmlElement(name = "DataObject", type = DataObject.class), 
		@XmlElement(name = "sequenceFlow", type = SequenceFlow.class), 
		@XmlElement(name = "messageFlow", type = MessageFlow.class), 
		@XmlElement(name = "association", type = Association.class) 
	})
	public List<FlowElement> flowElements = new ArrayList<FlowElement>();
	@XmlTransient
	@uap.workflow.bpmn2.annotation.PropEditor1(value="uap.workflow.modeler.editors.BpmnListenerEditor",type=ExecutionListener.class)
	public List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();

	@XmlElementWrapper(name = "laneSet")
	@XmlElement(name = "lane")
	public List<Lane> lanes = new ArrayList<Lane>();
	
	public ExtensionElements extensionElements = new ExtensionElements();

	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}
	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isContainer() {
		return true;
	}
	public List<FlowElement> getFlowElements() {
		return this.flowElements;
	}
	public void setFlowElements(List<FlowElement> flowElements) {
		this.flowElements = flowElements;
	}
	public List<Lane> getLanes() {
		return this.lanes;
	}
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}
	public List<ExecutionListener> getExecutionListeners() {
		return this.executionListeners;
	}
	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
	@Override
	public void marshal() {
		getExtensionElements().setExecutionListeners(getExecutionListeners());
	}
	@Override
	public void unmarshal() {
		setExecutionListeners(getExtensionElements().getExecutionListeners());
	}

}
