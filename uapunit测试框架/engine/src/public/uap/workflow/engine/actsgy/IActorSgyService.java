package uap.workflow.engine.actsgy;

import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.pvm.process.ActivityImpl;
public interface IActorSgyService {
	String[] getActorsTaskEntity(ITask task, ActivityImpl humAct, IBusinessKey formInfo);
}
