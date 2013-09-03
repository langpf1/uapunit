package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
/**
 * 
 * @author tianchw
 * 
 */
public class CommitProInsCtx extends UnStartProInsCtx {
	private static final long serialVersionUID = 424105896816551823L;
	private String outerProInsPk;
	private String innerProInsPk;
	private String parentTaskPk;
	private boolean isMakeBill = false;
	protected UserTaskRunTimeCtx[] nextInfo;
	public String getOuterProInsPk() {
		return outerProInsPk;
	}
	public void setOuterProInsPk(String outerProInsPk) {
		this.outerProInsPk = outerProInsPk;
	}
	public String getInnerProInsPk() {
		return innerProInsPk;
	}
	public void setInnerProInsPk(String innerProInsPk) {
		this.innerProInsPk = innerProInsPk;
	}
	public String getParentTaskPk() {
		return parentTaskPk;
	}
	public void setParentTaskPk(String parentTaskPk) {
		this.parentTaskPk = parentTaskPk;
	}
	@Override
	public void setProDefPk(String proDefPk) {
		this.proDefPk = proDefPk;
	}
	@Override
	public void check() {
		super.check();
	}
	@Override
	public void setProDefId(String proDefId) {
		this.proDefId = proDefId;
	}
	public boolean isMakeBill() {
		return isMakeBill;
	}
	public void setMakeBill(boolean isMakeBill) {
		this.isMakeBill = isMakeBill;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return TaskInstanceFinishMode.Normal;
	}
	public UserTaskRunTimeCtx[] getNextInfo() {
		return nextInfo;
	}
	public void setNextInfo(UserTaskRunTimeCtx[] nextInfo) {
		this.nextInfo = nextInfo;
	}
}
