package uap.workflow.engine.rejectsgy;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
public abstract class RejectSgyService {
	public boolean isPermit(IActivity humAct) {
		return true;
	}
	abstract IActivity[] getRejectRange(ITask task);
}
