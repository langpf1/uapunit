package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.SequenceFlow;
import uap.workflow.modeler.utils.BpmnModelerConstants;


public class SequenceFlowBeanInfo extends FlowNodeBeanInfo{
	public SequenceFlowBeanInfo() {
		super(SequenceFlow.class);
		addProperty("conditionExpression").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("defaultSequenceFlow").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("immediate").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("executionListeners").setCategory(BpmnModelerConstants.CATEGORY_LISTENERS);
	}

}
