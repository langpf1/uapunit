package uap.workflow.bpmn2.model;

import java.util.List;

/**
 * ����Ԫ�������ӿ�
 * @author
 */
public interface IFlowElementsContainer {

	List<FlowElement> getFlowElements();
	void setFlowElements(List<FlowElement> flowElements);
	boolean isContainer();
}