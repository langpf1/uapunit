package uap.workflow.reslet.application.receiveData;

public class HistoricActivity {
	/*
	 * ���name
	 * */
	private String activity_name;
	/**
	 * ����ʵ��ִ����
	 */
	private String pk_executer;
	/**
	 * ���񴴽�ʱ��
	 */
	private String begindate;//
	/**
	 * �������ʱ��
	 */
	private String finishdate;//
	/**
	 * �������������������
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
