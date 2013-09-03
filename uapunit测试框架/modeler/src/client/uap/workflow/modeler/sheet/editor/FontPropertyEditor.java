package uap.workflow.modeler.sheet.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;
import nc.ui.wfengine.sheet.swing.JFontChooser;

/**
 * FontPropertyEditor.<br>
 *
 */
public class FontPropertyEditor extends AbstractPropertyEditor {

  private DefaultCellRenderer label;
  private JButton button;
  private Font font;

  public FontPropertyEditor() {
    editor = new JPanel(new BorderLayout(0, 0));
    ((JPanel) editor).add("Center", label = new DefaultCellRenderer());
    label.setOpaque(false);
    ((JPanel) editor).add("East", button = new JButton(". . ."));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectFont();
      }
    });
    ((JPanel) editor).setOpaque(false);
  }

  public Object getValue() {
    return font;
  }

  public void setValue(Object value) {
    font = (Font) value;
    label.setValue(value);
  }

  protected void selectFont() {
    String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("101203","UPP101203-000118")/*@res "×ÖÌåÑ¡Ôñ"*/;

    Font selectedFont = JFontChooser.showDialog(editor, title, font);

    if (selectedFont != null) {
      Font oldFont = font;
      Font newFont = selectedFont;
      label.setValue(newFont);
      font = newFont;
      firePropertyChange(oldFont, newFont);
    }
  }

}