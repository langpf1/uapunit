package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="BusinessRuleTask")
public class BusinessRuleTask extends Task {
	private static final long serialVersionUID = 8877229521195827710L;
	@XmlAttribute(name="resultVariableName",namespace=NameSpaceConst.BIZEX_URL)
	public String resultVariableName="rulesOutput";
	@XmlAttribute(name="exclude",namespace=NameSpaceConst.BIZEX_URL)
	public boolean exclude;
	@XmlAttribute(name="rules",namespace=NameSpaceConst.BIZEX_URL)
	public String ruleNames;
	@XmlAttribute(name="ruleVariablesInput",namespace=NameSpaceConst.BIZEX_URL)
	public String inputVariables;

	public boolean isExclude() {
		return this.exclude;
	}

	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	public String getResultVariableName() {
		return this.resultVariableName;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}


	public String getRuleNames() {
		return ruleNames;
	}

	public void setRuleNames(String ruleNames) {
		this.ruleNames = ruleNames;
	}

	public String getInputVariables() {
		return inputVariables;
	}

	public void setInputVariables(String inputVariables) {
		this.inputVariables = inputVariables;
	}

	public int getTaskType() {
		return 6;
	}

	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.BusinessRuleTaskBeanInfo";
	}
}
