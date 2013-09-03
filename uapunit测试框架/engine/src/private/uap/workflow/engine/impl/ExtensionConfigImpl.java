package uap.workflow.engine.impl;

import uap.workflow.engine.bpmn.behavior.ExtensionServiceConfig;
import uap.workflow.engine.cfg.ExtensionPropertyConfig;
import uap.workflow.engine.el.ExtensionFunctionConfig;
import uap.workflow.engine.invocation.ExtensionListenerConfig;
import uap.workflow.engine.itf.IExtensionConfig;
import uap.workflow.engine.server.BizProcessServer;

public class ExtensionConfigImpl implements IExtensionConfig {

	@Override
	public ExtensionFunctionConfig[] getExtensionFunctions() {
		return BizProcessServer.getProcessEngineConfig().getExtensionconfig().getFunctionExtension().toArray(new ExtensionFunctionConfig[0]);
	}

	@Override
	public ExtensionListenerConfig[] getExtensionListeners() {
		return BizProcessServer.getProcessEngineConfig().getExtensionconfig().getListenerExtension().toArray(new ExtensionListenerConfig[0]);
	}

	@Override
	public String[] getExtensionVariables() {
		return null;
	}

	@Override
	public ExtensionServiceConfig[] getExtensionServices() {
		return BizProcessServer.getProcessEngineConfig().getExtensionconfig().getServiceExtension().toArray(new ExtensionServiceConfig[0]);
	}

	public ExtensionPropertyConfig[] getExtensionPropertyConfig(){
		if(BizProcessServer.getInstance().isStart==false)
			BizProcessServer.getInstance().start();
		return BizProcessServer.getProcessEngineConfig().getExtensionconfig().getPropertyExtension().toArray(new ExtensionPropertyConfig[0]);
	}
}
