package uap.workflow.engine.rejectsgy;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.orgs.FlowUserDesc;
public class RejectSgyManager implements IRejectSgy {
	private static RejectSgyManager instance = null;
	private RejectSgyService sgyService = null;
	private void setSgyService(RejectSgyService sgyService) {
		this.sgyService = sgyService;
	}
	private RejectSgyManager() {};
	synchronized public static RejectSgyManager getInstance() {
		if (instance != null)
			return instance;
		else {
			instance = new RejectSgyManager();
		}
		return instance;
	}
	public FlowUserDesc[] getRejectUsersByTaskAndHumAct(ITask task, IActivity activiti) {
		return null;
	}
	public IActivity[] getRejectRange(ITask task) {
		this.setService(this.getSgy(task.getExecution().getActivity()));
		if (sgyService == null) {
			return null;
		}
		return sgyService.getRejectRange(task);
	}
	public boolean isPermit(ITask task) {
		this.setService(this.getSgy(task.getExecution().getActivity()));
		if (sgyService != null) {
			return sgyService.isPermit(task.getExecution().getActivity());
		} else {
			return false;
		}
	}
	private int getSgy(IActivity humAct) {
		return 0;
	}
	private void setService(int sgy) {
		switch (sgy) {
		case RejectSgy_PreviousHumAct:
			this.setSgyService(new PreviousHumAct());
			break;
		case RejectSgy_AppointHumAct:
			this.setSgyService(new AppointHumActSgy());
			break;
		case RejectSgy_AllHumAct:
			this.setSgyService(new AllHumAct());
			break;
		case RejectSgy_StartHumAct:
			this.setSgyService(new StartHumAct());
			break;
		}
	}
}
