package uap.workflow.modeler.sheet.editor;

import java.awt.Dimension;

import nc.ui.wfengine.sheet.util.ConverterRegistry;

/**
 * java.awt.Dimension��������Ա༭��.<br>
 * չ�ָ�ʽ��"width x height"
 * @author �׾� 2004-5-6
 */
public class DimensionPropertyEditor extends StringConverterPropertyEditor {

  protected Object convertFromString(String text) {
    return ConverterRegistry.instance().convert(Dimension.class, text);
  }

}
