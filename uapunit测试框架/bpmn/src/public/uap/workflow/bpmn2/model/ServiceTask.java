package uap.workflow.bpmn2.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ServiceTask")
public class ServiceTask extends Task {
	
	@XmlAttribute(name="implementation")
	@PropEditor("uap.workflow.modeler.editors.ServiceImplementTypeEditor")
	@TypeChangeMonitor("implementation")
	public String implementation;
	@XmlAttribute(name="class",namespace=NameSpaceConst.BIZEX_URL)
	@TypeChangeMonitor("extendClass")
	@PropEditor("uap.workflow.modeler.editors.ServiceClassSelectionEditor")
	public String extendClass;
	@XmlAttribute(name="method",namespace=NameSpaceConst.BIZEX_URL)
	@TypeChangeMonitor("method")
	@PropEditor("uap.workflow.modeler.editors.MethodComboBoxEditor")
	public String method;
	@XmlTransient
	@PropEditor("uap.workflow.modeler.editors.ParameterEditor")
	public List<FieldExtension> fieldExtensions = new ArrayList<FieldExtension>();
	@XmlAttribute
	public String operationRef;
	@XmlAttribute(name="resultVariableName",namespace=NameSpaceConst.BIZEX_URL)
	public String resultVariableName;

	public String getExtendClass() {
		return extendClass;
	}

	public void setExtendClass(String extendClass) {
		this.extendClass = extendClass;
	}

	public String getOperationRef() {
		return operationRef;
	}

	public void setOperationRef(String operationRef) {
		this.operationRef = operationRef;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getImplementation() {
		return this.implementation;
	}

	public String getResultVariableName() {
		return this.resultVariableName;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}

	public List<FieldExtension> getFieldExtensions() {
		return this.fieldExtensions;
	}

	public void setFieldExtensions(List<FieldExtension> fieldExtensions) {
		this.fieldExtensions = fieldExtensions;
	}

	public int getTaskType() {
		return 2;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ServiceTaskBeanInfo";
	}
	
	@Override
	public void marshal() {
		super.marshal();
		getExtensionElements().setFieldExtensions(getFieldExtensions());
	}
	
	@Override
	public void unmarshal() {
		super.unmarshal();
		setFieldExtensions(getExtensionElements().getFieldExtensions());
	}
}

/*
 * Location: E:\dev
 * tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer
 * .model_5.9.1.jar Qualified Name:
 * org.activiti.designer.bpmn2.model.ServiceTask JD-Core Version: 0.5.4
 */