package uap.workflow.modeler.editors;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIComboBox;
import uap.workflow.engine.bpmn.behavior.ExtensionServiceConfig;
import uap.workflow.engine.itf.IExtensionConfig;
import uap.workflow.modeler.utils.ServiceImplementTypeEnum;


public class ServiceImplementTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		IExtensionConfig extensionService = NCLocator.getInstance().lookup(IExtensionConfig.class);
		ExtensionServiceConfig[] services = extensionService.getExtensionServices();
		List<String> implTypes = new ArrayList<String>();
		implTypes.add(ServiceImplementTypeEnum.Standard.toString());
		implTypes.add(ServiceImplementTypeEnum.WebService.toString());
		implTypes.add(ServiceImplementTypeEnum.Expression.toString());
		implTypes.add(ServiceImplementTypeEnum.DelegateExpression.toString());
		implTypes.add(ServiceImplementTypeEnum.CallMethod.toString());
		for(ExtensionServiceConfig service : services ){
			implTypes.add(service.getServiceName());
		}
		return implTypes.toArray(new String[0]);
	}

	@Override
	public Object getValue() {
		Object obj = super.getValue();
		return ((UIComboBox)editor).getModel().getSelectedItem();
		//return ServiceImplementTypeEnum.fromIntValue((Integer)obj).toString();
	}

	@Override
	public void setValue(Object value) {
		return;
/*		int intValue = 0;
		ServiceImplementTypeEnum[] types = ServiceImplementTypeEnum.class.getEnumConstants();
		for(ServiceImplementTypeEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		ServiceImplementTypeEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (ServiceImplementTypeEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
*/	}
}
