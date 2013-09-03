package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Association")
public class Association extends Connector {

	private static final long serialVersionUID = -1682338390293777905L;

	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.AssociationBeanInfo";
	}

}
