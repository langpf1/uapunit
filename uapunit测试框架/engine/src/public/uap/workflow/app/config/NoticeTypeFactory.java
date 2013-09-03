package uap.workflow.app.config;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.app.notice.INoticeType;
import uap.workflow.engine.logger.WorkflowLogger;
/**
 * 通知工厂类 NoticeTypeFactory,ParticipantFilterTypeFactory与ParticipantTypeFactory通过泛型合一
 * 
 * @author
 */
public class NoticeTypeFactory {
	private Map<String, INoticeType> types;
	private Map<String, NoticeTypeConfig> typeConfigMap;
	private List<NoticeTypeConfig> typeConfigs;
	
	private static NoticeTypeFactory inst = new NoticeTypeFactory();
	private NoticeTypeFactory() {}
	public static NoticeTypeFactory getInstance() {
		return inst;
	}
	private List<NoticeTypeConfig> getTypeConfigs(String configPath) {
		if (typeConfigs == null) {
			BusinessExtendConfig config = BusinessExtendConfigParser.getInstance().getBusinessExtendConfig(configPath);
			typeConfigs = config.getNoticeTypes();
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
		types = new HashMap<String, INoticeType>();
		typeConfigMap = new HashMap<String, NoticeTypeConfig>();
		for (NoticeTypeConfig typeConfig : typeConfigs) {
			try {
				Class<INoticeType> clazz = (Class<INoticeType>) Class.forName(typeConfig.getClassName());
				INoticeType type = clazz.newInstance();
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
		NoticeTypeConfig typeConfig = typeConfigMap.get(typeCode);
		return typeConfig.getImplClassName();
	}
	public INoticeType getType(String name) {
		if (types == null) {
			buildAllTypes();
		}
		return types.get(name);
	}
	public Map<String, INoticeType> getTypes() {
		if (types == null) {
			buildAllTypes();
		}
		return types;
	}
}