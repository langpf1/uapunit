package uap.workflow.modeler.uecomponent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
public class ParameterTableModel extends AbstractBpmnTableModel {

	private static final long serialVersionUID = 346523037415789672L;

	private Method method = null;
	private List<FieldExtension> paramValues = null;

	public void setMethod(Method method) {
		this.method = method;

		paramValues = new ArrayList<FieldExtension>();
		FieldExtension field = null;
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			field = new FieldExtension();
			field.setFieldType(method.getParameterTypes()[i].toString());
			paramValues.add(field);
		}
	}

	public List<FieldExtension> getParamValues() {
		if (paramValues == null)
			paramValues = new ArrayList<FieldExtension>();
		return paramValues;
	}

	public void setParamValues(List<FieldExtension> paramValues) {
		this.paramValues = paramValues;
	}

	public int[] getColumnWidth() {
		return new int[] { 
				BpmnModelerConstants.COLUMN_12WIDTH, 
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_12WIDTH
		};
	}

	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("Parameter Type"); 
		m_columnIdentifiers.add("Parameter Name");
		m_columnIdentifiers.add("Value/Expression");
	}

}
