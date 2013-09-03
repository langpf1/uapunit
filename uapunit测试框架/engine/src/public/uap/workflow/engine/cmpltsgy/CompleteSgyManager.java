package uap.workflow.engine.cmpltsgy;
import uap.workflow.engine.core.IActivityInstance;
/**
 * 完成策略管理器
 * 
 * @author tianchw
 * 
 */
public class CompleteSgyManager implements ICompleteSgy {
	private static CompleteSgyManager instance = null;
	private ICompleteSgyService sgyService = null;
	private void setSgyService(ICompleteSgyService sgyService) {
		this.sgyService = sgyService;
	}
	private CompleteSgyManager() {};
	synchronized public static CompleteSgyManager getInstance() {
		if (instance != null)
			return instance;
		else {
			instance = new CompleteSgyManager();
		}
		return instance;
	}
	public boolean isComplete(IActivityInstance humActIns, Integer count) {
		this.setService(this.getSgy(humActIns));
		if (sgyService == null) {
			return true;
		}
		boolean flag = sgyService.isComplete(humActIns, count);
		return flag;
	}
	private int getSgy(IActivityInstance humActIns) {
		return CompleteSgy_Countersign;
	}
	private void setService(int sgy) {
		switch (sgy) {
		case CompleteSgy_Occupy:
			this.setSgyService(new OccupySgy());
			break;
		case CompleteSgy_Countersign:
			this.setSgyService(new CountsignSgy());
			break;
		case CompleteSgy_ByCount:
			this.setSgyService(new ByCountSgy());
			break;
		case CompleteSgy_ByPercent:
			this.setSgyService(new ByPercentSgy());
			break;
		default:
			this.setSgyService(new OccupySgy());
			break;
		}
	}
}
