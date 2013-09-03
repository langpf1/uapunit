package uap.workflow.bpmn2.model.event;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.model.ExtensionElements;
import uap.workflow.bpmn2.model.FormProperty;
import uap.workflow.bpmn2.model.NameSpaceConst;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StartEvent")
public class StartEvent extends CatchEvent {

	private static final long serialVersionUID = 7810562738520141004L;

	@XmlAttribute(name="initiator", namespace=NameSpaceConst.BIZEX_URL)
	public String initiator;
	@XmlAttribute(name="formKey", namespace=NameSpaceConst.BIZEX_URL)
	public String formKey;
	@PropEditor("uap.workflow.modeler.editors.FormPropertyEditor")
	@XmlTransient
	public List<FormProperty> formProperties = new ArrayList<FormProperty>();
	public ExtensionElements extensionElements = new ExtensionElements();

	public String getInitiator() {
		return this.initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getFormKey() {
		return this.formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public List<FormProperty> getFormProperties() {
		return this.formProperties;
	}
	public void setFormProperties(List<FormProperty> formProperties) {
		this.formProperties = formProperties;
	}
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.StartEventBeanInfo";
	}
	
	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}
	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}

	public void marshal() {
		this.extensionElements.setFormProperties(this.formProperties);
	}
	@Override
	public void unmarshal() {
		this.setFormProperties(this.extensionElements.getFormProperties());
	}

}
