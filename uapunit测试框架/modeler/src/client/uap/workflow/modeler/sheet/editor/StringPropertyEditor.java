package uap.workflow.modeler.sheet.editor;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import nc.ui.wfengine.sheet.swing.LookAndFeelTweaks;

/**
 * StringPropertyEditor.<br>
 *
 */
public class StringPropertyEditor extends AbstractPropertyEditor {

  public StringPropertyEditor() {
    editor = new JTextField();
    ((JTextField)editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);
  }

  public Object getValue() {
    return ((JTextComponent)editor).getText();
  }

  public void setValue(Object value) {
    if (value == null) {
      ((JTextComponent)editor).setText("");
    } else {
      ((JTextComponent)editor).setText(String.valueOf(value));
    }
  }

}
