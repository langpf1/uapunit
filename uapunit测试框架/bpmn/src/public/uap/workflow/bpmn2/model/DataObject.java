package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="DataObject")
public class DataObject extends  FlowElement{

	private static final long serialVersionUID = 2627244509629475750L;
	@XmlAttribute(name="isCollection")
	public boolean isCollection=false;

	public boolean getIsCollection() {
		return isCollection;
	}

	public boolean isIsCollection() {
		return isCollection;
	}

	public void setIsCollection(boolean collection) {
		isCollection = collection;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.DataBeanInfo";
	}
}
