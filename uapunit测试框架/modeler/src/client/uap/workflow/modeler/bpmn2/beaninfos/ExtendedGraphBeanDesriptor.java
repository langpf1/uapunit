package uap.workflow.modeler.bpmn2.beaninfos;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;

import nc.bs.logging.Logger;

/**
 * @author chengsc
 * 
 */
public class ExtendedGraphBeanDesriptor extends PropertyDescriptor {

	private String category = "";

	private boolean forEdit = true;
	
	private String parentProperty = null;
	
	public boolean isForEdit() {
		return forEdit;
	}

	public void setForEdit(boolean forEdit) {
		this.forEdit = forEdit;
	}

	public ExtendedGraphBeanDesriptor(String propertyName, Class<?> beanClass) throws IntrospectionException {
		super(propertyName, beanClass);
	}

	public ExtendedGraphBeanDesriptor(String propertyName, Method getter, Method setter) throws IntrospectionException {
		super(propertyName, getter, setter);
	}

	public ExtendedGraphBeanDesriptor(String propertyName, Class<?> beanClass, String getterName, String setterName) throws IntrospectionException {
		super(propertyName, beanClass, getterName, setterName);
	}

	/**
	 * Sets this property category
	 * 
	 * @param category
	 * @return this property for chaining calls.
	 */
	public ExtendedGraphBeanDesriptor setCategory(String category) {
		this.category = category;
		return this;
	}

	/**
	 * @return the category in which this property belongs
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Force this property to be readonly
	 * 
	 * @return this property for chaining calls.
	 */
	public ExtendedGraphBeanDesriptor setReadOnly() {
		try {
			setWriteMethod(null);
		} catch (IntrospectionException e) {
			Logger.error(e.getMessage(),e);
		}
		return this;
	}

	private static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		} else {
			char chars[] = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return String.valueOf(chars);
		}
	}

	public static Method getReadMethod(Class<?> clazz, String propertyName) {
		Method readMethod = null;
		String base = capitalize(propertyName);

		// Since there can be multiple setter methods but only one getter
		// method, find the getter method first so that you know what the
		// property type is. For booleans, there can be "is" and "get"
		// methods. If an "is" method exists, this is the official
		// reader method so look for this one first.
		try {
			readMethod = clazz.getMethod("is" + base, (Class<?>[])null);
		} catch (Exception getterExc) {
			try {
				// no "is" method, so look for a "get" method.
				readMethod = clazz.getMethod("get" + base, (Class<?>[])null);
			} catch (Exception e) {
				// no is and no get, we will return null
			}
		}

		return readMethod;
	}

	/**
	 * 获得某类的“写”属性方法
	 * 
	 * @param clazz
	 *            类
	 * @param propertyName
	 *            属性名称
	 * @param propertyType
	 *            属性类型
	 * @return
	 */
	public static Method getWriteMethod(Class<?> clazz, String propertyName, Class<?> propertyType) {
		Method writeMethod = null;
		String base = capitalize(propertyName);

		Class<?> params[] = { propertyType };
		try {
			writeMethod = clazz.getMethod("set" + base, params);
		} catch (Exception e) {
			// no write method
		}

		return writeMethod;
	}

	public static ExtendedGraphBeanDesriptor newPropertyDescriptor(String propertyName, Class<?> beanClass) throws IntrospectionException {
		// the same initialization phase as in the PropertyDescriptor
		Method readMethod = getReadMethod(beanClass, propertyName);
		Method writeMethod = null;

		if (readMethod == null) {
			throw new IntrospectionException("No getter for property " + propertyName + " in class " + beanClass.getName());
		}

		writeMethod = getWriteMethod(beanClass, propertyName, readMethod.getReturnType());

		return new ExtendedGraphBeanDesriptor(propertyName, readMethod, writeMethod);
	}

	public void setParentProperty(String parentProperty) {
		this.parentProperty = parentProperty;
	}

	public String getParentProperty() {
		return parentProperty;
	}

	public static final Comparator<?> BY_CATEGORY_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			PropertyDescriptor desc1 = (PropertyDescriptor) o1;
			PropertyDescriptor desc2 = (PropertyDescriptor) o2;

			if (desc1 == null && desc2 == null) {
				return 0;
			} else if (desc1 != null && desc2 == null) {
				return 1;
			} else if (desc1 == null && desc2 != null) {
				return -1;
			} else {
				if (desc1 instanceof ExtendedGraphBeanDesriptor && !(desc2 instanceof ExtendedGraphBeanDesriptor)) {
					return -1;
				} else if (!(desc1 instanceof ExtendedGraphBeanDesriptor) && desc2 instanceof ExtendedGraphBeanDesriptor) {
					return 1;
				} else if (!(desc1 instanceof ExtendedGraphBeanDesriptor) && !(desc2 instanceof ExtendedGraphBeanDesriptor)) {
					return String.CASE_INSENSITIVE_ORDER.compare(desc1.getDisplayName(), desc2.getDisplayName());
				} else {
					int category = String.CASE_INSENSITIVE_ORDER.compare(((ExtendedGraphBeanDesriptor) desc1).getCategory() == null ? "" : ((ExtendedGraphBeanDesriptor) desc1).getCategory(),
							((ExtendedGraphBeanDesriptor) desc2).getCategory() == null ? "" : ((ExtendedGraphBeanDesriptor) desc2).getCategory());
					if (category == 0) {
						return String.CASE_INSENSITIVE_ORDER.compare(desc1.getDisplayName(), desc2.getDisplayName());
					} else {
						return category;
					}
				}
			}
		}
	};
}
