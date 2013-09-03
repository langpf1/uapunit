package uap.workflow.reslet.application.receiveData;

public class ApproveTask extends BaseReceiveTask{
	/**基类中有：批语，被执行动作编码，是否跟踪，当前任务的id，抄送*/
	/**增加指派的信息*/
	
	private AssignTask assign ;

	public void setAssign(AssignTask assign) {
		this.assign = assign;
	}

	public AssignTask getAssign() {
		return assign;
	}
}
