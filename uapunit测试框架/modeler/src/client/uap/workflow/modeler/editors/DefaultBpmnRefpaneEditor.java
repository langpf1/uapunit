package uap.workflow.modeler.editors;

import java.awt.Component;

import javax.swing.JTable;

import uap.workflow.modeler.refmodels.BizActionRefTreeModel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wfengine.designer.editors.ICustomRendererEdit;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;

public abstract class DefaultBpmnRefpaneEditor extends DefaultBpmnPropertyEditor implements ICustomRendererEdit {

	private DefaultCellRenderer label;

	public DefaultBpmnRefpaneEditor() {
		UIRefPane refPane = new UIRefPane();
		refPane.setRefModel(getRefModel());
		editor = refPane;
		setRefEditor(true);
		initCellRenderer();
	}
	
	public void initCellRenderer() {
		label = new DefaultCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				setValue(((UIRefPane) editor).getRefName());
				return this;
			}
		};
	}
	
	@Override
	public DefaultCellRenderer getRender() {
		return label;
	}
	
	public Object getValue() {
		return ((UIRefPane) editor).getRefPK();
	}
	
	/**
	 * ◊”¿‡÷ÿ–¥ 
	 * */
	public abstract AbstractRefModel getRefModel();

}
