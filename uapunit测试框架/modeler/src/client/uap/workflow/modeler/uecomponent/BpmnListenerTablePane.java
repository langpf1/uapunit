package uap.workflow.modeler.uecomponent;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import nc.bs.framework.common.NCLocator;
import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.table.NCTableModel;
import uap.workflow.bpmn2.model.EventListener;
import uap.workflow.bpmn2.model.ExecutionListener;
import uap.workflow.bpmn2.model.FieldExtension;
import uap.workflow.bpmn2.model.TaskListener;
import uap.workflow.engine.invocation.ExtensionListenerConfig;
import uap.workflow.engine.itf.IExtensionConfig;
import uap.workflow.engine.utils.StringUtil;
import uap.workflow.modeler.editors.ParameterEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;
import uap.workflow.modeler.utils.ExecutionListenerTypeEnum;
import uap.workflow.modeler.utils.ListenerServiceTypeEnum;
import uap.workflow.modeler.utils.TaskListenerTypeEnum;

public class BpmnListenerTablePane extends AbstractTablePane {

	private static final long serialVersionUID = -5895870735831042686L;
	private boolean taskListenerType = false; 
	private Class<?> implementationClass = null;
	
	public BpmnListenerTablePane(ListenerPropertyModel model, boolean taskListenerType) {
		super(model,model.getColumnWidth());
		this.taskListenerType = taskListenerType;
	}

	private Object[] getTaskListenerItem(){
		List<Object> list = new ArrayList<Object>();
		if (taskListenerType){
			TaskListenerTypeEnum[] array = TaskListenerTypeEnum.class.getEnumConstants();
			for (int i = 0; i < array.length; i++) {
				list.add(array[i].toString());
			}
		}else{
			ExecutionListenerTypeEnum[] array = ExecutionListenerTypeEnum.class.getEnumConstants();
			for (int i = 0; i < array.length; i++) {
				list.add(array[i].toString());
			}
		}
			
		return list.toArray(); 
	}

