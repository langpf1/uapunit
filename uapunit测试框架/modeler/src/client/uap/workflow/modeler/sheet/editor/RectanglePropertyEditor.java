package uap.workflow.modeler.sheet.editor;

import java.awt.Rectangle;

import nc.ui.wfengine.sheet.util.ConverterRegistry;

/**
 * Rectangle�����Ա༭��. <br>
 * @author �׾� 2004-5-6 
 */
public class RectanglePropertyEditor extends StringConverterPropertyEditor {

  protected Object convertFromString(String text) {
    return ConverterRegistry.instance().convert(Rectangle.class, text);
  }

}
