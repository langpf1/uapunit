package uap.workflow.modeler.editors;

import uap.workflow.modeler.utils.RejectTypeEnum;
import nc.ui.pub.beans.UIComboBox;


public class RejectTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { 
				RejectTypeEnum.LastStep, 
				RejectTypeEnum.BillMaker, 
				RejectTypeEnum.AllActivity,
				RejectTypeEnum.SpecifiedActivity};
	}
	
	@Override
	public Object getValue() {
		Object obj = super.getValue();
		
		return RejectTypeEnum.fromIntValue((Integer)obj).toString();
	}
	
	@Override
	public void setValue(Object value) {
		int intValue = 0;
		RejectTypeEnum[] types = RejectTypeEnum.class.getEnumConstants();
		for(RejectTypeEnum type : types){
			if (type.toString().equals(value)){
				intValue = type.getIntValue();
				break;
			}
		}

		UIComboBox combo = (UIComboBox) editor;
		RejectTypeEnum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (RejectTypeEnum)combo.getModel().getElementAt(i);
			if (intValue == current.getIntValue()) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

}
