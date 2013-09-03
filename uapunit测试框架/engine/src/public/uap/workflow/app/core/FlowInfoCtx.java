package uap.workflow.app.core;
import uap.workflow.engine.common.ExtendAttributeSupport;
import uap.workflow.engine.core.TaskInstanceFinishMode;
/**
 * 
 * @author tianchw
 * 
 */
public abstract class FlowInfoCtx extends ExtendAttributeSupport {
	private static final long serialVersionUID = 3542078163836371009L;
	protected String scratch;
	protected String comment;
	protected String visaPk;
	protected String userPk;
	public String getScratch() {
		return scratch;
	}
	public void setScratch(String scratch) {
		this.scratch = scratch;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getVisaPk() {
		return visaPk;
	}
	public void setVisaPk(String visaPk) {
		this.visaPk = visaPk;
	}
	public String getUserPk() {
		return userPk;
	}
	public void setUserPk(String userPk) {
		this.userPk = userPk;
	}
	abstract public void check();
	abstract public TaskInstanceFinishMode getFinishType();
}
