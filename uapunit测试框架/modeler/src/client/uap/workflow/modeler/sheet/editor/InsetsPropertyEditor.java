package uap.workflow.modeler.sheet.editor;


import java.awt.Insets;

import nc.ui.wfengine.sheet.util.ConverterRegistry;

/**
 * InsetsPropertyEditor. <br>
 *  
 */
public class InsetsPropertyEditor extends StringConverterPropertyEditor {

  protected Object convertFromString(String text) {
    return ConverterRegistry.instance().convert(Insets.class, text);
  }
  
}
