package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ThrowEventBeanInfo extends EventBeanInfo {

	public ThrowEventBeanInfo(Class<?> type) {
		super(type);
		addProperty("executionListeners").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);
	}

}
