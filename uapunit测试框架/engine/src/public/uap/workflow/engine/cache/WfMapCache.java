package uap.workflow.engine.cache;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * LFwøÚº‹NCª∫¥Ê  ≈‰
 * 
 * @author dengjt
 * 
 */
public class WfMapCache implements IWfCache {
	private Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
	private String name;
	public WfMapCache(String name) {
		this.name = name;
	}
	public Object get(Object key) {
		return map.get(key);
	}
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}
	public Object remove(Object key) {
		return map.remove(key);
	}
	public Set<Object> getKeys() {
		return map.keySet();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
