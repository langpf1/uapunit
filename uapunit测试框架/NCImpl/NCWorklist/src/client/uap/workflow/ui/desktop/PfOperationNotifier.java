package uap.workflow.ui.desktop;

import java.awt.Container;

import nc.ui.pub.ToftPanel;

public class PfOperationNotifier implements IPfOperationListenerAware, IPfOperationListener {
	
	private IPfOperationListener defaultListener = null;
	private IPfOperationListener currListener = null;
	
	public PfOperationNotifier(Container container) {
		if (container instanceof ToftPanel) {
			defaultListener = new PfToftPanelOperationAdapter((ToftPanel) container);
		} else {
			defaultListener = new PfContainerOperationAdapter(container);
		}
		
		currListener = defaultListener;
	}

	@Override
	public void setListener(IPfOperationListener listener) {
		currListener = listener;
	}

	@Override
	public void restoreDefaultListener() {
		currListener = defaultListener;
	}

	@Override
	public void fireOperationPerformed(PfOperationEvent event) {
		currListener.operationPerformed(event);
	}

	@Override
	public void fireOperationPerformed(int type, boolean succeeded, String hint) {
		currListener.operationPerformed(new PfOperationEvent(type, succeeded, hint));
	}

	@Override
	public void operationPerformed(PfOperationEvent event) {
		fireOperationPerformed(event);
	}
	
	public void showMessage(String message) {
		fireOperationPerformed(0, true, message);
	}
	
	public void showError(String error) {
		fireOperationPerformed(0, false, error);
	}

}
