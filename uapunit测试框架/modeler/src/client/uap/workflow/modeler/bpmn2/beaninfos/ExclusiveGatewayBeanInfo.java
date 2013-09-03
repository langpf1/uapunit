package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.ExclusiveGateway;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ExclusiveGatewayBeanInfo extends GatewayBeanInfo {

	public ExclusiveGatewayBeanInfo() {
		super(ExclusiveGateway.class);
		addProperty("defaultSequenceFlow").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
