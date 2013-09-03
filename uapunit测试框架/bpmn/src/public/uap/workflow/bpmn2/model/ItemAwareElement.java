package uap.workflow.bpmn2.model;

import java.util.List;

import uap.workflow.bpmn2.model.definition.ItemDefinition;


public class ItemAwareElement extends BaseElement{
	private static final long serialVersionUID = 4546422856244563008L;
	List<ItemDefinition> itemSubjectRef=null;
	List<DataState> dataState=null;
    
	public List<ItemDefinition> getItemSubjectRef() {
		return itemSubjectRef;
	}
	public void setItemSubjectRef(List<ItemDefinition> itemSubjectRef) {
		this.itemSubjectRef = itemSubjectRef;
	}
	public List<DataState> getDataState() {
		return dataState;
	}
	public void setDataState(List<DataState> dataState) {
		this.dataState = dataState;
	}
}
