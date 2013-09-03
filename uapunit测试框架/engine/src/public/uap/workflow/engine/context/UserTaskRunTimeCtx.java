package uap.workflow.engine.context;
import uap.workflow.engine.message.MsgType;
/**
 * 
 * @author tianchw
 * 
 */
public class UserTaskRunTimeCtx extends ActivityRunTimeCtx {
	private static final long serialVersionUID = -8210014397801334363L;
	protected String[] userPks;
	protected boolean isSequence = false;
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
	public boolean isSequence() {
		return isSequence;
	}
	public void setSequence(boolean isSequence) {
		this.isSequence = isSequence;
	}
	@Override
	public void check() {
		super.check();
	}
}
