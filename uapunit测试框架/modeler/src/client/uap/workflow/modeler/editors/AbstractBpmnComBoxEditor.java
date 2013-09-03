package uap.workflow.modeler.editors;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import uap.workflow.modeler.utils.BpmnTaskTypeEnum;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.wfengine.sheet.ValueTag;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;
import nc.uitheme.ui.ImageIconLoader;


public abstract class AbstractBpmnComBoxEditor extends DefaultBpmnPropertyEditor implements ICustomRendererEdit{

	public AbstractBpmnComBoxEditor() {
		editor = new UIComboBox() {
			public void setSelectedItem(Object anObject) {
				Object oldValue = getSelectedItem();
				super.setSelectedItem(anObject);
				AbstractBpmnComBoxEditor.this.firePropertyChange(oldValue, getSelectedItem());
			}
		};
		setAvailableValues();
		((UIComboBox) editor).setRenderer(getComboxRender());
	}

	public Object getValue() {
		return ((UIComboBox) editor).getSelectedIndex();
	}

	public void setValue(Object value) {
		super.setValue(value);
		UIComboBox combo = (UIComboBox) editor;
		Enum current = null;
		int index = -1;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = (Enum)combo.getModel().getElementAt(i);
			if (value == current || (current != null && current.equals(value))) {
				index = i;
				break;
			}
		}
		((UIComboBox) editor).setSelectedIndex(index);
	}

	private void setAvailableValues() {
		((UIComboBox) editor).setModel(new DefaultComboBoxModel(initTags()));
	}

	// ×ÓÀàÖØÐ´
	protected abstract Object[] initTags();

	protected String[] IconURLS() {
		return null;
	};

	protected ListCellRenderer getComboxRender() {
		return new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (IconURLS() != null && IconURLS().length != 0&&index!=-1) {
					setIcon(ImageIconLoader.loadImageIconImple(IconURLS()[index]));
				}
				return this;
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
						setText(initTags()[index].toString());
						if(IconURLS() != null && IconURLS().length != 0){
							setIcon(ImageIconLoader.loadImageIconImple(IconURLS()[index]));
						}
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
