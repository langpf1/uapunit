package uap.workflow.reslet.application.receiveData;

public class HistoricActivity {
	/*
	 * 活动的name
	 * */
	private String activity_name;
	/**
	 * 任务实际执行人
	 */
	private String pk_executer;
	/**
	 * 任务创建时间
	 */
	private String begindate;//
	/**
	 * 任务完成时间
	 */
	private String finishdate;//
	/**
	 * 任务审批的意见，批语
	 */
	private String comment;//
	
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getPk_executer() {
		return pk_executer;
	}
	public void setPk_executer(String pk_executer) {
		this.pk_executer = pk_executer;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getFinishdate() {
		return finishdate;
	}
	public void setFinishdate(String finishdate) {
		this.finishdate = finishdate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
