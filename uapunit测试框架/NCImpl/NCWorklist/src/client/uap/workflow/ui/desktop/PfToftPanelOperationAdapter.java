package uap.workflow.ui.desktop;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ToftPanel;

public class PfToftPanelOperationAdapter implements IPfOperationListener {
	
	private ToftPanel toftPanel = null;
	
	public PfToftPanelOperationAdapter(ToftPanel panel) {
		this.toftPanel = panel;
	}

	@Override
	public void operationPerformed(PfOperationEvent event) {
		boolean succeeded =event.isSucceeded();
		String hint = event.getHintMessage();
		
		if (succeeded) {
			toftPanel.showHintMessage(hint);
		} else {
			toftPanel.showErrorMessage(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000237")/*
																												 * @
																												 * res
																												 * "´íÎó"
																												 */, hint);
		}
	}

}
