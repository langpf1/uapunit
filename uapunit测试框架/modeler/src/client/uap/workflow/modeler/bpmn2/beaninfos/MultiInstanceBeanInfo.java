package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.modeler.utils.BpmnModelerConstants;
import nc.vo.pub.graph.bean.GraphBeanInfo;

public class MultiInstanceBeanInfo extends GraphBeanInfo {

	public MultiInstanceBeanInfo(Class<?> type) {
		super(type);
		addProperty("sequential").setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
		addProperty("loopCardinality").setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
		addProperty("elementVariable").setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
		addProperty("completionCondition").setCategory(BpmnModelerConstants.CATEGORY_MULTIINSTANCE);
	}

}
