package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.bpmn2.model.FormProperty;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class FormPropertyModel extends AbstractBpmnTableModel {
	public FormPropertyModel() {
		super();
	}

	@Override
	public Class getColumnClass(int col) {
		if (col > 7 && col < 11)
			return Boolean.class;
		return String.class;
	}

	public int[] getColumnWidth() {
		return new int[] { BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_4WIDTH, BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_3WIDTH,
				BpmnModelerConstants.COLUMN_4WIDTH, BpmnModelerConstants.COLUMN_3WIDTH, BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_2WIDTH,
				BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_2WIDTH, BpmnModelerConstants.COLUMN_12WIDTH };
	}

	public Object getValueAt(int row, int column) {
		Object obj = super.getValueAt(row, column);
		if (column > 7 && column < 11) {
			return obj == null ? false : Boolean.parseBoolean(obj.toString());
		} else
			return obj == null ? "" : obj.toString();
	}

	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("Id");
		m_columnIdentifiers.add("Name");
		m_columnIdentifiers.add("Type");
		m_columnIdentifiers.add("Value");
		m_columnIdentifiers.add("Expression");
		m_columnIdentifiers.add("Variable");
		m_columnIdentifiers.add("Default");
		m_columnIdentifiers.add("Pattern");
		m_columnIdentifiers.add("Required");
		m_columnIdentifiers.add("Readable");
		m_columnIdentifiers.add("Writeable");
		m_columnIdentifiers.add("Form Values");
	}	
}
