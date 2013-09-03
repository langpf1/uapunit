package uap.workflow.bpmn2.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Process")
public class Process extends FlowElementsContainer {
	private static final long serialVersionUID = 2804284104304124980L;
	@XmlAttribute(name = "processDefinitionPk")
	private String processDefinitionPk = null;
	@XmlAttribute(name = "isExecutable")
	public boolean executable = true;
	@PropEditor("uap.workflow.modeler.editors.ProcessTypeEditor")
	@XmlAttribute
	public String processType = "Public";
	//@PropEditor("uap.workflow.client.ui.BillOrTransTypeEditor")
	@XmlAttribute(name = "objectType")
//	@XmlAttribute(name = "defaultBillType", namespace = NameSpaceConst.BIZEX_URL)
	public String objectType;
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	@XmlAttribute(name="matchPolicy")
	public String matchPolicy="getUserCode()=\"initor\" && organization in(\"\",\"\")";
	@XmlAttribute(namespace = NameSpaceConst.BIZEX_URL, name = "group")
	public String group;
	@PropEditor("uap.workflow.modeler.editors.OrgPropertyEditor")
	@XmlTransient
	public String[] organization;
	@PropEditor("uap.workflow.modeler.editors.CustomPropertyEditor")
	@XmlTransient
	public List<CustomProperty> customProperties = new ArrayList<CustomProperty>();
	
	public Process(){
		CustomProperty customProperty = new CustomProperty();
		customProperty.setName("initializeData");
		customProperty.setId("initializeData");
		customProperties.add(customProperty);
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String[] getOrganization() {
		return organization;
	}
	public void setOrganization(String[] orgs) {
		this.organization = orgs;
	}
	public boolean isExecutable() {
		return this.executable;
	}
	public void setExecutable(boolean executable) {
		this.executable = executable;
	}
	public List<CustomProperty> getCustomProperties() {
		return customProperties;
	}
	public void setCustomProperties(List<CustomProperty> customProperties) {
		this.customProperties = customProperties;
	}
	public String getProcessDefinitionPk() {
		return processDefinitionPk;
	}
	public void setProcessDefinitionPk(String processDefinitionPk) {
		this.processDefinitionPk = processDefinitionPk;
	}
	public String getMatchPolicy() {
		return matchPolicy;
	}
	public void setMatchPolicy(String matchPolicy) {
		this.matchPolicy = matchPolicy;
	}
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ProcessBeanInfo";
	}
	@Override
	public void marshal() {
		super.marshal();
		getExtensionElements().setCustomProperties(getCustomProperties());
		getExtensionElements().setOrganization(getOrganization());
	}
	@Override
	public void unmarshal() {
		super.unmarshal();
		setCustomProperties(getExtensionElements().getCustomProperties());
		setOrganization(getExtensionElements().getOrganization());
	}
}