	private Object[] getListenerServiceType(){
		List<Object> list = new ArrayList<Object>();
		ListenerServiceTypeEnum[] array = ListenerServiceTypeEnum.class.getEnumConstants();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i].toString());
		}		
		
		//服务端注册的
		IExtensionConfig extensionConfig= NCLocator.getInstance().lookup(IExtensionConfig.class);
		ExtensionListenerConfig[] extensionListeners = extensionConfig.getExtensionListeners();
		for (ExtensionListenerConfig config : extensionListeners) {
			if (StringUtil.isNotEmpty(config.getEvent()))
				list.add(config.name);
		}
		
		return list.toArray();
	}
	
	public void setTaskListenerType(boolean isTaskListenerType) {
		this.taskListenerType = isTaskListenerType;
	}

	private void implementationEditingStoped(ChangeEvent e, UIComboBox methodEditor) {
		String className = (String)((TableCellEditor)e.getSource()).getCellEditorValue();
		try {
			//TODO 应该在服务端返回方法集合
			implementationClass = Class.forName(className);
			Method[] methods = implementationClass.getMethods();
			List<Method> removedMethods = new ArrayList<Method>();
			methodEditor.removeAllItems();
			for(Method method:methods){
				if (method.getDeclaringClass().equals(implementationClass))
					removedMethods.add(method);
			}
			methodEditor.addItems(removedMethods.toArray());
		} catch (ClassNotFoundException e1) {
			MessageBox.showMessageDialog("提示信息","不能找到此类：不正确的类名或在前台无法实例化");
		}
	}
	
	private void methodSelectedChanged(ItemEvent e, ParameterEditor parameterEditor){
		Object methods = e.getItem();
		Method method = (Method)e.getItem();
		List<FieldExtension> fields = new ArrayList<FieldExtension>();
		Class<?>[] types = method.getParameterTypes();
		FieldExtension field = null;
		for(int i = 0; i < types.length; i++){
			field = new FieldExtension();
			field.setFieldType(types[i].getName());
			field.setFieldName("");
			field.setExpression("");
			fields.add(field);
		}
		if (((ParameterEditorDlg)parameterEditor.getOKCancelDlg()) != null)
			((ParameterEditorDlg)parameterEditor.getOKCancelDlg()).SetPropertys(fields);
	}
	
	private void installListener(){
		//listener implementation
		TableCellEditor defaultEditor = getTable().getColumnModel().getColumn(2).getCellEditor();
		defaultEditor.addCellEditorListener(new CellEditorListener(){
			@Override
			public void editingCanceled(ChangeEvent e) {
			}
			@Override
			public void editingStopped(ChangeEvent e) {
				TableCellEditor editor = getTable().getColumnModel().getColumn(3).getCellEditor();
				UIComboBox combobox = (UIComboBox)((DefaultCellEditor)editor).getComponent();
				implementationEditingStoped(e,combobox);
			}
		});

		defaultEditor = getTable().getColumnModel().getColumn(3).getCellEditor();
		UIComboBox combobox = (UIComboBox)((DefaultCellEditor)defaultEditor).getComponent();
		combobox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				TableCellEditor editor = getTable().getColumnModel().getColumn(4).getCellEditor();
				ParameterEditor parameterEditor = (ParameterEditor)((CellEditorAdapter)editor).getEditor();
				methodSelectedChanged(e,parameterEditor);
			}
		});
	}
	
	@SuppressWarnings("serial")
	public void SetCellEditor(){
		Object[] comboItem = getTaskListenerItem();
		getTable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new UIComboBox(comboItem)));
		comboItem  = getListenerServiceType();
		getTable().getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new UIComboBox(comboItem)));
		getTable().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor( new UITextField()));
		getTable().getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new UIComboBox()));
		getTable().getColumnModel().getColumn(3).setCellRenderer(new MethodRenderer());
		getTable().getColumnModel().getColumn(4).setCellEditor(new CellEditorAdapter( new ParameterEditor()));
		installListener();
	}
	
	@Override
	protected void doNew() {
		((NCTableModel)getTable().getModel()).addRow(1);
	}

	@Override
	protected void doEdit() {

	}

	@Override
	public Object assemberData() {
		Vector  vector= ((AbstractBpmnTableModel)getTable().getModel()).getDataVector();
		List<Object> list =new ArrayList<Object>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			if (taskListenerType){
				TaskListener actListener = new TaskListener();
				actListener.setEvent(rowVector.get(0).toString());
				actListener.setImplementationType(rowVector.get(1).toString());
				actListener.setImplementation(rowVector.get(2).toString());	
				actListener.setMethod(rowVector.get(3).toString());	
				if(!(rowVector.get(4) instanceof String))
					actListener.setFieldExtensions((List<FieldExtension>)rowVector.get(4));	
				list.add(actListener);
			}else{
				ExecutionListener actListener=new ExecutionListener();
				actListener.setEvent(rowVector.get(0).toString());
				actListener.setImplementationType(rowVector.get(1).toString());
				actListener.setImplementation(rowVector.get(2).toString());	
				if (rowVector.get(3) instanceof Method){
					actListener.setMethod(((Method)rowVector.get(3)).getName().toString());
				}
				if(!(rowVector.get(4) instanceof String))
					actListener.setFieldExtensions((List<FieldExtension>)rowVector.get(4));	
				list.add(actListener);
			}
					}
		return list;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unassemberData(Object intializeData) {

		//initCellEditor(); // 不同的数据编辑器不完全一样

		((AbstractBpmnTableModel) getTable().getModel()).getDataVector().clear();
		if (intializeData == null)
			return;
		List<EventListener> list = (List<EventListener>) intializeData;
		for (Object obj : list) {
			Vector<Object> rowVector = new Vector<Object>();
			EventListener actListener = (EventListener) obj;
			rowVector.add(actListener.getEvent());
			rowVector.add(actListener.getImplementationType());
			rowVector.add(actListener.getImplementation());
			rowVector.add(actListener.getMethod());
			rowVector.add(actListener.getFieldExtensions());
			rowVector.add(obj);
			((AbstractBpmnTableModel) getTable().getModel()).addRow(rowVector);
		}

	}

	class MethodRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			
			if (value != null && value instanceof Method) {
				value = ((Method)value).getName();
			}

			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
