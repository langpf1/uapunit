package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Annotation")
public class Annotation extends Artifact {

	private static final long serialVersionUID = 6081458190748900276L;
	@XmlAttribute
	public String text="";

	 public Annotation(){
	    this.name="Annotation";
	}
	 
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.AnnotationBeanInfo";
	}
}
