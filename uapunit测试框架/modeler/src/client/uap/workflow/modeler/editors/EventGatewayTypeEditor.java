package uap.workflow.modeler.editors;

import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.utils.EventGatewayTypeEnum;
import nc.ui.pub.beans.UIComboBox;


public class EventGatewayTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() { 
		return new Object[] { 
				EventGatewayTypeEnum.Exclusive,
				EventGatewayTypeEnum.Parallel
				};
	}

	protected String[] IconURLS() {
		return new String[] { 
				BpmnCellLib.ICON_PALETTE_EXCLUSIVEGATEWAY, 
				BpmnCellLib.ICON_PALETTE_PARALLELGATEWAY};
	};

	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return EventGatewayTypeEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		EventGatewayTypeEnum[] types = EventGatewayTypeEnum.class.getEnumConstants();
		for(EventGatewayTypeEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		EventGatewayTypeEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (EventGatewayTypeEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}
}
