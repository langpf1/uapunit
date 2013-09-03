package uap.workflow.modeler.sheet.editor;

import java.awt.Dimension;

import nc.ui.wfengine.sheet.util.ConverterRegistry;

/**
 * java.awt.Dimension对象的属性编辑器.<br>
 * 展现格式："width x height"
 * @author 雷军 2004-5-6
 */
public class DimensionPropertyEditor extends StringConverterPropertyEditor {

  protected Object convertFromString(String text) {
    return ConverterRegistry.instance().convert(Dimension.class, text);
  }

}
