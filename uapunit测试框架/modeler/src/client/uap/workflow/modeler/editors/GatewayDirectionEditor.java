package uap.workflow.modeler.editors;

import uap.workflow.modeler.utils.GatewayDirectionEnum;
import nc.ui.pub.beans.UIComboBox;

public class GatewayDirectionEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { GatewayDirectionEnum.Unspecified, 
				GatewayDirectionEnum.Converging,
				GatewayDirectionEnum.Diverging,
				GatewayDirectionEnum.Mixed};
	}
	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return GatewayDirectionEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		GatewayDirectionEnum[] types = GatewayDirectionEnum.class.getEnumConstants();
		for(GatewayDirectionEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		GatewayDirectionEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (GatewayDirectionEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

}
