package uap.workflow.modeler.bpmn2.beaninfos;

import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.cfg.ExtensionPropertyConfig;
import uap.workflow.engine.itf.IExtensionConfig;
import uap.workflow.modeler.utils.BpmnModelerConstants;


public class BaseElementBeanInfo extends GraphBeanInfoWithAddtionalBean {

	public BaseElementBeanInfo(Class<?> type) {
		super(type);
		addProperty("id").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);//.setReadOnly();
		addProperty("documentation").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);
			
		IExtensionConfig config = NCLocator.getInstance().lookup(IExtensionConfig.class);
		ExtensionPropertyConfig[] properties = config.getExtensionPropertyConfig();

		for(ExtensionPropertyConfig property : properties){
			if (property.getNotationType().equalsIgnoreCase(type.getSimpleName())){
				addProperty("extendProperties").setCategory(BpmnModelerConstants.CATEGORY_GENERAL);
			}
		}
		//addProperty("fillColor").setCategory(BpmnModelerConstants.CATEGORY_STYLE);
		//addProperty("font").setCategory(BpmnModelerConstants.CATEGORY_STYLE);
	}

}
