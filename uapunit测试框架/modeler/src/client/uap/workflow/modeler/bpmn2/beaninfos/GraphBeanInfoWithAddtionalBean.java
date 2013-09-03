package uap.workflow.modeler.bpmn2.beaninfos;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.MissingResourceException;
import nc.bs.ml.NCLangResOnserver;

public class GraphBeanInfoWithAddtionalBean extends GraphBeanInfo {

	public GraphBeanInfoWithAddtionalBean(Class<?> type) {
		super(type);
	}
	
	private Class<?> existedProperty(String propertyName){
		PropertyDescriptor[] properties = (PropertyDescriptor[])this.getPropertyDescriptors();
		for(int i = 0; i < properties.length; i++){
			if (properties[i].getName().equals(propertyName))
				return properties[i].getPropertyType();
		}
		return null;
	}
	
	protected ExtendedGraphBeanDesriptor addProperty(String propertyName,Class<?> rootType, boolean multiGrade){
		ExtendedGraphBeanDesriptor descriptor = null;
		try {
			if (propertyName == null || propertyName.trim().length() == 0) {
				throw new IntrospectionException("bad property name");
			}

			String[] propertyList = propertyName.split("\\.");
			Class<?> type = rootType;
			Class<?> tempType = null;
			String parentProperty = null;
			String currentProperty = null;
			
			for(int i = 0; i < propertyList.length; i++){
				try {
					currentProperty = propertyList[i];
					if (type == null){
						type = rootType.getDeclaredField(currentProperty).getType();
					}
					tempType = existedProperty(propertyList[i]); 
					if (tempType == null){			//不为空说明属性已经在属性表中
						descriptor = ExtendedGraphBeanDesriptor.newPropertyDescriptor(currentProperty, type);
						String str = currentProperty;//NCLangResOnserver.getInstance().getStrByID("pfworkflow", currentProperty);
						descriptor.setDisplayName(str == null ? currentProperty : str);
						descriptor.setForEdit(false);
						descriptor.setShortDescription(currentProperty);
						//descriptor.setShortDescription(NCLangResOnserver.getInstance().getStrByID("pfworkflow", currentProperty + ".shortDescription"));
						descriptor.setParentProperty(parentProperty);
						type = descriptor.getPropertyType();
						addPropertyDescriptor(descriptor);
					}else{
						type = tempType;
					}
					parentProperty = currentProperty;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			//if (descriptor == null)	//重复增加属性，将舍弃
			descriptor.setForEdit(true);
			return descriptor;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void adjustProperties(Object object){}
	
	protected ExtendedGraphBeanDesriptor addProperty(String propertyName,Class<?> classType){
		ExtendedGraphBeanDesriptor descriptor;
		try {
			if (propertyName == null || propertyName.trim().length() == 0) {
				throw new IntrospectionException("bad property name");
			}

			descriptor = ExtendedGraphBeanDesriptor.newPropertyDescriptor(propertyName, classType);
			try {
				String str = NCLangResOnserver.getInstance().getStrByID("pfworkflow", propertyName);
				descriptor.setDisplayName(str == null ? propertyName : str);
			} catch (MissingResourceException e) {
			}
			try {
				descriptor.setShortDescription(NCLangResOnserver.getInstance().getStrByID("pfworkflow", propertyName + ".shortDescription"));
			} catch (MissingResourceException e) {
			}
			addPropertyDescriptor(descriptor);
			return descriptor;
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
	
//	protected ExtendedGraphBeanDesriptor addProperty(String propertyName,Class classType){
//		if(getAdditionalBeanInfo()==null)
//			return null;
//		boolean isAddtionalBean =false;
//		for(BeanInfo beanInfo: getAdditionalBeanInfo()){
//			if(((GraphBeanInfo)beanInfo).getType().toString().equals(classType.toString())){
//				isAddtionalBean =true;
//				break;
//			}
//				
//		}
//		
//		
//		if(!isAddtionalBean)
//			return null;
//		
//		ExtendedGraphBeanDesriptor descriptor;
//		try {
//			if (propertyName == null || propertyName.trim().length() == 0) {
//				throw new IntrospectionException("bad property name");
//			}
//
//			descriptor = ExtendedGraphBeanDesriptor.newPropertyDescriptor(propertyName, classType);
//			try {
//				String str = NCLangResOnserver.getInstance().getStrByID("pfworkflow", propertyName);
//				descriptor.setDisplayName(str == null ? propertyName : str);
//			} catch (MissingResourceException e) {
//			}
//			try {
//				descriptor.setShortDescription(NCLangResOnserver.getInstance().getStrByID("pfworkflow", propertyName + ".shortDescription"));
//			} catch (MissingResourceException e) {
//			}
//			addPropertyDescriptor(descriptor);
//			return descriptor;
//		} catch (IntrospectionException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	

}
