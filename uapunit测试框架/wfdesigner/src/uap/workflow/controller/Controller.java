package uap.workflow.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/** 
   Action øÿ÷∆∆˜ ≈‰÷√œÓ
 * @author 
 */
@XmlRootElement(name = "Controller")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Controller")
public class Controller implements Serializable{
	private static final long serialVersionUID = -9045110756346954073L;
	@XmlElementWrapper(name = "ActionConfigs")
	@XmlElement(name = "ActionConfig")
	public List<ActionConfig> actionConfigs = new ArrayList<ActionConfig>();

	public List<ActionConfig> getActionConfigs() {
		return actionConfigs;
	}
	public void setActionConfigs(List<ActionConfig> actionConfigs) {
		actionConfigs = actionConfigs;
	}
}