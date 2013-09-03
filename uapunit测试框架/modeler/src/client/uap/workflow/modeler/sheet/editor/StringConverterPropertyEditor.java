package uap.workflow.modeler.sheet.editor;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import nc.ui.wfengine.sheet.swing.LookAndFeelTweaks;
import nc.ui.wfengine.sheet.util.ConverterRegistry;

/**
 * StringConverterPropertyEditor. <br>A comma separated list of values.
 */
public abstract class StringConverterPropertyEditor
  extends AbstractPropertyEditor {

  private Object oldValue;

  public StringConverterPropertyEditor() {
    editor = new JTextField();
    ((JTextField)editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);
  }

  public Object getValue() {
    String text = ((JTextComponent)editor).getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    } else {
      try {
        return convertFromString(text.trim());
      } catch (Exception e) {
        /*UIManager.getLookAndFeel().provideErrorFeedback(editor);*/
        return oldValue;
      }
    }
  }

  public void setValue(Object value) {
    if (value == null) {
      ((JTextComponent)editor).setText("");
    } else {
      oldValue = value;
      ((JTextComponent)editor).setText(convertToString(value));
    }
  }

  protected abstract Object convertFromString(String text);

  protected String convertToString(Object value) {
    return (String)ConverterRegistry.instance().convert(String.class, value);
  }
}
