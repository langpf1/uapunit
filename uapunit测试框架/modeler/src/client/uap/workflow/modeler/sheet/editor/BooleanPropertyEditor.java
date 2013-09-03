package uap.workflow.modeler.sheet.editor;

import nc.ui.wfengine.sheet.ValueTag;

/**
 * 展现为ComboBox的Boolean类型属性编辑器.<br>
 * @author 雷军 2004-5-6
 */
public class BooleanPropertyEditor extends ComboBoxPropertyEditor {

  public BooleanPropertyEditor() {
    super();
    Object[] values = new Object[] { new ValueTag(Boolean.TRUE, "True"), new ValueTag(Boolean.FALSE, "False") };
    setAvailableValues(values);
  }

}