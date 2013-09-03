package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.ServiceTask;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ServiceTaskBeanInfo extends TaskBeanInfo {

	public ServiceTaskBeanInfo() {
		super(ServiceTask.class);
		addProperty("implementation").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("extendClass").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("method").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("fieldExtensions").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("operationRef").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("resultVariableName").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		
	}

}
