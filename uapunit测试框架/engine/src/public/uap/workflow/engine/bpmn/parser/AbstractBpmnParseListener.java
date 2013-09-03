package uap.workflow.engine.bpmn.parser;
import java.util.List;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.variable.VariableDeclaration;
import uap.workflow.engine.xml.Element;
/**
 * Abstract base class for implementing a {@link BpmnParseListener} without
 * being forced to implement all methods provided, which makes the
 * implementation more robust to future changes.
 * 
 * @author ruecker
 */
public class AbstractBpmnParseListener implements BpmnParseListener {
	
	public void parseProcess(Element processElement, IProcessDefinition processDefinition) {
	}
	public void parseStartEvent(Element startEventElement, IScope scope, IActivity startEventActivity) {
	}
	public void parseExclusiveGateway(Element exclusiveGwElement, IScope scope, IActivity activity) {
	}
	public void parseInclusiveGateway(Element inclusiveGwElement, IScope scope, IActivity activity) {
	}
	public void parseComplexGateway(Element complexGwElement, IScope scope, IActivity activity) {
	}
	public void parseParallelGateway(Element parallelGwElement, IScope scope, IActivity activity) {
	}
	public void parseScriptTask(Element scriptTaskElement, IScope scope, IActivity activity) {
	}
	public void parseServiceTask(Element serviceTaskElement, IScope scope, IActivity activity) {
	}
	public void parseBusinessRuleTask(Element businessRuleTaskElement, IScope scope, IActivity activity) {
	}
	public void parseTask(Element taskElement, IScope scope, IActivity activity) {
	}
	public void parseManualTask(Element manualTaskElement, IScope scope, IActivity activity) {
	}
	public void parseUserTask(Element userTaskElement, IScope scope, IActivity activity) {
	}
	public void parseEndEvent(Element endEventElement, IScope scope, IActivity activity) {
	}
	public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, IActivity timerActivity) {
	}
	public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, IActivity activity, IActivity nestedErrorEventActivity) {
	}
	public void parseSubProcess(Element subProcessElement, IScope scope, IActivity activity) {
	}
	public void parseCallActivity(Element callActivityElement, IScope scope, IActivity activity) {
	}
	public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, IActivity activity) {
	}
	public void parseSequenceFlow(Element sequenceFlowElement, IScope scopeElement, ITransition transition) {
	}
	public void parseSendTask(Element sendTaskElement, IScope scope, IActivity activity) {
	}
	public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, IActivity activity) {
	}
	public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, IActivity timerActivity) {
	}
	public void parseRootElement(Element rootElement, List<IProcessDefinition> processDefinitions) {
	}
	public void parseReceiveTask(Element receiveTaskElement, IScope scope, IActivity activity) {
	}
	public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, IActivity signalActivity) {
	}
	public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl signalActivity) {
	}
	public void parseEventBasedGateway(Element eventBasedGwElement, IScope scope, IActivity activity) {
	}
	public void parseTransaction(Element transactionElement, IScope scope, IActivity activity) {
	}
	public void parseCompensateEventDefinition(Element compensateEventDefinition, IActivity compensationActivity) {
	}
	public void parseIntermediateThrowEvent(Element intermediateEventElement, IScope scope, IActivity activity) {
	}
	@Override
	public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, IActivity signalActivity) {
		
	}
    public void parseIntermediateCompensateEventDefinition(Element compensateEventDefinition, IActivity compensateActivity){
    	
    }
    public void parseIntermediateLinkCatchEventDefinition(Element linkEventDefinition, IActivity linkActivity){
    	
    }
}
