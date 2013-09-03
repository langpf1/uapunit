package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.SendTask;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class MailTaskBeanInfo extends TaskBeanInfo {

	public MailTaskBeanInfo() {
		super(SendTask.class);
		addProperty("to").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("from").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("subject").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("cc").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("bcc").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("charset").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("html").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("text").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
