package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.ScriptTask;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ScriptTaskBeanInfo extends TaskBeanInfo {

	public ScriptTaskBeanInfo() {
		super(ScriptTask.class);
		addProperty("scriptFormat").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("script").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("resultVariable").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);	
	}
	

}
