package uap.workflow.modeler.sheet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

/**
 * ���������. <br>
 * @author �׾� 2004-5-6
 * @modifier leijun 2009-6 �������Ե���������
 */
public abstract class AbstractProperty implements Property {

	private Object value;

	//XXX:leijun+2009-6 �����Ե���������
	private Object ownerObject;

	// PropertyChangeListeners are not serialized.
	private transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		Object oldValue = this.value;
		this.value = value;
		if(oldValue==null&&this.value==null)
			return;
		firePropertyChange(oldValue, getValue());
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

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		listeners = new PropertyChangeSupport(this);
	}

	public Object getOwnerObject() {
		return ownerObject;
	}

	public void setOwnerObject(Object ownerObject) {
		this.ownerObject = ownerObject;
	}
}
