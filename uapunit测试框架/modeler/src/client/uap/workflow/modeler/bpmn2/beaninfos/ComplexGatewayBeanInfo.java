package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.ComplexGateway;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ComplexGatewayBeanInfo extends GatewayBeanInfo {

	public ComplexGatewayBeanInfo() {
		super(ComplexGateway.class);
		addProperty("activationCondition").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}
	
}
