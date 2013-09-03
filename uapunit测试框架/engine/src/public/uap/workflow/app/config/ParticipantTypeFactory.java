package uap.workflow.app.config;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.app.participant.IParticipantType;
import uap.workflow.engine.logger.WorkflowLogger;
/**
 * 参与者模式工厂类 ParticipantFilterTypeFactory与ParticipantTypeFactory通过泛型合一
 * 
 * @author
 */
public class ParticipantTypeFactory {
	private Map<String, IParticipantType> types;
	private Map<String, ParticipantTypeConfig> typeConfigMap;
	private List<ParticipantTypeConfig> typeConfigs;
	private static ParticipantTypeFactory inst = new ParticipantTypeFactory();
	private ParticipantTypeFactory() {}
	public static ParticipantTypeFactory getInstance() {
		return inst;
	}
	private List<ParticipantTypeConfig> getTypeConfigs(String configPath) {
		if (typeConfigs == null) {
			BusinessExtendConfig config = BusinessExtendConfigParser.getInstance().getBusinessExtendConfig(configPath);
			typeConfigs = config.getParticipantTypes();
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
		types = new HashMap<String, IParticipantType>();
		typeConfigMap = new HashMap<String, ParticipantTypeConfig>();
		for (ParticipantTypeConfig typeConfig : typeConfigs) {
			try {
				Class<IParticipantType> clazz = (Class<IParticipantType>) Class.forName(typeConfig.getClassName());
				IParticipantType type = clazz.newInstance();
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
		ParticipantTypeConfig typeConfig = typeConfigMap.get(typeCode);
		return typeConfig.getImplClassName();
	}

	public String getSelectorClassName(String typeCode) {
		if (typeConfigMap == null) {
			buildAllTypes();
		}
		ParticipantTypeConfig typeConfig = typeConfigMap.get(typeCode);
		return typeConfig.getSelectorClassName();
	}

	public IParticipantType getType(String name) {
		if (types == null) {
			buildAllTypes();
		}
		return types.get(name);
	}
	public Map<String, IParticipantType> getTypes() {
		if (types == null) {
			buildAllTypes();
		}
		return types;
	}
}