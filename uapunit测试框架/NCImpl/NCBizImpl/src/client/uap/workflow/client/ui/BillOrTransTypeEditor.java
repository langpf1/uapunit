package uap.workflow.client.ui;

import uap.workflow.modeler.editors.DefaultBpmnRefpaneEditor;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.BillTypeDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.org.GroupVO;

public class BillOrTransTypeEditor extends DefaultBpmnRefpaneEditor{

	public BillOrTransTypeEditor(){
		((UIRefPane) editor).setMultiSelectedEnabled(false);
	}
	@Override
	public AbstractRefModel getRefModel() {
		BillTypeDefaultRefModel model = new BillTypeDefaultRefModel("单据类型/交易类型"); 
		GroupVO gVO = WorkbenchEnvironment.getInstance().getGroupVO();
		String pkGroup = gVO == null ? null : gVO.getPk_group();
		model.setPk_group(pkGroup);
		return model;
	}
	
	public Object getValue() {
		return ((UIRefPane) editor).getRefPK();
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		((UIRefPane) editor).setPK(value);
	}

}

