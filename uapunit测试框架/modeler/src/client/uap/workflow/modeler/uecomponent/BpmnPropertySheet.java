package uap.workflow.modeler.uecomponent;

import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableCellEditor;

import nc.bs.logging.Logger;
import nc.uap.ws.gen.util.StringUtil;
import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.UIComboBox;
import nc.vo.pub.graph.annotation.MonitorProp;
import nc.vo.pub.graph.element.IUfGraphUserOjbect;
import nc.vo.pub.graph.element.UfGraphCell;
import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.ComplexGateway;
import uap.workflow.bpmn2.model.ExclusiveGateway;
import uap.workflow.bpmn2.model.FieldExtension;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.Gateway;
import uap.workflow.bpmn2.model.InclusiveGateway;
import uap.workflow.bpmn2.model.SequenceFlow;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.graph.itf.ListenerType;
import uap.workflow.modeler.bpmn.listeners.GraphBeanPropChangeListener;
import uap.workflow.modeler.bpmn.listeners.IGraphElementPropChangeListener;
import uap.workflow.modeler.bpmn2.beaninfos.BaseBeanInfo;
import uap.workflow.modeler.bpmn2.beaninfos.DefaultBeanInfoResolver;
import uap.workflow.modeler.bpmn2.beaninfos.ExtendedGraphBeanDesriptor;
import uap.workflow.modeler.core.IBeanInfoProvider;
import uap.workflow.modeler.editors.DefaultBpmnPropertyEditor;
import uap.workflow.modeler.editors.MethodComboBoxEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;
import uap.workflow.modeler.sheet.ModelerPropertySheetPanel;
import uap.workflow.modeler.sheet.Property;
import uap.workflow.modeler.sheet.PropertyDescriptorAdapter;
import uap.workflow.modeler.sheet.PropertySheet;
import uap.workflow.modeler.utils.BpmnIconFactory;
import uap.workflow.modeler.utils.UserObjectFactory;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;


public class BpmnPropertySheet extends ModelerPropertySheetPanel implements IGraphListenerClaim {
	private static final long serialVersionUID = -4212144130835002571L;

	// 当前正被编辑的对象
	private Object _editingObject = null;

	private mxICell _editingCell = null;

	private List<IGraphElementPropChangeListener> alOtherPropChangeHandlers = new ArrayList<IGraphElementPropChangeListener>();
	
	private boolean _hasPropertyChangeListener =false;

	private IUfGraphUserOjbect graphOjbect;

	private Map<String, PropertyEditor> m_hmEditorCache = new HashMap<String, PropertyEditor>();

	private DefaultBeanInfoResolver _beanInfoResolver = new DefaultBeanInfoResolver();
	
	//被监视的属性发生变化
	private Map<String, ArrayList<GraphBeanPropChangeListener>> MonitorPropChangeListeners = new HashMap<String, ArrayList<GraphBeanPropChangeListener>>();
	//outline更新
	private List<GraphBeanPropChangeListener> uiPropChangeListeners = new ArrayList<GraphBeanPropChangeListener>();

	private Map<String, Property> propertyMap = new HashMap<String, Property>();

	private mxGraphComponent graphComponent;

	public BpmnPropertySheet() {
		super();
		setMode(PropertySheet.VIEW_AS_CATEGORIES);
		setToolBarVisible(true);
		setDescriptionVisible(true);
	}

	public mxGraphComponent getGraphComponent() {
		return graphComponent;
	}

	public void setGraphComponent(mxGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}
	
	public IUfGraphUserOjbect getGraphOjbect() {
		return graphOjbect;
	}

	public void setGraphOjbect(IUfGraphUserOjbect graphOjbect) {
		this.graphOjbect = graphOjbect;
	}

	public void addUIPropChangeListener(GraphBeanPropChangeListener l) {
		uiPropChangeListeners.add(l);
	}

