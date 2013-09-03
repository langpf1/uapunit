package uap.workflow.modeler.editors;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.utils.BpmnTaskTypeEnum;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;
import nc.uitheme.ui.ImageIconLoader;


public class MethodComboBoxEditor extends DefaultBpmnPropertyEditor {
	public MethodComboBoxEditor() {
		editor = new UIComboBox() {
			public void setSelectedItem(Object anObject) {
				Object oldValue = getSelectedItem().toString();
				super.setSelectedItem(anObject);
				MethodComboBoxEditor.this.firePropertyChange(oldValue, getSelectedItem().toString());
			}
		
		};
		((UIComboBox) editor).setRenderer(getComboxRender());
	}

	public Object getValue() {
		Object selectedItem = ((UIComboBox)editor).getSelectedItem(); 
		if (selectedItem != null){
			if(selectedItem instanceof Method)
			return ((Method)selectedItem).getName();
		}
		return "";
	}

	public void setValue(Object value) {
		super.setValue(value);
//		UIComboBox combo = (UIComboBox) editor;
//		Enum current = null;
//		int index = -1;
//		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
//			current = (Enum)combo.getModel().getElementAt(i);
//			if (value == current || (current != null && current.equals(value))) {
//				index = i;
//				break;
//			}
//		}
//		((UIComboBox) editor).setSelectedIndex(index);
	}

	protected ListCellRenderer getComboxRender() {
		return new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value != null && (value instanceof Method))
						value = ((Method)value).getName();
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//				if (IconURLS() != null && IconURLS().length != 0&&index!=-1) {
//					setIcon(ImageIconLoader.loadImageIconImple(IconURLS()[index]));
//				}
				//return this;
			}
		};
	}
	public DefaultCellRenderer getRender() {
		return new DefaultCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value != null && value instanceof Integer) {
					int index =((Integer) value).intValue();
					if(index!=-1){
//						setText(initTags()[index].toString());
//						if(IconURLS() != null && IconURLS().length != 0){
//							setIcon(ImageIconLoader.loadImageIconImple(IconURLS()[index]));
//						}
					}
					
				}
				return this;
			}
		};
	}
	
	public Component getEditor(){
		return editor;
	}

}
