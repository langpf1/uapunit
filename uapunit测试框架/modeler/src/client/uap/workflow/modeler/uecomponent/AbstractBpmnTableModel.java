package uap.workflow.modeler.uecomponent;

import java.util.List;
import nc.ui.pub.beans.table.NCTableModel;

public abstract class AbstractBpmnTableModel extends NCTableModel{
	
	public AbstractBpmnTableModel(){
		super();
		initColumns();
	}
	
	//子类重写
	protected abstract void initColumns();
	
	//子类重写
	protected abstract int[] getColumnWidth();
	
	public boolean isCellEditable(int row, int column)
	{
		return true;
	}
}
