package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Artifact")
public class Artifact extends BaseElement {

private static final long serialVersionUID = 5964455577899828530L;
  
	public String name;
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		String strName = "";
		if(name != null)
		{
			strName = name;
		}
		if(this instanceof Annotation)
			strName=((Annotation)this).text ;
		return strName ;
	}
	
}
