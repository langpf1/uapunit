package uap.workflow.modeler.editors;

import uap.workflow.modeler.utils.ProcessTypeEnum;
import nc.ui.pub.beans.UIComboBox;


public class ProcessTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { 
				ProcessTypeEnum.None, 
				ProcessTypeEnum.Private, 
				ProcessTypeEnum.Public};
	}
	
	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return ProcessTypeEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		ProcessTypeEnum[] types = ProcessTypeEnum.class.getEnumConstants();
		for(ProcessTypeEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		ProcessTypeEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (ProcessTypeEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

}
