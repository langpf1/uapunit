package uap.workflow.modeler.editors;

import nc.ui.pub.beans.UIComboBox;
import uap.workflow.modeler.utils.ScriptLanguageTypeEnum;


public class ScriptLanguageTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { 
				ScriptLanguageTypeEnum.javascript, 
				ScriptLanguageTypeEnum.groovy,
				ScriptLanguageTypeEnum.juel};
	}
	
	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return ScriptLanguageTypeEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		ScriptLanguageTypeEnum[] types = ScriptLanguageTypeEnum.class.getEnumConstants();
		for(ScriptLanguageTypeEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		ScriptLanguageTypeEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (ScriptLanguageTypeEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

}
