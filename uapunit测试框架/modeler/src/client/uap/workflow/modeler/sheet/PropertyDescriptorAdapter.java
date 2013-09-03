package uap.workflow.modeler.sheet;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import uap.workflow.modeler.bpmn2.beaninfos.ExtendedGraphBeanDesriptor;


/**
 * ÊôÐÔÃèÊöÆ÷µÄÊÊÅäÆ÷.<br>
 * @author À×¾ü 2004-5-6
 */
public class PropertyDescriptorAdapter extends AbstractProperty {

	private PropertyDescriptor descriptor;

	public PropertyDescriptorAdapter() {
		super();
	}

	public PropertyDescriptorAdapter(PropertyDescriptor descriptor) {
		this();
		setDescriptor(descriptor);
	}

	public void setDescriptor(PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}

	public String getName() {
		return descriptor.getName();
	}

	public String getDisplayName() {
		return descriptor.getDisplayName();
	}

	public String getShortDescription() {
		return descriptor.getShortDescription();
	}

	public Class getType() {
		return descriptor.getPropertyType();
	}

	public void readFromObject(Object object) {
		try {
			setOwnerObject(object);

			Method method = descriptor.getReadMethod();
			if (method != null) {
				setValue(method.invoke(object, null));
			}
		} catch (Exception e) {
			String message = "Got exception when reading property " + getName();
			if (object == null) {
				message += ", object was 'null'";
			} else {
				message += ", object was " + String.valueOf(object);
			}
			throw new RuntimeException(message, e);
		}
	}

	public void writeToObject(Object object) {
		try {
			Method method = descriptor.getWriteMethod();
			if (method != null) {
				method.invoke(object, new Object[] { getValue() });
			}
		} catch (Exception e) {
			String message = "Got exception when writing property " + getName();
			if (object == null) {
				message += ", object was 'null'";
			} else {
				message += ", object was " + String.valueOf(object);
			}
			throw new RuntimeException(message, e);
		}
	}

	public boolean isEditable() {
		return descriptor.getWriteMethod() != null;
	}

	public String getCategory() {
		if (descriptor instanceof ExtendedPropertyDescriptor) {
			return ((ExtendedPropertyDescriptor) descriptor).getCategory();
		} else if (descriptor instanceof ExtendedGraphBeanDesriptor){
			return ((ExtendedGraphBeanDesriptor)descriptor).getCategory();
		} else{
			return null;
		}
	}

}
