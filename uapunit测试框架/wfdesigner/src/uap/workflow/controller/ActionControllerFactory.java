package uap.workflow.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.action.IAction;
import uap.workflow.app.config.ParseConfigException;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.handler.IHandler;
/**
 * π§≥ß¿‡
 * 
 * @author
 */
public class ActionControllerFactory {
	private Map<String, ActionConfig> controllerConfigMap;
	private List<ActionConfig> controllerConfigs;
	
	private static ActionControllerFactory inst = new ActionControllerFactory();
	private ActionControllerFactory() {}
	public static ActionControllerFactory getInstance() {
		return inst;
	}
	public List<ActionConfig> getActionControllers(String configPath) {
		if (controllerConfigs == null) {
			Controller config = ControllerParser.getInstance().getControllerConfig(configPath);
			controllerConfigs = config.getActionConfigs();
		}
		return controllerConfigs;
	}
	public void initActionController() {
		String configPath = "uap/workflow/controller/ActionConfig.xml";
		controllerConfigs = getActionControllers(configPath);
		if (controllerConfigs == null) {
			return;
		}
		controllerConfigMap = new HashMap<String, ActionConfig>();
		for (ActionConfig actionController : controllerConfigs) {
			controllerConfigMap.put(actionController.getCode(), actionController);
		}
	}
	
	public ActionConfig getActionConfig(String name){
		if(controllerConfigMap == null)
		{
			initActionController();
		}
		ActionConfig actionController = controllerConfigMap.get(name);
		return actionController;
	}
	
	public IAction getAction(String name) {
		ActionConfig actionConfig = getActionConfig(name);
		if(actionConfig == null)
			return null;
		try {
			Class<IAction> clazz = (Class<IAction>) Class.forName(actionConfig.getAction());
			IAction action = clazz.newInstance();
			return action;
		} catch (Exception e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new ParseConfigException(e.getMessage());
		}
	}

	public IHandler getHandler(String name) {
		ActionConfig actionConfig = getActionConfig(name);
		if(actionConfig == null)
			return null;
		try {
			Class<IHandler> clazz = (Class<IHandler>) Class.forName(actionConfig.getHandler());
			IHandler handler = clazz.newInstance();
			return handler;
		} catch (Exception e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new ParseConfigException(e.getMessage());
		}
	}
}