	public void stopCellEditing() {
		int columnCount = this.getTable().getColumnCount();
		int rowCount = this.getTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				TableCellEditor tce = this.getTable().getCellEditor();
				if (tce != null) {
					CellEditorAdapter cellEditor = (CellEditorAdapter) tce;
					cellEditor.stopCellEditing();
				}
			}
		}
	}

	public Object getParentValue(String parentProperty){
		Property[] properties = getTableModel().getProperties();
		for(int j = 0; j < properties.length; j++){
			if (parentProperty.equals(properties[j].getName())){
				return properties[j].getValue();
			}
		}
		return null;
	}
	
	public void setEditingObject(mxICell editingCell, Object editingObject) {
		if (editingObject == null)
			return;
		reLoadProperties(editingCell, editingObject);
		// everytime a property change, update the userobject and cell with it
		if (!_hasPropertyChangeListener) {
			PropertyChangeListener listener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					
					Property prop = (Property) evt.getSource();
					Object editedObject = _editingObject;
					
					//有些属性值不一定来源于_editingObject
					PropertyDescriptorAdapter adapter = (PropertyDescriptorAdapter)prop;
					ExtendedGraphBeanDesriptor descriptor = (ExtendedGraphBeanDesriptor)adapter.getDescriptor();
					if (descriptor.getParentProperty() != null)
						editedObject = getParentValue(descriptor.getParentProperty());

					// 当前过程编辑器需要单独处理 属性变化事件
					BpmnPropertySheet.this.handlePropertyChange(prop, _editingCell, editedObject, evt);
					// 过程解析树等其他的 需要处理属性变化事件
					for (Iterator<?> iter = alOtherPropChangeHandlers.iterator(); iter.hasNext();) {
						IGraphElementPropChangeListener element = (IGraphElementPropChangeListener) iter.next();
						element.handlePropertyChange(prop, _editingCell, _editingObject);
					}
				}
			};
			this.addPropertySheetChangeListener(listener);
			_hasPropertyChangeListener = true;
		}

	}
	
	//重新加载属性列表
	private void reLoadProperties(mxICell editingCell, Object editingObject){
		if(this._editingObject == editingObject)
			return;
		this._editingObject = editingObject;
		this._editingCell = editingCell;
		BeanInfo beanInfo = null;
		if (_editingObject instanceof IBeanInfoProvider) {
			if (((IBeanInfoProvider) _editingObject).provideBeanInfoClass() != null)
				beanInfo = _beanInfoResolver.getBeanInfo(_editingObject);
		} else if (_editingObject instanceof BeanInfo) {
			beanInfo = (BeanInfo) _editingObject;
		}
		if (beanInfo == null)
			return;
		// 限制某些属性的编辑
		restrictSomeProperties(beanInfo);
		setProperties(beanInfo.getPropertyDescriptors());
		readFromObject(_editingObject);
		registerPropertyEditors();
	}

	//选中主流程
	public void selectMainProcessCell() {
		//setEditingObject(null,((BpmnGraphComponent)graphComponent).getMemeryModel().getMainProcess());
		setEditingObject(null,((mxCell)graphComponent.getGraph().getDefaultParent()).getValue());
	}

	@ListenerType(eventType = mxEvent.MOVE_CELLS)
	public void invokeRemoveCell(Object sender, mxEventObject evt) {
		if (evt.getName().equals(mxEvent.MOVE_CELLS)) {
			// add
			Object[] moved = (Object[]) evt.getProperty("cells");
			Boolean isCloned = (Boolean) evt.getProperty("clone");
			if (!isCloned)
				return;
			for (Object obj : moved) {
				if (((mxICell) obj).getValue() != null && !((mxICell) obj).getValue().equals(""))
					setEditingObject((mxICell) obj, ((mxICell) obj).getValue());
				notifyCellValueChanged((mxCell) obj);
			}
			doImportCell(moved);
		}
	}

	@SuppressWarnings("unchecked")
	@ListenerType(eventType = mxEvent.CHANGE)
	public void invokeSwitchCell(Object sender, mxEventObject evt) {
		/* 切换图元时候首先停止编辑 */
		stopCellEditing();
		Collection<Object> added = (Collection<Object>) evt.getProperty("added");
		Collection<Object> removed = (Collection<Object>) evt.getProperty("removed");
		if (removed == null&&added!=null) {
			//选中process
			selectMainProcessCell();
		} else {
			for (Object obj : removed) {
				mxICell selCell = null;
				if (obj instanceof Object[]) {
					Object[] cells = (Object[]) obj;
					selCell = (UfGraphCell) cells[0];
				} else {
					selCell = (mxICell) obj;
				}
					setEditingObject(selCell, selCell.getValue());
			}
		}
		doChangeCellSelection(added, removed);
	}

	@ListenerType(eventType = mxEvent.CONNECT_CELL)
	public void invokeConnectCell(Object sender, mxEventObject evt) {
		mxCell terminal = (mxCell) evt.getProperty("terminal");
		mxCell edge = (mxCell) evt.getProperty("edge");
		doConnectCell(terminal, edge);
		notifyCellValueChanged((mxCell) terminal);
		notifyCellValueChanged((mxCell) edge);
		for (GraphBeanPropChangeListener uiListener : uiPropChangeListeners)
			uiListener.valueChanged(null, null, null);
	}

	/**
	 * @see mxEvent.SPLIT_EDGE
	 * */
	@ListenerType(eventType = mxEvent.SPLIT_EDGE)
	public void invokeSplitEdge(Object sender, mxEventObject evt) {
		Object[] cells = (Object[]) evt.getProperty("cells");
		mxCell terminal = (mxCell) cells[0];
		mxCell edge = (mxCell) evt.getProperty("edge");
		mxCell newEdge = (mxCell) evt.getProperty("newEdge");
		doConnectCell(terminal, edge);
		doConnectCell(terminal, newEdge);
		notifyCellValueChanged((mxCell) terminal);
		notifyCellValueChanged((mxCell) edge);
		notifyCellValueChanged((mxCell) newEdge);
		for (GraphBeanPropChangeListener uiListener : uiPropChangeListeners)
			uiListener.valueChanged(null, null, null);
	}

	@ListenerType(eventType = mxEvent.SELECT)
	public void invokeSelect(Object sender, mxEventObject evt) {

	}

	@Override
	public String[] getListenTargetType() {
		return new String[] { mxEvent.CONNECT_CELL, mxEvent.MOVE_CELLS, mxEvent.CHANGE, mxEvent.SPLIT_EDGE, mxEvent.SELECT };
	}

	public void notifyCellValueChanged(mxCell cell) {

	}

	// 连接
	public void doConnectCell(mxCell terminal, mxCell edge) {
	};

	// 改变图元选择
	public void doChangeCellSelection(Collection<?> added, Collection<?> removed) {
	};

	//
	public void doImportCell(Object[] importedCells) {
	};
	
	//修改名称时即时更新UI；更改Type类型时将旧有userObj设置为null
	public void customePropertyChange(Property prop, mxICell graphCell, Object userObject) {
		Field field = null;
		try{
			Object editorObject = _editingObject;
			PropertyDescriptorAdapter adapter = (PropertyDescriptorAdapter)prop;
			ExtendedGraphBeanDesriptor descriptor = (ExtendedGraphBeanDesriptor)adapter.getDescriptor();
			if (descriptor.getParentProperty() != null)
				editorObject = getParentValue(descriptor.getParentProperty());

			field = editorObject.getClass().getField(prop.getName());
		}catch(Exception e){
			Logger.error(e.getMessage());
		}
		try {
			//Field field = _editingObject.getClass().getField(prop.getName());
			if (field != null && field.getAnnotation(TypeChangeMonitor.class) != null){
				Object newUserObj =userObject;
				if(graphCell==null)
					return;
				if(prop.getName().equals("name")){
					graphComponent.labelChanged(graphCell, userObject, new EventObject(graphComponent.getGraph()));
				}else if (prop.getName().equals("interrupting")){
					boolean interrupting =(Boolean)prop.getValue();
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_DASHED,String.valueOf(interrupting));
					reLoadProperties(graphCell,graphCell.getValue());
				}/*else if (prop.getName().equals("fillColor")){
					String fillColor=(String)prop.getValue();
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR,fillColor);
					reLoadProperties(graphCell,graphCell.getValue());
				}else if (prop.getName().equals("font")){
					String font=(String)prop.getValue();
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_FONTFAMILY,font);
					reLoadProperties(graphCell,graphCell.getValue());
				}*/
				else if (prop.getName().equals("implementation")){
					if (((String) prop.getValue()).equalsIgnoreCase("GenerateBill")){
						Class<?> implementationClass = Class.forName("uap.workflow.bizimpl.bizinvocation.VOExchange");
						propertyMap.get(String.valueOf(_editingObject.hashCode()) + "extendClass").setValue("uap.workflow.bizimpl.bizinvocation.VOExchange");

						Method method = implementationClass.getMethod("GenerateBillProxy",String.class);
						MethodComboBoxEditor methodEditor = (MethodComboBoxEditor) getEditorRegistry().getEditor(this.propertyMap.get(String.valueOf(_editingObject.hashCode()) + "method"));
						((UIComboBox) methodEditor.getEditor()).removeAllItems();
						((UIComboBox) methodEditor.getEditor()).addItems(new Object[]{method});
	
						List<FieldExtension> fields = new ArrayList<FieldExtension>();
						Class<?>[] types = method.getParameterTypes();
						FieldExtension fieldExtension = new FieldExtension();
						fieldExtension.setFieldType(types[0].getName());
						fieldExtension.setFieldName("");
						fieldExtension.setExpression("");
						fields.add(fieldExtension);
						Property filedExtensionProperty = this.propertyMap.get(String.valueOf(_editingObject.hashCode()) + "fieldExtensions");
						filedExtensionProperty.setValue(fields);
						propertyMap.get(String.valueOf(_editingObject.hashCode()) + "resultVariableName").setValue("generatedResult");
					}
				}
				else if(prop.getName().equals("extendClass")){												//先如此凑合
					String className = (String) prop.getValue();
					if(className.length()>6&&className.substring(className.length()-6,className.length()).equals(".class"))
						className=className.substring(0, className.length()-6);
					if (!StringUtil.isEmptyOrNull(className)) {
						try {
							// TODO 应该在服务端返回方法集合
							Class<?> implementationClass = Class.forName(className);
							Method[] methods = implementationClass.getMethods();
							List<Method> removedMethods = new ArrayList<Method>();
							MethodComboBoxEditor methodEditor = (MethodComboBoxEditor) getEditorRegistry().getEditor(this.propertyMap.get(String.valueOf(_editingObject.hashCode()) + "method"));
							((UIComboBox) methodEditor.getEditor()).removeAllItems();
							for (Method method : methods) {
								if (method.getDeclaringClass().equals(implementationClass))
									removedMethods.add(method);
							}
							((UIComboBox) methodEditor.getEditor()).addItems(removedMethods.toArray());
						} catch (ClassNotFoundException e1) {
							MessageBox.showMessageDialog("提示信息", "不能找到此类：不正确的类名或在前台无法实例化");
						}
					}
				}else if(prop.getName().equals("method")){												//先如此凑合
					MethodComboBoxEditor methodEditor = (MethodComboBoxEditor)getEditorRegistry().getEditor(this.propertyMap.get(
							String.valueOf(_editingObject.hashCode()) +"method"));
					Method method = (Method)((UIComboBox)methodEditor.getEditor()).getSelectedItem();
					List<FieldExtension> fields = new ArrayList<FieldExtension>();
					Class<?>[] types = method.getParameterTypes();
					FieldExtension fieldExtension = null;
					for(int i = 0; i < types.length; i++){
						fieldExtension = new FieldExtension();
						fieldExtension.setFieldType(types[i].getName());
						fieldExtension.setFieldName("");
						fieldExtension.setExpression("");
						fields.add(fieldExtension);
					}
					Property filedExtensionProperty = this.propertyMap.get(
							String.valueOf(_editingObject.hashCode()) + "fieldExtensions");
					//ParameterEditor parameterEditor = (ParameterEditor)getEditorRegistry().getEditor(filedExtensionProperty);
					filedExtensionProperty.setValue(fields);
					//((ParameterEditorDlg)parameterEditor.getOKCancelDlg()).SetPropertys(fields);
				}else if (prop.getName().equals("conditionExpression")){
					String arrowStyle = "hadCondition";
					if (StringUtil.isEmptyOrNull(((SequenceFlow)_editingObject).getConditionExpression()))
						arrowStyle  = "none";
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STARTARROW,arrowStyle);
					//reLoadProperties(graphCell,graphCell.getValue());
				}else if (prop.getName().equals("defaultSequenceFlow")){
					String arrowStyle = "none";
					if (((SequenceFlow) _editingObject).isDefaultSequenceFlow()) {
						arrowStyle = "sequenceFlow";
						FlowNode node = (FlowNode) ((SequenceFlow) _editingObject).getSource();
						if (node instanceof Gateway) {
							if (node instanceof ExclusiveGateway)
								((ExclusiveGateway) node).setDefaultSequenceFlow(((SequenceFlow) _editingObject).getId());
							else if (node instanceof InclusiveGateway)
								((InclusiveGateway) node).setDefaultSequenceFlow(((SequenceFlow) _editingObject).getId());
							else if (node instanceof ComplexGateway)
								((ComplexGateway) node).setDefaultSequenceFlow(((SequenceFlow) _editingObject).getId());
						}
					}
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STARTARROW, arrowStyle);
					reLoadProperties(graphCell, graphCell.getValue());
				}else{
					TypeChangeMonitor monitor = field.getAnnotation(TypeChangeMonitor.class);
					int itype =(Integer)prop.getValue();
					Method readMethod =((PropertyDescriptorAdapter)prop).getDescriptor().getReadMethod();
					int oldtype =(Integer) readMethod.invoke(userObject, (Object[])null);
					if(itype==oldtype)
						return;
					String IconURL =BpmnIconFactory.getIconURL(itype,monitor.value());
					newUserObj =UserObjectFactory.getUserObject(itype,((BaseElement)userObject).getId(),monitor.value());
					graphComponent.getGraph().setCellStyles(mxConstants.STYLE_IMAGE,IconURL);
					graphComponent.labelChanged(graphCell, newUserObj,new EventObject(graphComponent));
					reLoadProperties(graphCell,graphCell.getValue());
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		
	}	
	
	private void registerSepcialEditor(Property p) {
		try {
			Object editorObject = _editingObject;
			PropertyDescriptorAdapter adapter = (PropertyDescriptorAdapter)p;
			ExtendedGraphBeanDesriptor descriptor = (ExtendedGraphBeanDesriptor)adapter.getDescriptor();
			if (descriptor.getParentProperty() != null)
				editorObject = getParentValue(descriptor.getParentProperty());
			
			
			Field field = editorObject.getClass().getField(p.getName());
			if (field != null && field.getAnnotation(uap.workflow.bpmn2.annotation.PropEditor1.class) != null) {
				uap.workflow.bpmn2.annotation.PropEditor1 pe = field.getAnnotation(uap.workflow.bpmn2.annotation.PropEditor1.class);
				DefaultBpmnPropertyEditor editor = getEditor(p.getName(), pe.value());
				getEditorRegistry().registerEditor(p, editor);
				Method method = editor.getClass().getMethod("setType", Class.class);
				method.invoke(editor, pe.type());
				return ;
			}
			if (field != null && field.getAnnotation(PropEditor.class) != null) {
				PropEditor pe = field.getAnnotation(PropEditor.class);
				getEditorRegistry().registerEditor(p, getEditor(p.getName(), pe.value()));
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	// 注册属性编辑器
	private void registerPropertyEditors() {
		Property[] props = this.getProperties();
		for (int i = 0; i < props.length; i++) {
			String key = String.valueOf(_editingObject.hashCode()) + props[i].getName();
			propertyMap.put(key, props[i]);
			registerSepcialEditor(props[i]);
			registerMonitorListeners(props[i]);
		}
	}
	
	
	private void handlePropertyChange(Property prop, mxICell graphCell, Object userObject, PropertyChangeEvent evt) {
		prop.writeToObject(userObject);
		customePropertyChange(prop, graphCell, userObject);
		notifyCellValueChanged((mxCell) graphCell);
		for (GraphBeanPropChangeListener uiListener : uiPropChangeListeners)
			uiListener.valueChanged(prop, null, userObject);
	}

	private void cancelEditing() {
		// BUG：如果正在编辑属性，切换对象时，编辑框并未消除编辑状态？
		// 解决：取消所有CellEditor的编辑状态
		int columnCount = this.getTable().getColumnCount();
		int rowCount = this.getTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				TableCellEditor tce = this.getTable().getCellEditor();
				if (tce != null) {
					CellEditorAdapter cellEditor = (CellEditorAdapter) tce;
					cellEditor.cancelCellEditing();
				}
			}
		}
	}
	
	private DefaultBpmnPropertyEditor getEditor(String propName, String propEditorName) {
		String key = String.valueOf(_editingObject.hashCode()) + propName;
		if (m_hmEditorCache.get(key) == null) {
			DefaultBpmnPropertyEditor editor = null;
			try {
				editor = (DefaultBpmnPropertyEditor) Class.forName(propEditorName).newInstance();
				editor.setPropName(key);
				//editor.setGraphComponent((BpmnGraphComponent)graphComponent);
				editor.setOwnerObject(_editingObject);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			m_hmEditorCache.put(key, editor);
		}
		BpmnGraphComponent.setGraphComponentObject((BpmnGraphComponent)graphComponent);
		return (DefaultBpmnPropertyEditor) m_hmEditorCache.get(key);
	}

	private void registerMonitorListeners(Property p) {
		try {
			Object editorObject = _editingObject;
			PropertyDescriptorAdapter adapter = (PropertyDescriptorAdapter)p;
			ExtendedGraphBeanDesriptor descriptor = (ExtendedGraphBeanDesriptor)adapter.getDescriptor();
			if (descriptor.getParentProperty() != null)
				editorObject = getParentValue(descriptor.getParentProperty());
			
			
			Field field = editorObject.getClass().getField(p.getName());
			if (field != null && field.getAnnotation(MonitorProp.class) != null) {
				MonitorProp monitors = field.getAnnotation(MonitorProp.class);
				if (monitors.value() != null) {
					for (String prop : monitors.value())
						regValueChangeListener(prop, (GraphBeanPropChangeListener) getEditorByPropName(p.getName()));
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	private void regValueChangeListener(String propName, GraphBeanPropChangeListener l) {
		String key = String.valueOf(_editingObject.hashCode()) + propName;
		ArrayList<GraphBeanPropChangeListener> list = MonitorPropChangeListeners.get(key);
		if (list == null) {
			list = new ArrayList<GraphBeanPropChangeListener>();
			MonitorPropChangeListeners.put(key, list);
		}

		if (l != null)
			list.add(l);//
	}
	
	private DefaultBpmnPropertyEditor getEditorByPropName(String propName) {
		String key = String.valueOf(_editingObject.hashCode()) + propName;
		return (DefaultBpmnPropertyEditor) m_hmEditorCache.get(key);
	}

	/**
	 * 限制某些属性的编辑
	 * */
	private void restrictSomeProperties(BeanInfo beanInfo) {

	}

	private void hideSomeProperties(BeanInfo beanInfo, String[] propNames) {
		if (beanInfo instanceof BaseBeanInfo) {
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			String propName = null;
			List<?> al = Arrays.asList(propNames);
			for (int i = 0; i < (propertyDescriptors == null ? 0 : propertyDescriptors.length); i++) {
				propName = propertyDescriptors[i].getName();
				if (al.contains(propName))
					((BaseBeanInfo) beanInfo).removeProperty(propName);
			}
		}
	}

}
