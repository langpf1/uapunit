package uap.workflow.engine.cache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import nc.vo.cache.CacheManager;
import nc.vo.cache.ICache;
/**
 * �����ع����࣬�ṩ�˻����������api
 * 
 * @author dengjt
 * 
 */
public final class WfCacheMonitor {
	public static Set<String> getExistCacheKeys() {
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, IWfCache> getExistCacheMapByRegion(String key) {
		ICache cache = CacheManager.getInstance().getCache(key);
		return cache.toMap();
	}
	public static Map<String, IWfCache> getExitFileCacheMap() {
		Map<String, IWfCache> cache = WfCacheManager.fileCacheMap;
		Map<String, IWfCache> ncache = new HashMap<String, IWfCache>();
		Iterator<Entry<String, IWfCache>> it = cache.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, IWfCache> entry = it.next();
			String key = entry.getKey();
			if (key.startsWith(WfCacheManager.SESSION_PRE))
				continue;
			ncache.put(key, entry.getValue());
		}
		return ncache;
	}
}
