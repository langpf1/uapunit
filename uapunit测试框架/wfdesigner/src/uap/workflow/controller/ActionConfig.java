package uap.workflow.controller;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/** 
 * Action øÿ÷∆∆˜ ≈‰÷√œÓ
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionConfig")
public class ActionConfig implements Serializable{
	private static final long serialVersionUID = 2299600740297406195L;
	@XmlAttribute
	private String code;
	@XmlAttribute
	private String action;
	@XmlAttribute
	private String handler;

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getHandler() {
		return handler;
	}
	
	public void setHandler(String handler) {
		this.handler = handler;
	}
}