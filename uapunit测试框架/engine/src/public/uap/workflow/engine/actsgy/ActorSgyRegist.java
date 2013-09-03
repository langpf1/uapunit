package uap.workflow.engine.actsgy;
import java.util.HashMap;
import java.util.Map;
public class ActorSgyRegist {
	private static ActorSgyRegist instance = null;
	private Map<Integer, IActorSgyService> services = new HashMap<Integer, IActorSgyService>();
	private ActorSgyRegist() {};
	synchronized public static ActorSgyRegist getInstance() {
		if (instance != null)
			return instance;
		else {
			instance = new ActorSgyRegist();
			instance.addService();
		}
		return instance;
	}
	private void addService() {
		services.put(IActorSgy.ActorSgy_AppointActor, null);
		services.put(IActorSgy.ActorSgy_ProcessStart, null);
		services.put(IActorSgy.ActorSgy_SameSomeActs, null);
		services.put(IActorSgy.ActorSgy_SomeActAsign, null);
		services.put(IActorSgy.ActorSgy_PreActAssign, null);
		services.put(IActorSgy.ActorSgy_SelfDefActor, null);
	}
}
