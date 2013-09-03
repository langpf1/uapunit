package uap.workflow.vo;


/**
 * 工作项或流程实例的超期情况
 * @author yanke1
 *
 */
public class FlowOverdueVO {
	
	public static final int TYPE_INSTANCE = 0;
	public static final int TYPE_WORKNOTE = 1;
	
	
	/** 工作项或实例的pk */
	private String primaryKey = null;

	/** 是否超期 */
	private boolean isOverdue = false;
	/** 是否需要提醒 */
	private boolean isNeedRemind = false;
	/** 工作项或实例是否已完成 */
	private boolean isFinished = false;

	/** FlowOverdueVO类型，表示是工作项超期情况还是流程实例超期情况 */
	private int type;
	
	/** 期限 */
	private Integer timeLimit = 0;
	/** 提前提醒天数 */
	private Integer timeRemind = 0;
	
	/** 超期天数 */
	private Integer overdueDays = 0;
	
	/** 历时 */
	private String duration = null;
	
	
	public String getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public boolean isOverdue() {
		return isOverdue;
	}
	
	public void setOverdue(boolean isOverdue) {
		this.isOverdue = isOverdue;
	}
	
	public boolean isNeedRemind() {
		return isNeedRemind;
	}
	
	public void setNeedRemind(boolean isNeedRemind) {
		this.isNeedRemind = isNeedRemind;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public Integer getTimeLimit() {
		return timeLimit;
	}
	
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public Integer getTimeRemind() {
		return timeRemind;
	}
	
	public void setTimeRemind(Integer timeRemind) {
		this.timeRemind = timeRemind;
	}

	public Integer getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	
}
