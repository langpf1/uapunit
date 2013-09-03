package uap.workflow.engine.context;
import org.apache.commons.lang.StringUtils;
import uap.workflow.engine.message.MsgType;
public class UserTaskPrepCtx extends ActivityRunTimeCtx {
	private static final long serialVersionUID = -8912234394985094238L;
	protected String[] userPks;
	protected String[] userNames;
	protected boolean isAssign;
	protected MsgType[] msgType;
	public MsgType[] getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType[] msgType) {
		this.msgType = msgType;
	}
	public String[] getUserPks() {
		return userPks;
	}
	public void setUserPks(String[] userPks) {
		this.userPks = userPks;
	}
	public String[] getUserNames() {
		return userNames;
	}
	public void setUserNames(String[] userNames) {
		this.userNames = userNames;
	}
	public boolean isAssign() {
		return isAssign;
	}
	public void setAssign(boolean isAssign) {
		this.isAssign = isAssign;
	}
	public String getUserPksToString() {
		if (userPks == null) {
			return null;
		}
		return StringUtils.join(userPks, ",");
	}
	public String getUserNamesToString() {
		if (userNames == null) {
			return null;
		}
		return StringUtils.join(userNames, ",");
	}
}
