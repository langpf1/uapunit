package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.Annotation;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class AnnotationBeanInfo extends FlowElementBeanInfo {

	public AnnotationBeanInfo() {
		super(Annotation.class);
		//addProperty("name").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("text").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}

}
