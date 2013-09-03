package uap.workflow.engine.actsgy;

import uap.workflow.app.core.IBusinessKey;

public class ActorService implements IActorService {
	public String[] getActivityActors(IBusinessKey formVo, String activityId, String processKey)
	{
		return ActorSgyManager.getInstance().getActivityActors(formVo, activityId, processKey);
	}
}
