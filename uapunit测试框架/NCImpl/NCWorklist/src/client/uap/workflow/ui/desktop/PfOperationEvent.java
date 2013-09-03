package uap.workflow.ui.desktop;

public class PfOperationEvent {
	
	private int type;
	private boolean succeeded;
	private String hintMessage;
	
	
	public PfOperationEvent(int type, boolean succeeded, String hintMessage) {
		this.type = type;
		this.succeeded = succeeded;
		this.hintMessage = hintMessage;
	}

	public int getType() {
		return type;
	}
	
	public boolean isSucceeded() {
		return succeeded;
	}

	public String getHintMessage() {
		return hintMessage;
	}

}
