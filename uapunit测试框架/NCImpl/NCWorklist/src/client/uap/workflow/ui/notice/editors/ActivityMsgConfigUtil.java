package uap.workflow.ui.notice.editors;

import java.util.HashMap;
import java.util.Map;
import uap.workflow.app.notice.ReceiverVO;
import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import com.thoughtworks.xstream.XStream;

public class ActivityMsgConfigUtil {

	private static Map<String, String> configDisplayMap = new HashMap<String, String>();

	public static String getDisplayValue(String receivers) {
		if (StringUtil.isEmptyWithTrim(receivers)) {
			return "";
		}

		if (!configDisplayMap.containsKey(receivers)) {

			String displayStr = "";

			try {
				ReceiverVO[] vos = (ReceiverVO[]) new XStream().fromXML(receivers);
				displayStr = ReceiverUtils.analyseStrRevsFromXML(vos);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			
			configDisplayMap.put(receivers, displayStr);
		}
		
		return configDisplayMap.get(receivers);
	}

}
