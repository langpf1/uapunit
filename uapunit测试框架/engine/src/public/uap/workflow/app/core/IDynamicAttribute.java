package uap.workflow.app.core;
import java.io.Serializable;
/**
 * 
 * @author tianchw
 * 
 */
public interface IDynamicAttribute extends Serializable {
	void add2Property(Object key, Object value);
	Object get2Property(Object key);
}
