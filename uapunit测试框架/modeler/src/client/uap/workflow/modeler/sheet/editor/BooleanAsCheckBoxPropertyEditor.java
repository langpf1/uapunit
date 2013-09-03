package uap.workflow.modeler.sheet.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * 展现为CheckBox的Boolean类型属性编辑器. <br>
 * @author 雷军 2004-5-6 
 */
public class BooleanAsCheckBoxPropertyEditor extends AbstractPropertyEditor {

  public BooleanAsCheckBoxPropertyEditor() {
    editor = new JCheckBox();
    ((JCheckBox)editor).setOpaque(false);
    ((JCheckBox)editor).addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        firePropertyChange(
          ((JCheckBox)editor).isSelected() ? Boolean.FALSE : Boolean.TRUE,
          ((JCheckBox)editor).isSelected() ? Boolean.TRUE : Boolean.FALSE);
        ((JCheckBox)editor).transferFocus();
      }
    });
  }

  public Object getValue() {
    return ((JCheckBox)editor).isSelected() ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValue(Object value) {
    ((JCheckBox)editor).setSelected(Boolean.TRUE.equals(value));
  }

}
