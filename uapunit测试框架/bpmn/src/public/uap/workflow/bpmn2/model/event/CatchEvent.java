package uap.workflow.bpmn2.model.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.TypeChangeMonitor;
import uap.workflow.bpmn2.model.ISynchronization;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="CatchEvent")
public class CatchEvent extends Event implements ISynchronization {
	
	private String boundaryRef;
	@TypeChangeMonitor("interrupting")
	@XmlAttribute(name="isInterrupting")
	public boolean interrupting;

	public boolean isInterrupting() {
		return interrupting;
	}

	public void setInterrupting(boolean isInterrupting) {
		this.interrupting = isInterrupting;
	}

	public String getBoundaryRef() {
		return boundaryRef;
	}

	public void setBoundaryRef(String boundaryRef) {
		this.boundaryRef = boundaryRef;
	}

	private List<String> getDestFields(Object dest){
		Field[] fields = dest.getClass().getFields();
		List<String> fieldNames = new ArrayList<String>();
		int i = 0;
		for(Field field : fields){
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}
	public void CloneEventDefinitation(Object dest, Object source){
		Field[] fields = source.getClass().getFields();
		List<String> fieldNames = getDestFields(dest);
		for(int i = 0; i < fields.length; i++){
			try {
				if (fieldNames.contains(fields[i].getName()))
					fields[i].set(dest, fields[i].get(source));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void marshal() {
	}

	@Override
	public void unmarshal() {
	}

}
