package uap.workflow.ui.desktop;

import java.awt.Container;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;

public class PfContainerOperationAdapter implements IPfOperationListener {

	private Container container = null;

	public PfContainerOperationAdapter(Container container) {
		this.container = container;
	}

	@Override
	public void operationPerformed(PfOperationEvent event) {
		boolean succeeded = event.isSucceeded();
		String hint = event.getHintMessage();

		if (succeeded) {
			MessageDialog.showHintDlg(container, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000227")/*
																														 * @
																														 * res
																														 * "Ã· æ"
																														 */, hint);
		} else {
			MessageDialog.showErrorDlg(container, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000237")/*
																														 * @
																														 * res
																														 * "¥ÌŒÛ"
																														 */, hint);
		}
	}

}
