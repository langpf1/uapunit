package uap.workflow.modeler.editors;

import uap.workflow.modeler.utils.OpenUIStyleEnum;
import nc.ui.pub.beans.UIComboBox;


public class OpenUIStyleEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { 
				OpenUIStyleEnum.BisunessUI, 
				OpenUIStyleEnum.ApproveUI, 
				OpenUIStyleEnum.DefinedUI,
				OpenUIStyleEnum.CustomURI};
	}
	
	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return OpenUIStyleEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		OpenUIStyleEnum[] types = OpenUIStyleEnum.class.getEnumConstants();
		for(OpenUIStyleEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		OpenUIStyleEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (OpenUIStyleEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

}
