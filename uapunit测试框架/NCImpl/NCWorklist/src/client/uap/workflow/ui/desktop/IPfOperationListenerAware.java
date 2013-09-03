package uap.workflow.ui.desktop;

public interface IPfOperationListenerAware {

	public void setListener(IPfOperationListener listener);
	
	public void restoreDefaultListener();
	
	public void fireOperationPerformed(PfOperationEvent event);
	
	public void fireOperationPerformed(int type, boolean succeeded, String hint);
	
}
