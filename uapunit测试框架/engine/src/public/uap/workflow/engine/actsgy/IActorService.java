package uap.workflow.engine.actsgy;

import uap.workflow.app.core.IBusinessKey;

public interface IActorService {
	String[] getActivityActors(IBusinessKey formVo, String activityId, String processKey);
}
