package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.DataObject;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class DataBeanInfo extends FlowElementBeanInfo{
	public DataBeanInfo() {
		super(DataObject.class);
		//addProperty("Collection").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}
	@Override
	public void adjustProperties(Object obj) {
		addProperty("isCollection").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		super.adjustProperties(obj);
	}
}
