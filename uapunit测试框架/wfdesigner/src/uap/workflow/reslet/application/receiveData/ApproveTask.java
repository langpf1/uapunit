package uap.workflow.reslet.application.receiveData;

public class ApproveTask extends BaseReceiveTask{
	/**�������У������ִ�ж������룬�Ƿ���٣���ǰ�����id������*/
	/**����ָ�ɵ���Ϣ*/
	
	private AssignTask assign ;

	public void setAssign(AssignTask assign) {
		this.assign = assign;
	}

	public AssignTask getAssign() {
		return assign;
	}
}
