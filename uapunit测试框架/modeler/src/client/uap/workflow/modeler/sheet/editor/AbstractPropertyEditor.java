package uap.workflow.modeler.sheet.editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

/**
 * 抽象的属性编辑器. <br>
 * @author 雷军 2004-5-6 
 * @modifier leijun 2009-6 增加属性的宿主对象
 */
public class AbstractPropertyEditor implements PropertyEditor {

	protected Component editor;

	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	//XXX:leijun+2009-6 该属性的宿主对象
	private Object ownerObject; 
	
	private String propName;

	public boolean isPaintable() {
		return false;
	}

	public boolean supportsCustomEditor() {
		return false;
	}

	public Component getCustomEditor() {
		return editor;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(Object oldValue, Object newValue) {
		listeners.firePropertyChange("value", oldValue, newValue);
	}

	public Object getValue() {
		return null;
	}

	public void setValue(Object value) {
	}

	public String getAsText() {
		return null;
	}

	public String getJavaInitializationString() {
		return null;
	}

	public String[] getTags() {
		return null;
	}

	public void setAsText(String text) throws IllegalArgumentException {
	}

	public void paintValue(Graphics gfx, Rectangle box) {
	}

	public void setOwnerObject(Object ownerObject) {
		this.ownerObject = ownerObject;
	}

	public Object getOwnerObject() {
		return ownerObject;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

}
