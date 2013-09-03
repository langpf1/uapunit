package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class ReSignTask extends BaseReceiveTask {
	
	private List<Participant> reassign;

	public void setReassign(List<Participant> reassign) {
		this.reassign = reassign;
	}

	public List<Participant> getReassign() {
		return reassign;
	}
}
