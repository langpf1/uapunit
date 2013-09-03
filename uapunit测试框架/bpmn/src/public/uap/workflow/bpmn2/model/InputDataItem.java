package uap.workflow.bpmn2.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="InputDataItem")
public class InputDataItem implements Serializable{
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	@XmlAttribute(name="name")
	public String dataItem;

	public String getDataItem() {
		return dataItem;
	}
	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}		
}