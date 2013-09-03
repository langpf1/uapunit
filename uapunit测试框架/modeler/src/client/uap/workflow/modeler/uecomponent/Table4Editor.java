package uap.workflow.modeler.uecomponent;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;

public class Table4Editor extends UITable {

	public Table4Editor(String[] columNames, int[] columnWidth) {
		super(new TableModel4Editor(columNames));
		setColumnWidth(columnWidth);
	}

}

class TableModel4Editor extends NCTableModel {

	public TableModel4Editor(String[] clumnNames) {
		super(null, clumnNames);
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	};
}

class TableModel4Editor2 extends TableModel4Editor {

	public TableModel4Editor2(String[] clumnNames) {
		super(clumnNames);
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	};
}
