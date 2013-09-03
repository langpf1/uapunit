package uap.workflow.engine.plugin;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import uap.workflow.engine.exception.WorkflowRuntimeException;
/**
 * 扩展机制定义解析类
 * 
 * @author licza
 * @2010年9月9日14:59:03
 */
public class PluginDefParser {
	/**
	 * 解析输入
	 * 
	 * @param reader
	 * @return
	 */
	public static WfPlugin parser(Reader reader) {
		try {
			JAXBContext jc = JAXBContext.newInstance(WfPlugin.class);
			Unmarshaller um = jc.createUnmarshaller();
			//jc.createMarshaller();
			WfPlugin plugin = (WfPlugin) um.unmarshal(reader);
			if (plugin != null) {
				List<WfExPoint> points = plugin.getExtensionPointList();
				WfExPoint point = null;
				for (int i = 0; i < points.size(); i++) {
					point = points.get(i);
					PluginManager.newIns().put(point);
					List<WfExtension> extensions = point.getExtensionList();
					WfExtension extension = null;
					for (int j = 0; j < extensions.size(); j++) {
						extension = extensions.get(j);
						extension.setPointId(extension.getPointId());
					}
				}
				return plugin;
			}
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
		return null;
	}
	/**
	 * 解析xml文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static WfPlugin reader(String filePath) {
		Reader reader = null;
		try {
			File f = new File(filePath);
			if (!f.exists())
				return null;
			String xmlText = FileUtils.readFileToString(new File(filePath), "UTF-8");
			reader = new StringReader(xmlText);
			return parser(reader);
		} catch (IOException e) {} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {}
		}
		return null;
	}
}
