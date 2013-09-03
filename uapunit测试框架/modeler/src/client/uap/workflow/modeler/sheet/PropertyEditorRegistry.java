package uap.workflow.modeler.sheet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyEditor;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.wfengine.sheet.editor.AbstractPropertyEditor;
import nc.ui.wfengine.sheet.editor.BooleanAsCheckBoxPropertyEditor;
import nc.ui.wfengine.sheet.editor.ColorPropertyEditor;
import nc.ui.wfengine.sheet.editor.DimensionPropertyEditor;
import nc.ui.wfengine.sheet.editor.DoublePropertyEditor;
import nc.ui.wfengine.sheet.editor.FilePropertyEditor;
import nc.ui.wfengine.sheet.editor.FloatPropertyEditor;
import nc.ui.wfengine.sheet.editor.FontPropertyEditor;
import nc.ui.wfengine.sheet.editor.InsetsPropertyEditor;
import nc.ui.wfengine.sheet.editor.IntegerPropertyEditor;
import nc.ui.wfengine.sheet.editor.LongPropertyEditor;
import nc.ui.wfengine.sheet.editor.RectanglePropertyEditor;
import nc.ui.wfengine.sheet.editor.ShortPropertyEditor;
import nc.ui.wfengine.sheet.editor.StringPropertyEditor;
import nc.ui.wfengine.sheet.editor.UFBooleanPropertyEditor;
import nc.vo.pub.lang.UFBoolean;

/**
 * 属性/属性类型与Editor之间的映射注册
 * 
 * @author 雷军 2004-5-6
 */
public class PropertyEditorRegistry {

	private Map typeToEditor;

	private Map propertyToEditor;

	public PropertyEditorRegistry() {
		typeToEditor = new HashMap();
		propertyToEditor = new HashMap();
		registerDefaults();
	}

	/**
	 * Gets an editor for the given property. The lookup is as follow:
	 * <ul>
	 * <li>if an editor was registered with
	 * {@link #registerEditor(Property, AbstractPropertyEditor)}, it is
	 * returned, else</li>
	 * <li>if an editor class was registered with
	 * {@link #registerEditor(Property, Class)}, it is returned, else
	 * <li>
	 * <li>look for editor for the property type using {@link #getEditor(Class)}
	 * .</li>
	 * </ul>
	 * 
	 * @param property
	 * @return an editor suitable for the Property.
	 */
	public synchronized PropertyEditor getEditor(Property property) {
		PropertyEditor editor = null;
		Object value = propertyToEditor.get(property);
		if (value instanceof PropertyEditor) {
			editor = (PropertyEditor) value;
		} else if (value instanceof Class) {
			try {
				editor = (AbstractPropertyEditor) ((Class) value).newInstance();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		} else {
			editor = getEditor(property.getType());
		}

		// XXX:leijun+2009-6
		if (editor != null&&editor instanceof AbstractPropertyEditor)
			((AbstractPropertyEditor)editor).setOwnerObject(property.getOwnerObject());

		return editor;
	}

	/**
	 * Gets an editor for the given property type. The lookup is as follow:
	 * <ul>
	 * <li>if an editor was registered with
	 * {@link #registerEditor(Class, AbstractPropertyEditor)}, it is returned,
	 * else</li>
	 * <li>if an editor class was registered with
	 * {@link #registerEditor(Class, Class)}, it is returned, else
	 * <li>
	 * <li>it returns null.</li>
	 * </ul>
	 * 
	 * @param type
	 * @return an editor suitable for the Property type or null if none found
	 */
	private synchronized AbstractPropertyEditor getEditor(Class type) {
		AbstractPropertyEditor editor = null;
		Object value = typeToEditor.get(type);
		if (value instanceof AbstractPropertyEditor) {
			editor = (AbstractPropertyEditor) value;
		} else if (value instanceof Class) {
			try {
				editor = (AbstractPropertyEditor) ((Class) value).newInstance();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return editor;
	}

	public synchronized void registerEditor(Class type, Class editorClass) {
		typeToEditor.put(type, editorClass);
	}

	public synchronized void registerEditor(Class type,
			AbstractPropertyEditor editor) {
		typeToEditor.put(type, editor);
	}

	// public synchronized void unregisterEditor(Class type) {
	// typeToEditor.remove(type);
	// }

	public synchronized void registerEditor(Property property, Class editorClass) {
		propertyToEditor.put(property, editorClass);
	}

	public synchronized void registerEditor(Property property,
			PropertyEditor editor) {
		propertyToEditor.put(property, editor);
	}

	// public synchronized void unregisterEditor(Property property) {
	// propertyToEditor.remove(property);
	// }

	/**
	 * Adds default editors. This method is called by the constructor but may be
	 * called later to reset any customizations made through the
	 * <code>registerEditor</code> methods. <b>Note: if overriden,
	 * <code>super.registerDefaults()</code> must be called before plugging
	 * custom defaults. </b>
	 */
	public void registerDefaults() {
		typeToEditor.clear();
		propertyToEditor.clear();

		// our editors
		registerEditor(String.class, StringPropertyEditor.class);

		registerEditor(double.class, DoublePropertyEditor.class);
		registerEditor(Double.class, DoublePropertyEditor.class);

		registerEditor(float.class, FloatPropertyEditor.class);
		registerEditor(Float.class, FloatPropertyEditor.class);

		registerEditor(int.class, IntegerPropertyEditor.class);
		registerEditor(Integer.class, IntegerPropertyEditor.class);

		registerEditor(long.class, LongPropertyEditor.class);
		registerEditor(Long.class, LongPropertyEditor.class);

		registerEditor(short.class, ShortPropertyEditor.class);
		registerEditor(Short.class, ShortPropertyEditor.class);

		registerEditor(boolean.class, BooleanAsCheckBoxPropertyEditor.class);
		registerEditor(Boolean.class, BooleanAsCheckBoxPropertyEditor.class);
		registerEditor(UFBoolean.class, UFBooleanPropertyEditor.class);
		registerEditor(File.class, FilePropertyEditor.class);

		// awt object editors
		registerEditor(Color.class, ColorPropertyEditor.class);
		registerEditor(Dimension.class, DimensionPropertyEditor.class);
		registerEditor(Insets.class, InsetsPropertyEditor.class);
		registerEditor(Font.class, FontPropertyEditor.class);
		registerEditor(Rectangle.class, RectanglePropertyEditor.class);
	}
}
