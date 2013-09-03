package uap.workflow.engine.context;
import java.io.Serializable;
/**
 * 
 * @author tianchw
 * 
 */
public class ActivityRunTimeCtx implements Serializable {
	private static final long serialVersionUID = 3664281227862545263L;
	protected String activityId;
	protected String activityName;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activiId) {
		this.activityId = activiId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activiName) {
		this.activityName = activiName;
	}
	public void check() {}
}
