package uap.workflow.modeler.editors;
import uap.workflow.modeler.refmodels.BizActionRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;

public class BpmnBizActionEditor extends DefaultBpmnRefpaneEditor  {

	@Override
	public AbstractRefModel getRefModel() {
		return new BizActionRefGridTreeModel();
	}

}
