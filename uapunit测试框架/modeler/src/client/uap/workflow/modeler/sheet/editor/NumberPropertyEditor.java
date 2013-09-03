package uap.workflow.modeler.sheet.editor;

import java.text.ParseException;

import javax.swing.JFormattedTextField;

import nc.ui.pub.desktop.NumberOnlyTextField;
import nc.ui.wfengine.sheet.swing.LookAndFeelTweaks;

/**
 * Number类型的属性编辑器父类. <br>
 * 包括：Double/Long/Float/Integer/Short
 * 
 * @author 雷军 2004-5-6
 */
public class NumberPropertyEditor extends AbstractPropertyEditor {

	private Class type;

	public NumberPropertyEditor(Class type) {
		if (!Number.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException("type must be a subclass of Number");
		}

		editor = new NumberOnlyTextField();
		this.type = type;		
		((JFormattedTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);
	}

	public Object getValue() {
		try {
			((JFormattedTextField) editor).commitEdit();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Number number = (Number) ((JFormattedTextField) editor).getValue();
		if (Double.class.equals(type)) {
			return Double.valueOf(number.doubleValue());
		} else if (Float.class.equals(type)) {
			return Float.valueOf(number.floatValue());
		} else if (Integer.class.equals(type)) {
			return Integer.valueOf(number.intValue());
		} else if (Long.class.equals(type)) {
			return Long.valueOf(number.longValue());
		} else if (Short.class.equals(type)) {
			return Short.valueOf(number.shortValue());
		} else {
			// never happen
			return null;
		}
	}

	public void setValue(Object value) {
		if (value == null) {
			((JFormattedTextField) editor).setValue(getDefaultValue());
		} else if (value instanceof Number) {
			((JFormattedTextField) editor).setValue(value);
		} else {
			((JFormattedTextField) editor).setValue(getDefaultValue());
		}
	}

	private Object getDefaultValue() {
		try {
			return type.getConstructor(new Class[] { String.class }).newInstance(new Object[] { "0" });
		} catch (Exception e) {
			// will not happen
			throw new RuntimeException(e);
		}
	}

}
