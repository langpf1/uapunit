package uap.workflow.bpmn2.model;

import java.util.List;

/**
 * 流程元素容器接口
 * @author
 */
public interface IFlowElementsContainer {

	List<FlowElement> getFlowElements();
	void setFlowElements(List<FlowElement> flowElements);
	boolean isContainer();
}