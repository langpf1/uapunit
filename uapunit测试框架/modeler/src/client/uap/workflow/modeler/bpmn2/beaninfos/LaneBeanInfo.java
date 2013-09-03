package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.Lane;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class LaneBeanInfo extends BaseElementBeanInfo {

	public LaneBeanInfo() {
		super(Lane.class);
		addProperty("name").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);		
		//addProperty("documentation").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);		
	}

}
