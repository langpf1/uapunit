package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.MessageFlow;
import uap.workflow.modeler.utils.BpmnModelerConstants;


public class MessageFlowBeanInfo extends FlowNodeBeanInfo{
	public MessageFlowBeanInfo() {
		super(MessageFlow.class);
		addProperty("messageRef").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
