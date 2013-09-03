package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.BusinessRuleTask;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class BusinessRuleTaskBeanInfo extends TaskBeanInfo {

	public BusinessRuleTaskBeanInfo() {
		super(BusinessRuleTask.class);
		addProperty("ruleNames").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("inputVariables").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("exclude").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("resultVariableName").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
