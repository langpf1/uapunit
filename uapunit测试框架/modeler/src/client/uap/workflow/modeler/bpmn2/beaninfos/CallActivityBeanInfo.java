package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.CallActivity;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class CallActivityBeanInfo extends ActivityBeanInfo {

	public CallActivityBeanInfo() {
		super(CallActivity.class);

		addProperty("calledElement").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("inParameters").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("outParameters").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);

	}


}
