package uap.workflow.modeler.uecomponent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import uap.workflow.bpmn2.model.CustomProperty;
import uap.workflow.bpmn2.model.FieldExtension;
import uap.workflow.modeler.utils.BpmnModelerConstants;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author wcj
 */
public class CustomPropertyTableModel extends AbstractBpmnTableModel {

	private static final long serialVersionUID = 346523037415789672L;

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}


	public int[] getColumnWidth() {
		return new int[] { 
				BpmnModelerConstants.COLUMN_12WIDTH, 
				BpmnModelerConstants.COLUMN_12WIDTH
		};
	}

	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("Property Name"); 
		m_columnIdentifiers.add("Value/Expression");
	}

}
