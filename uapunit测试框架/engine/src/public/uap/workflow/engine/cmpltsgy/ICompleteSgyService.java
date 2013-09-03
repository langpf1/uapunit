package uap.workflow.engine.cmpltsgy;
import java.util.Set;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ITask;
public interface ICompleteSgyService {
	boolean isComplete(IActivityInstance actIns, Integer count);
	Set<ITask> fiterTask(Set<ITask> tasks);
}
