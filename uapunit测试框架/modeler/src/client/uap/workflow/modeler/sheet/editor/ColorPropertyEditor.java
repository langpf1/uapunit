package uap.workflow.modeler.sheet.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import nc.ui.wfengine.sheet.swing.ColorCellRenderer;

/**
 * Color类型的属性编辑器. <br>
 * @author 雷军 2004-5-6
 */
public class ColorPropertyEditor extends AbstractPropertyEditor {

  private ColorCellRenderer label;
  private JButton button;
  private Color color;

  public ColorPropertyEditor() {
    editor = new JPanel(new BorderLayout(0, 0));
    ((JPanel) editor).add("Center", label = new ColorCellRenderer());
    label.setOpaque(false);
    ((JPanel) editor).add("East", button = new JButton(". . ."));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectColor();
      }
    });
    ((JPanel) editor).setOpaque(false);
  }

  public Object getValue() {
    return color;
  }

  public void setValue(Object value) {
    color = (Color) value;
    label.setValue(color);
  }

  protected void selectColor() {
    String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("101203","UPP101203-000116")/*@res "颜色选择"*/;
    Color selectedColor = JColorChooser.showDialog(editor, title, color);

    if (selectedColor != null) {
      Color oldColor = color;
      Color newColor = selectedColor;
      label.setValue(newColor);
      color = newColor;
      firePropertyChange(oldColor, newColor);
    }
  }

}