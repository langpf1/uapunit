package uap.workflow.modeler.sheet;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.graph.itf.IGraphListenerClaim;
import nc.ui.wfengine.sheet.Property;

public class BasicModelerPropertyPanel extends UITabbedPane implements
		ImodelerPropertySheet, IGraphListenerClaim {

	@Override
	public String[] getListenTargetType() {
		return null;
	}

	@Override
	public void setProperties(Property[] properties) {

	}

	@Override
	public Property[] getProperties() {
		return null;
	}

}
