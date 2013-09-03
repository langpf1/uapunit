package uap.workflow.bpmn2.model.definition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.model.NameSpaceConst;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Condition")
public class Condition implements Serializable{
	
	private static final long serialVersionUID = -8494478536242803753L;
	@XmlAttribute(namespace=NameSpaceConst.BIZEX_URL)
	public String type="tFormalExpression";
	@XmlValue
	@PropEditor("uap.workflow.modeler.editors.ExpressionEditor")
	public String value="";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
