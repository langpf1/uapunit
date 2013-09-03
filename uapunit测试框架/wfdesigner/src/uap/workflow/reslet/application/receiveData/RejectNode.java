package uap.workflow.reslet.application.receiveData;
/**
 * 任务可以被驳回的环节的定义，包含驳回环节的id和驳回环节的名称
 */
public class RejectNode {
  
	/**驳回环节的id*/
	private String RejectID;
	
	/**驳回环节的名称*/
	private String RejectName;

	public String getRejectID() {
		return RejectID;
	}

	public void setRejectID(String rejectID) {
		RejectID = rejectID;
	}

	public String getRejectName() {
		return RejectName;
	}

	public void setRejectName(String rejectName) {
		RejectName = rejectName;
	}
	
}
