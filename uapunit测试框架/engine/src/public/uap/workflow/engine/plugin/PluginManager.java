package uap.workflow.engine.plugin;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import uap.workflow.engine.cache.IWfCache;
import uap.workflow.engine.cache.WfCacheManager;
/**
 * Plugin管理类
 */
public class PluginManager {
	private static final String P_EXTENSIONPOINT = "wf_extension-point";
	private static final String P_PLUGIN = "wf_plugin";
	private static PluginManager pm = null;
	private PluginManager() {}
	public void refresh() {
		pm = new PluginManager();
	}
	public synchronized static PluginManager newIns() {
		if (pm == null) {
			pm = new PluginManager();
		}
		return pm;
	}
	public IWfCache getPluginCache() {
		String ds = null;
		return WfCacheManager.getStrongCache(P_PLUGIN, ds);
	}
	/**
	 * 将扩展点存入缓存
	 * 
	 * @param point
	 */
	@SuppressWarnings("unchecked")
	public void put(WfExPoint point) {
		Map<String, WfExPoint> points = (Map<String, WfExPoint>) getPluginCache().get(P_EXTENSIONPOINT);
		if (points == null) {
			points = new ConcurrentHashMap<String, WfExPoint>();
			getPluginCache().put(P_EXTENSIONPOINT, points);
		}
		if (!points.containsKey(point.getPoint())) {
			points.put(point.getPoint(), point);
		}
	}
	@SuppressWarnings("rawtypes")
	public WfExPoint getExtPoint(String extPointId) {
		return (WfExPoint) ((Map)this.getPluginCache().get(P_EXTENSIONPOINT)).get(extPointId);
	}
	public List<WfExtension> getExtensions(String pointId) {
		return this.getExtPoint(pointId).getExtensionList();
	}
	public WfExtension getExtension(String extPointId, String extensionId) {
		List<WfExtension> list = this.getExtensions(extPointId);
		if (list == null) {
			return null;
		}
		WfExtension tmp = null;
		for (int i = 0; i < list.size(); i++) {
			tmp = list.get(i);
			if (tmp.getId().equalsIgnoreCase(extensionId)) {
				return tmp;
			}
		}
		return null;
	}
}
