package uap.workflow.ui.notice.editors;

import java.util.ArrayList;

import uap.workflow.app.notice.ReceiverVO;
import uap.workflow.pub.app.message.vo.MsgReceiverTypes;

/**
 *  接收者工具类 
 * @author 雷军 2003-10-23
 */
public class ReceiverUtils {
	public ReceiverUtils() {
	}

	/**
	 * 从接收者数组中提取出显示串
	 */
	public static String analyseStrRevsFromXML(ReceiverVO[] revs) {
		StringBuffer sb = new StringBuffer();
		if (revs == null || revs.length == 0)
			return "";

		for (int i = 0; i < revs.length; i++) {
			switch (revs[i].getType()) {
			case MsgReceiverTypes.REVEIVER_TYPE_USER:
				sb.append("\n");
				sb.append(revs[i].getName());
				sb.append(" [U]");
				break;
			case MsgReceiverTypes.REVEIVER_TYPE_ROLE:
				sb.append("\n");
				sb.append(revs[i].getName());
				sb.append(" [R]");
				break;
			case MsgReceiverTypes.REVEIVER_TYPE_STATION:
				sb.append("\n");
				sb.append(revs[i].getName());
				sb.append(" [S]");
				break;
			case MsgReceiverTypes.REVEIVER_TYPE_SYSTEM:
				sb.append("\n");
				sb.append(revs[i].getName());
				sb.append(" [V]");
				break;
			case MsgReceiverTypes.REVEIVER_TYPE_CUSTOM:
				sb.append("\n");
				sb.append(revs[i].getName());
				sb.append(" [C]");

				// case MsgReceiverTypes.REVEIVER_TYPE_USER:
				// retSTR += "\n" + revs[i].getName() + " [U]";
				// break;
				// case MsgReceiverTypes.REVEIVER_TYPE_ROLE:
				// retSTR += "\n" + revs[i].getName() + " [R]";
				// break;
				// case MsgReceiverTypes.REVEIVER_TYPE_STATION:
				// retSTR += "\n" + revs[i].getName() + " [S]";
				// break;
				// case MsgReceiverTypes.REVEIVER_TYPE_SYSTEM:
				// retSTR += "\n" + revs[i].getName() + " [V]";
				// break;
				// case MsgReceiverTypes.REVEIVER_TYPE_CUSTOM:
				// retSTR += "\n" + revs[i].getName() + " [C]";
				break;
			default:
				break;
			}
		}
		
		if (sb.length() > 0) {
			return sb.substring(1);
		} else {
			return sb.toString();
		}
	}
	
	/**
	 * 从接收者数组中提取出显示串
	 */
	public static ArrayList<String> analyseStrRevPKsFromXML(ReceiverVO[] revs) {
		ArrayList<String> retSTR = new ArrayList<String>();
		if (revs == null || revs.length == 0)
			return retSTR;

		for (int i = 0; i < revs.length; i++) {
			switch (revs[i].getType()) {
				case MsgReceiverTypes.REVEIVER_TYPE_USER:
					retSTR.add("[U]" + revs[i].getPK());
					break;
				case MsgReceiverTypes.REVEIVER_TYPE_ROLE:
					retSTR.add("[R]" + revs[i].getPK());
					break;
				case MsgReceiverTypes.REVEIVER_TYPE_STATION:
					retSTR.add("[S]" + revs[i].getPK());
					break;
				case MsgReceiverTypes.REVEIVER_TYPE_SYSTEM:
					retSTR.add("[V]" + revs[i].getCode());
					break;
				case MsgReceiverTypes.REVEIVER_TYPE_CUSTOM:
					retSTR.add("[C]" + revs[i].getCode() + "$" + revs[i].getName());
					break;
				default:
					break;
			}
		}
		return retSTR;
	}

}