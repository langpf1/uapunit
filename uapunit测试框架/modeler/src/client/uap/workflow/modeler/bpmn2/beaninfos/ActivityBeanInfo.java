package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ActivityBeanInfo extends FlowNodeBeanInfo{

	public ActivityBeanInfo(Class<?> type) {
		super(type);
		addProperty("asynchronous").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("forCompensation").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("executionListeners").setCategory(BpmnModelerConstants.CATEGORY_LISTENERS);
		addProperty("loopCharacteristics").setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
		addProperty("customProperties").setCategory(BpmnModelerConstants.CATEGORY_EXTENDS);
//		addProperty("customerProperties").setCategory(BpmnModelerConstants.CATEGORY_EXTENDS);
//		addProperty("loopCharacteristics.sequential",type,true).setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
//		addProperty("loopCharacteristics.inputDataItem.dataItem",type,true).setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
//		addProperty("loopCharacteristics.loopCardinality",type,true).setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
//		addProperty("loopCharacteristics.elementVariable",type,true).setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
//		addProperty("loopCharacteristics.completionCondition",type,true).setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
		
	}
	

}
