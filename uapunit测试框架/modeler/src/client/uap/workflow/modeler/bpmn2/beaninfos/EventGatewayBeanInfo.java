package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.EventGateway;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class EventGatewayBeanInfo extends GatewayBeanInfo {

	public EventGatewayBeanInfo() {
		super(EventGateway.class);
		addProperty("instantiate").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("eventGatewayType").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
