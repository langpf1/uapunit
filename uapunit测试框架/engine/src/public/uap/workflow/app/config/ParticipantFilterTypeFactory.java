package uap.workflow.app.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.app.participant.IParticipantFilterType;

/**
 * 参与者限定模式工厂类 ParticipantFilterTypeFactory与ParticipantTypeFactory通过泛型合一
 * 
 * @author
 */
public class ParticipantFilterTypeFactory {
	private Map<String, IParticipantFilterType> filterTypes;
	private Map<String, ParticipantFilterTypeConfig> filterTypeconfigMaps;
	private List<ParticipantFilterTypeConfig> filterTypeConfigs;
	private static ParticipantFilterTypeFactory inst = new ParticipantFilterTypeFactory();

	private ParticipantFilterTypeFactory() {
	}

	public static ParticipantFilterTypeFactory getInstance() {
		return inst;
	}

	private List<ParticipantFilterTypeConfig> getFilterTypeConfigs(String configPath) {
		if (this.filterTypeConfigs == null) {
			BusinessExtendConfig config = BusinessExtendConfigParser.getInstance().getBusinessExtendConfig(configPath);
			filterTypeConfigs = config.getParticipantFilterTypes();
		}
		return filterTypeConfigs;
	}

	@SuppressWarnings("unchecked")
	private void buildAllFilterTypes() {
		String configPath = "uap/workflow/app/config/BizAdapter.xml";
		List<ParticipantFilterTypeConfig> filterTypeConfigs = null;
		filterTypeConfigs = getFilterTypeConfigs(configPath);
		if (filterTypeConfigs == null) {
			return;
		}
		filterTypes = new HashMap<String, IParticipantFilterType>();
		filterTypeconfigMaps = new HashMap<String, ParticipantFilterTypeConfig>();
		for (ParticipantFilterTypeConfig filterTypeConfig : filterTypeConfigs) {
			try {
				Class<IParticipantFilterType> clazz;
				clazz = (Class<IParticipantFilterType>) Class.forName(filterTypeConfig.getClassName());
				IParticipantFilterType filterType = clazz.newInstance();
				filterTypes.put(filterTypeConfig.getCode(), filterType);
				filterTypeconfigMaps.put(filterTypeConfig.getCode(), filterTypeConfig);
			} catch (ClassNotFoundException e) {
				throw new ParseConfigException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new ParseConfigException(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				throw new ParseConfigException(e.getMessage(), e);
			}
		}
	}

	public String getImpl(String typeCode) {
		if (filterTypeconfigMaps == null) {
			buildAllFilterTypes();
		}
		ParticipantFilterTypeConfig filterTypeConfig = filterTypeconfigMaps.get(typeCode);
		return filterTypeConfig.getImplClassName();
	}

	public IParticipantFilterType getFilterType(String name) {
		if (filterTypes == null) {
			buildAllFilterTypes();
		}
		return filterTypes.get(name);
	}

	public Map<String, IParticipantFilterType> getFilterTypes() {
		if (filterTypes == null) {
			buildAllFilterTypes();
		}
		return filterTypes;
	}
}