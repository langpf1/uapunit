package uap.workflow.engine.message;

/**
 * 
 * @author tianchw
 * 
 */
public enum MsgType {
	EMAIL, MSGCENTER, SMS;
	public static MsgType getMsgType(String pointId) {
		if (pointId == null) {
			return null;
		}
		if (pointId.indexOf("email") > -1) {
			return MsgType.EMAIL;
		}
		if (pointId.indexOf("msgcenter") > -1) {
			return MsgType.MSGCENTER;
		}
		return null;
	}
}
