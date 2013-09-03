package uap.workflow.engine.actsgy;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
public interface IActorSgy {
	public static final int ActorSgy_AppointActor = 1;// ָ��������
	public static final int ActorSgy_ProcessStart = 2;// ͬ����������
	public static final int ActorSgy_SameSomeActs = 3;// ͬĳ������ڵ��Ȩ��һ��
	public static final int ActorSgy_SomeActAsign = 4;// ��ĳ����ڵ�ָ�ɵ�Ȩ��
	public static final int ActorSgy_PreActAssign = 5;// ǰһ����ڵ�ָ��
	public static final int ActorSgy_SelfDefActor = 6;// �Զ��������
	String[] getActorsRange(IBusinessKey formVo, IActivity humAct, ITask pTask,String pk_user);
	String[] getRuntimeActors(IBusinessKey formVo, IActivity humAct, ITask task);
	String[] getActivityActors(IBusinessKey formVo, String activityId, String processKey);
}
