package uap.workflow.modeler.sheet.editor;

import nc.ui.wfengine.sheet.ValueTag;

/**
 * չ��ΪComboBox��Boolean�������Ա༭��.<br>
 * @author �׾� 2004-5-6
 */
public class BooleanPropertyEditor extends ComboBoxPropertyEditor {

  public BooleanPropertyEditor() {
    super();
    Object[] values = new Object[] { new ValueTag(Boolean.TRUE, "True"), new ValueTag(Boolean.FALSE, "False") };
    setAvailableValues(values);
  }

}