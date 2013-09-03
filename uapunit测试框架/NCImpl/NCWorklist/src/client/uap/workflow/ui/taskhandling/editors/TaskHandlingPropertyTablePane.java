package uap.workflow.ui.taskhandling.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;

import nc.ui.pub.beans.UIComboBox;
import uap.workflow.app.config.TaskHandlingTypeFactory;
import uap.workflow.app.taskhandling.ITaskHandlingType;
import uap.workflow.bpmn2.model.DefaultTaskHandlingDefinition;
import uap.workflow.bpmn2.model.DefaultTaskHandlingType;
import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.uecomponent.AbstractTablePane;

public class TaskHandlingPropertyTablePane extends AbstractTablePane {
	private static final long serialVersionUID = -5546399684298698350L;

	public TaskHandlingPropertyTablePane(TaskHandlingPropertyModel model) {
		super(model, model.getColumnWidth());
	}

	protected void initCellEditor() {
		UIComboBox comboBox = new UIComboBox();
		comboBox.addItem("");
		// ±È¿˙HashMap
		Map<String, ITaskHandlingType> taskHandlingTypeMap = TaskHandlingTypeFactory.getInstance().getTypes();
		Iterator iterator = taskHandlingTypeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ITaskHandlingType> entry = (Map.Entry<String, ITaskHandlingType>) iterator.next();
			String key = entry.getKey();
			ITaskHandlingType value = entry.getValue();
			comboBox.addItem(key);
		}

		getTable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));
	}

	@Override
	public Object assemberData() {
		Vector vector = ((AbstractBpmnTableModel) getTable().getModel()).getDataVector();
		List<DefaultTaskHandlingDefinition> list = new ArrayList<DefaultTaskHandlingDefinition>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			
			DefaultTaskHandlingDefinition taskHandlingDefinition = new DefaultTaskHandlingDefinition();
			ITaskHandlingType taskHandingType = TaskHandlingTypeFactory.getInstance().getType(rowVector.get(0).toString());
			DefaultTaskHandlingType defaultTaskHandlingType = new DefaultTaskHandlingType();
			if(taskHandingType!= null)
			{
				defaultTaskHandlingType.setCode(taskHandingType.getCode());
				defaultTaskHandlingType.setName(taskHandingType.getName());
			}
			taskHandlingDefinition.setTaskHandleType(defaultTaskHandlingType);

			list.add(taskHandlingDefinition);
		}
		return list;
	}

	@Override
	public void unassemberData(Object intializeData) {
		((AbstractBpmnTableModel) getTable().getModel()).getDataVector().clear();
		if (intializeData == null)
			return;
		List list = (List) intializeData;
		for (Object obj : list) {
			Vector rowVector = new Vector();
			DefaultTaskHandlingDefinition taskHandling = (DefaultTaskHandlingDefinition) obj;
			rowVector.add(taskHandling.getTaskHandleType().getCode());
			((AbstractBpmnTableModel) getTable().getModel()).getDataVector().add(rowVector);
		}
	}

	@Override
	protected void doEdit() {
		// TODO Auto-generated method stub
		
	}
}
