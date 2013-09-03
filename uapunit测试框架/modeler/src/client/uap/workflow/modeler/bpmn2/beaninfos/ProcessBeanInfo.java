package uap.workflow.modeler.bpmn2.beaninfos;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.modeler.utils.BpmnModelerConstants;
public class ProcessBeanInfo extends FlowElementsContainerBeanInfo{

	public ProcessBeanInfo() {
		super(Process.class);
		addProperty("name");
		addProperty("executable");
		addProperty("processType");
		addProperty("objectType");
		addProperty("matchPolicy");
		//addProperty("organization");
		addProperty("executionListeners");
		addProperty("customProperties").setCategory(BpmnModelerConstants.CATEGORY_EXTENDS);
	}

}
