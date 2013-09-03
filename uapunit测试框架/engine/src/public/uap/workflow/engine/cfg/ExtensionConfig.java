package uap.workflow.engine.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.engine.bpmn.behavior.ExtensionServiceConfig;
import uap.workflow.engine.el.ExtensionELResolveConfig;
import uap.workflow.engine.el.ExtensionFunctionConfig;
import uap.workflow.engine.invocation.ExtensionListenerConfig;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtensionConfig")
public class ExtensionConfig implements Serializable{

	private static final long serialVersionUID = 375045117954018465L;

	@XmlElementWrapper(name="extensionListeners")
	@XmlElement(name="listener")
	public List<ExtensionListenerConfig> listenerExtension = new ArrayList<ExtensionListenerConfig>();
	
	@XmlElementWrapper(name="extensionResolvers")
	@XmlElement(name="resolver")
	public List<ExtensionELResolveConfig> resolveExtension = new ArrayList<ExtensionELResolveConfig>();
	
	@XmlElementWrapper(name="extensionFunctions")
	@XmlElement(name="function")
	public List<ExtensionFunctionConfig> functionExtension = new ArrayList<ExtensionFunctionConfig>();
	@XmlElementWrapper(name="extensionService")
	@XmlElement(name="service")
	public List<ExtensionServiceConfig> serviceExtension = new ArrayList<ExtensionServiceConfig>();
	
	@XmlElementWrapper(name="extendProperties")
	@XmlElement(name="extendProperty")	
	public List<ExtensionPropertyConfig> propertyExtension = new ArrayList<ExtensionPropertyConfig>();
	
	public List<ExtensionListenerConfig> getListenerExtension() {
		return listenerExtension;
	}

	public void setListenerExtension(List<ExtensionListenerConfig> listenerExtension) {
		this.listenerExtension = listenerExtension;
	}

	public List<ExtensionELResolveConfig> getResolveExtension() {
		return resolveExtension;
	}

	public void setResolveExtension(List<ExtensionELResolveConfig> resolveExtension) {
		this.resolveExtension = resolveExtension;
	}

	public List<ExtensionFunctionConfig> getFunctionExtension() {
		return functionExtension;
	}

	public void setFunctionExtension(List<ExtensionFunctionConfig> functionExtension) {
		this.functionExtension = functionExtension;
	}
	public List<ExtensionServiceConfig> getServiceExtension() {
		return serviceExtension;
	}
	public void setServiceExtension(List<ExtensionServiceConfig> serviceExtension) {
		this.serviceExtension = serviceExtension;
	}

	public List<ExtensionPropertyConfig> getPropertyExtension() {
		return propertyExtension;
	}

	public void setPropertyExtension(List<ExtensionPropertyConfig> propertyExtension) {
		this.propertyExtension = propertyExtension;
	}
}
