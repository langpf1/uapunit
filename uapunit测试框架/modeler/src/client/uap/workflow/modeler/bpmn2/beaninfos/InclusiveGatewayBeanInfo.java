package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.InclusiveGateway;
import uap.workflow.modeler.utils.BpmnModelerConstants;
public class InclusiveGatewayBeanInfo extends GatewayBeanInfo {

	public InclusiveGatewayBeanInfo() {
		super(InclusiveGateway.class);
		
		addProperty("defaultSequenceFlow").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);

	}

}
