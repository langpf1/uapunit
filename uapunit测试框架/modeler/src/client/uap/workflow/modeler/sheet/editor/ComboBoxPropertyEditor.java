package uap.workflow.modeler.sheet.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.wfengine.sheet.ValueTag;

/**
 * ComboBox样式的属性编辑器. <br>
 * @author 雷军 2004-5-6
 */
public class ComboBoxPropertyEditor extends AbstractPropertyEditor {

  public ComboBoxPropertyEditor() {
    editor = new UIComboBox() {
      public void setSelectedItem(Object anObject) {
        Object oldValue = getSelectedItem();
        super.setSelectedItem(anObject);
        ComboBoxPropertyEditor.this.firePropertyChange(oldValue, getSelectedItem());
      }
    };
    ((UIComboBox) editor).setRenderer(new Renderer());
  }

  public Object getValue() {
    Object selected = ((UIComboBox) editor).getSelectedItem();
    if (selected instanceof ValueTag) {
      //return ((ValueTag) selected).value;
      return ((ValueTag) selected);
    } else {
      return selected;
    }
  }

  public void setValue(Object value) {
	  UIComboBox combo = (UIComboBox) editor;
    Object current = null;
    int index = -1;
    for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
      current = combo.getModel().getElementAt(i);
      if (value == current || (current != null && current.equals(value))) {
        index = i;
        break;
      }
    }
    ((UIComboBox) editor).setSelectedIndex(index);
  }

  public void setAvailableValues(Object[] values) {
    ((UIComboBox) editor).setModel(new DefaultComboBoxModel(values));
  }

  public static class Renderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
        boolean cellHasFocus) {
      if (value instanceof ValueTag) {
        //lj+
        list.setBackground(Color.white);
        return super.getListCellRendererComponent(list, ((ValueTag) value).getVisualValue(), index, isSelected, cellHasFocus);
      } else {
        //lj+
        list.setBackground(Color.white);
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    }
  }
}