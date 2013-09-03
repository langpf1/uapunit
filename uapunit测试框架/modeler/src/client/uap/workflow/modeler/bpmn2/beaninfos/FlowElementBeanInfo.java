package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.modeler.utils.BpmnModelerConstants;

public class FlowElementBeanInfo extends BaseElementBeanInfo {

	public FlowElementBeanInfo(Class<?> type) {
		super(type);
		addProperty("name").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);
	}

}
