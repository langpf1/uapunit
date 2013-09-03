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
@XmlType(name="CallActivity")
public class CallActivity extends Activity {

	private static final long serialVersionUID = 8661597514957440356L;
	@XmlAttribute
	public String calledElement;
	@XmlTransient
	@PropEditor("uap.workflow.modeler.editors.DataAssociationEditor")
	public List<DataAssociation> inParameters = new ArrayList<DataAssociation>();
	@XmlTransient
	@PropEditor("uap.workflow.modeler.editors.DataAssociationEditor")
	public List<DataAssociation> outParameters = new ArrayList<DataAssociation>();
	
	public String getCalledElement() {
		return this.calledElement;
	}

	public void setCalledElement(String calledElement) {
		this.calledElement = calledElement;
	}

	public List<DataAssociation> getInParameters() {
		return this.inParameters;
	}

	public void setInParameters(List<DataAssociation> inParameters) {
		this.inParameters = inParameters;
	}

	public List<DataAssociation> getOutParameters() {
		return this.outParameters;
	}

	public void setOutParameters(List<DataAssociation> outParameters) {
		this.outParameters = outParameters;
	}
	
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.CallActivityBeanInfo";
	}
	public void marshal() {
		getExtensionElements().setInParameters(getInParameters());
		getExtensionElements().setOutParameters(getOutParameters());
	}
	@Override
	public void unmarshal() {
		setInParameters(getExtensionElements().getInParameters());
		setOutParameters(getExtensionElements().getOutParameters());
	}

}
