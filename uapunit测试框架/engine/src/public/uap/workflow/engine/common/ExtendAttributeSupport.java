package uap.workflow.engine.common;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author tianchw
 * 
 */
public class ExtendAttributeSupport implements Serializable {
	private static final long serialVersionUID = 424372049342125924L;
	private Map<Object, Object> propMap = new HashMap<Object, Object>();
	public void add2Property(Object key, Object value) {
		propMap.put(key, value);
	}
	public Object get2Property(Object key) {
		return propMap.get(key);
	}
}
