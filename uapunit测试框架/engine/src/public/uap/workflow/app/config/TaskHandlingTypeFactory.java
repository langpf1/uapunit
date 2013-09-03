package uap.workflow.app.config;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.app.taskhandling.ITaskHandlingType;
import uap.workflow.engine.logger.WorkflowLogger;
/**
 * 通知工厂类 NoticeTypeFactory,ParticipantFilterTypeFactory与ParticipantTypeFactory通过泛型合一
 * 
 * @author
 */
public class TaskHandlingTypeFactory {
	private Map<String, ITaskHandlingType> types;
	private Map<String, TaskHandlingTypeConfig> typeConfigMap;
	private List<TaskHandlingTypeConfig> typeConfigs;
	
	private static TaskHandlingTypeFactory inst = new TaskHandlingTypeFactory();
	private TaskHandlingTypeFactory() {}
	public static TaskHandlingTypeFactory getInstance() {
		return inst;
	}
	private List<TaskHandlingTypeConfig> getTypeConfigs(String configPath) {
		if (typeConfigs == null) {
			BusinessExtendConfig config = BusinessExtendConfigParser.getInstance().getBusinessExtendConfig(configPath);
			typeConfigs = config.getTaskHandlingTypes();
		}
		return typeConfigs;
	}
	@SuppressWarnings("unchecked")
	private void buildAllTypes() {
		String configPath = "uap/workflow/app/config/BizAdapter.xml";
			typeConfigs = getTypeConfigs(configPath);
		if (typeConfigs == null) {
			return;
		}
		types = new HashMap<String, ITaskHandlingType>();
		typeConfigMap = new HashMap<String, TaskHandlingTypeConfig>();
		for (TaskHandlingTypeConfig typeConfig : typeConfigs) {
			try {
				Class<ITaskHandlingType> clazz = (Class<ITaskHandlingType>) Class.forName(typeConfig.getClassName());
				ITaskHandlingType type = clazz.newInstance();
				types.put(typeConfig.getCode(), type);
				typeConfigMap.put(typeConfig.getCode(), typeConfig);
			} catch (Exception e) {
				WorkflowLogger.error(e.getMessage(), e);
				throw new ParseConfigException(e.getMessage());
			}
		}
	}
	public String getImpl(String typeCode) {
		if (typeConfigMap == null) {
			buildAllTypes();
		}
		TaskHandlingTypeConfig typeConfig = typeConfigMap.get(typeCode);
		return typeConfig.getImplClassName();
	}
	public ITaskHandlingType getType(String code) {
		if (types == null) {
			buildAllTypes();
		}
		return types.get(code);
	}
	public Map<String, ITaskHandlingType> getTypes() {
		if (types == null) {
			buildAllTypes();
		}
		return types;
	}
}