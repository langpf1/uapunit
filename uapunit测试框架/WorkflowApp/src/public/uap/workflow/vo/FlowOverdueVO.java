package uap.workflow.vo;


/**
 * �����������ʵ���ĳ������
 * @author yanke1
 *
 */
public class FlowOverdueVO {
	
	public static final int TYPE_INSTANCE = 0;
	public static final int TYPE_WORKNOTE = 1;
	
	
	/** �������ʵ����pk */
	private String primaryKey = null;

	/** �Ƿ��� */
	private boolean isOverdue = false;
	/** �Ƿ���Ҫ���� */
	private boolean isNeedRemind = false;
	/** �������ʵ���Ƿ������ */
	private boolean isFinished = false;

	/** FlowOverdueVO���ͣ���ʾ�ǹ�����������������ʵ��������� */
	private int type;
	
	/** ���� */
	private Integer timeLimit = 0;
	/** ��ǰ�������� */
	private Integer timeRemind = 0;
	
	/** �������� */
	private Integer overdueDays = 0;
	
	/** ��ʱ */
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
