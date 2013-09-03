package uap.workflow.engine.itf;

import uap.workflow.engine.bpmn.behavior.ExtensionServiceConfig;
import uap.workflow.engine.cfg.ExtensionPropertyConfig;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.el.ExtensionFunctionConfig;
import uap.workflow.engine.invocation.ExtensionListenerConfig;

public interface IExtensionConfig {
	
	ExtensionListenerConfig[] getExtensionListeners();
	
	ExtensionFunctionConfig[] getExtensionFunctions();
	
	String[] getExtensionVariables();
	
	ExtensionServiceConfig[] getExtensionServices();
	
	ExtensionPropertyConfig[] getExtensionPropertyConfig();

}
