package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.modeler.core.IBeanInfoProvider;
import uap.workflow.modeler.utils.CreateElementUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseElement")
public class BaseElement implements Serializable, IBeanInfoProvider, IUserObjectClone {
	private static final long serialVersionUID = 7490616908676980551L;
	@XmlAttribute
	public String id;
	@XmlElement(name = "documentation")
	@PropEditor("uap.workflow.modeler.editors.DocumentEditor")	
	public String documentation ;	
	@XmlTransient
	protected boolean isContainer = false;
	@XmlTransient
	protected boolean canBound = false;
	@XmlElement(name = "extendProperties")
	@PropEditor("uap.workflow.modeler.editors.ExtendEditor")	
	public String extendProperties ;
	/*@PropEditor("uap.workflow.modeler.editors.ExtendPropertyEditor")
	@XmlTransient
	public List<FormProperty> extendProperties = new ArrayList<FormProperty>();*/
	/*@TypeChangeMonitor("fillColor")
	@XmlAttribute
	public String fillColor="";
	@TypeChangeMonitor("font")
	@XmlAttribute
	public String font;
	
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String getFillColor() {
		return fillColor;
	}
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}*/
	
	
	public String getExtendProperties() {
		return extendProperties;
	}
	public void setExtendProperties(String extendProperties) {
		this.extendProperties = extendProperties;
	}
	public boolean isCanBound() {
		return canBound;
	}
	public void setCanBound(boolean canBound) {
		this.canBound = canBound;
	}
	public boolean isContainer() {
		return isContainer;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String provideBeanInfoClass() {
		return null;
	}	
	
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	/**
	 * 注意：浅拷贝是允许的。但elmenet的id要重新生成 因为 目前clone 仅仅发生在新增元素时候，此时引用型的变量均未赋值为null。
	 * */
	@Override
	public Object replicate() {
		try {
			BaseElement cloneObj = (BaseElement) Class.forName(getClass().getName()).newInstance();
			Method[] methods = getClass().getMethods();
			for (Method setMethod : methods) {
				if (setMethod.getName().startsWith("set")) {
					if (setMethod.getParameterTypes()[0].equals(List.class)) 
						continue;
					Object value = null;
					// id重新生成
					if (setMethod.getName().equals("setId")) {
						if(cloneObj instanceof Process){
							value = java.util.UUID.randomUUID().toString();
						}else{
							value = new CreateElementUtils().generateElementId(cloneObj);
						}
					} else {
						String getMethodName = "get" + setMethod.getName().substring(3, setMethod.getName().length());
						if (setMethod.getParameterTypes()[0].getName().equals("boolean")) {
							getMethodName = "is" + setMethod.getName().substring(3, setMethod.getName().length());
						}
						for (Method getMethod : methods) {
							if (getMethod.getName().equals(getMethodName)) {
								value = getMethod.invoke(this, new Object[] {});
								break;
							}
						}
					}
					setMethod.invoke(cloneObj, value);
				}
			}
			return cloneObj;
		} catch (Exception e) {
			return null;
		}
	}
}