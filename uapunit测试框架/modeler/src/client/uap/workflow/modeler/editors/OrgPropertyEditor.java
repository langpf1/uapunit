package uap.workflow.modeler.editors;

import uap.workflow.modeler.refmodels.OrgRefModel;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.org.GroupVO;

public class OrgPropertyEditor extends DefaultBpmnRefpaneEditor{

	public OrgPropertyEditor(){
		((UIRefPane) editor).setMultiSelectedEnabled(true);
	}
	@Override
	public AbstractRefModel getRefModel() {
		OrgRefModel model = new OrgRefModel(); 
		GroupVO gVO = WorkbenchEnvironment.getInstance().getGroupVO();
		String pkGroup = gVO == null ? null : gVO.getPk_group();
		model.setPk_group(pkGroup);
		return model;
	}
	
	public Object getValue() {
		return ((UIRefPane) editor).getRefPKs();
	}

}

