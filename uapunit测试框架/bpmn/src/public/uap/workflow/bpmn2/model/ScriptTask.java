package uap.workflow.bpmn2.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import uap.workflow.bpmn2.annotation.PropEditor;

public class ScriptTask extends Task {
	
	private static final long serialVersionUID = -8365625685849347728L;
	@XmlAttribute(name="scriptFormat")
	@PropEditor("uap.workflow.modeler.editors.ScriptLanguageTypeEditor")
	public String scriptFormat="javascript";
	@XmlElement(name = "script")
	@PropEditor("uap.workflow.modeler.editors.DocumentEditor")	
	public String script ;	
	@XmlElement(name="resultVariable")
	public String resultVariable;
	
	public String getScriptFormat() {
		return this.scriptFormat;
	}

	public void setScriptFormat(String scriptFormat) {
		this.scriptFormat = scriptFormat;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getTaskType() {
		return 1;
	}

	public String getResultVariable() {
		return resultVariable;
	}

	public void setResultVariable(String resultVariable) {
		this.resultVariable = resultVariable;
	}

	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ScriptTaskBeanInfo";
	}
}
