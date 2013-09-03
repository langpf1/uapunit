package uap.workflow.engine.actsgy;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
public interface IActorSgy {
	public static final int ActorSgy_AppointActor = 1;// 指定参与者
	public static final int ActorSgy_ProcessStart = 2;// 同流程启动者
	public static final int ActorSgy_SameSomeActs = 3;// 同某几个活动节点的权限一样
	public static final int ActorSgy_SomeActAsign = 4;// 由某个活动节点指派的权限
	public static final int ActorSgy_PreActAssign = 5;// 前一个活动节点指派
	public static final int ActorSgy_SelfDefActor = 6;// 自定义参与者
	String[] getActorsRange(IBusinessKey formVo, IActivity humAct, ITask pTask,String pk_user);
	String[] getRuntimeActors(IBusinessKey formVo, IActivity humAct, ITask task);
	String[] getActivityActors(IBusinessKey formVo, String activityId, String processKey);
}
