package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataAssociation")
public class DataAssociation extends BaseElement {
	private static final long serialVersionUID = -8810748113974201295L;
	@XmlAttribute
	protected String source;
	@XmlAttribute
	protected String sourceExpression;
	@XmlAttribute
	protected String target;
	@XmlAttribute
	protected String targetExpression;

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSourceExpression() {
		return this.sourceExpression;
	}

	public void setSourceExpression(String sourceExpression) {
		this.sourceExpression = sourceExpression;
	}

	public String getTargetExpression() {
		return this.targetExpression;
	}

	public void setTargetExpression(String targetExpression) {
		this.targetExpression = targetExpression;
	}
}

/*
 * Location: E:\dev
 * tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer
 * .model_5.9.1.jar Qualified Name:
 * org.activiti.designer.bpmn2.model.IOParameter JD-Core Version: 0.5.4
 */