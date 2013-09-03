package uap.workflow.client.ui;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uap.workflow.bpmn2.model.FieldExtension;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wcj
 */
public class ParameterTableModel extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 346523037415789672L;

	public String[] COLUMN_NAMES = new String[] {
        "参数类型",
        "参数名称",
        "值/表达式"
    };

    private Method method = null;
    private List<FieldExtension> paramValues = null;


    public void setMethod(Method method) {
        this.method = method;

        paramValues = new ArrayList<FieldExtension>();
        FieldExtension field = null;
        for (int i = 0; i < method.getParameterTypes().length; i++){
        	field = new FieldExtension();
        	field.setFieldType(method.getParameterTypes()[i].toString());
        	paramValues.add(field);
        }
    }

    public List<FieldExtension> getParamValues() {
        return paramValues;
    }

    public void setParamValues(List<FieldExtension> paramValues){
        this.paramValues = paramValues;
    }
    
    @Override
    public String getColumnName(int arg0) {
        return COLUMN_NAMES[arg0];
    }



    public int getRowCount() {
        if (method == null) {
            return 0;
        } else {
            return method.getParameterTypes().length;
        }
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Class<?> clazz = method.getParameterTypes()[rowIndex];

        switch(columnIndex) {
            case 0:
                return clazz.getCanonicalName();
            case 1:
                return null;
            case 2:
                return paramValues.get(rowIndex).getExpression();
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }



    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 2) {
            return;
        } else {
            paramValues.get(rowIndex).setExpression((String)value);
        }
    }








}
