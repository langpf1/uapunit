package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.modeler.utils.BpmnModelerConstants;

public class GatewayBeanInfo extends FlowNodeBeanInfo{

	public GatewayBeanInfo(Class<?> type) {
		super(type);
		addProperty("gatewayDirection").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);
	}

}
