package uap.workflow.bpmn2.model;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documentation")
public class Documentation implements Serializable{
	private static final long serialVersionUID = -8930911267547488567L;
	@XmlValue
	protected String value="";
	public String getValue() {
		if (value == null)
			return "";
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String toString(){
		if (value == null)
			return "";
		return value;
	}
	
}
