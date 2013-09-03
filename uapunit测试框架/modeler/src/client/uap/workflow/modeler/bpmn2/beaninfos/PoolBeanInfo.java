package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.Pool;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class PoolBeanInfo extends BaseElementBeanInfo {

	public PoolBeanInfo() {
		super(Pool.class);
		addProperty("name").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);		
		//addProperty("documentation").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);		
	}
}
