package uap.workflow.ui.participant.editors;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.jcom.lang.StringUtil;
import uap.workflow.app.config.ParticipantFilterTypeFactory;
import uap.workflow.app.config.ParticipantTypeFactory;
import uap.workflow.app.participant.IParticipantFilterType;
import uap.workflow.app.participant.IParticipantType;
import uap.workflow.bpmn2.model.DefaultParticipantDefinition;
import uap.workflow.bpmn2.model.DefaultParticipantFilterType;
import uap.workflow.bpmn2.model.DefaultParticipantType;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.uecomponent.AbstractTablePane;

public class ParticipantPropertyTablePane extends AbstractTablePane implements ItemListener{

	private static final long serialVersionUID = -1915237695544434728L;
	// 参与者参照
	private UIRefPane participantRef;
	private UIComboBox comboParticipantType;
	private UIComboBox comboParticipantFilterType;
	private Object oldSelectedItemValue;
	private ParticipantPropertyModel tableModel = null;

	public ParticipantPropertyTablePane(ParticipantPropertyModel model) {
		super(model, model.getColumnWidth());
		this.tableModel = model;
		
		tableModel.setRefPane(getParticipantRef());
	}

	protected void initCellEditor() {
		comboParticipantType = new UIComboBox();
		comboParticipantType.addItem("");
		// 遍历HashMap
		Map<String, IParticipantType> participantTypeMap = ParticipantTypeFactory.getInstance().getTypes();
		Iterator iterator = participantTypeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, IParticipantType> entry = (Map.Entry<String, IParticipantType>) iterator.next();
			String key = entry.getKey();
			IParticipantType value = entry.getValue();
			comboParticipantType.addItem(key);
		}
		getTable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboParticipantType));
		comboParticipantType.addItemListener(this);

		UIRefCellEditor refCellEditor = new UIRefCellEditor(getParticipantRef());
		getTable().getColumnModel().getColumn(1).setCellEditor(refCellEditor);
		
		comboParticipantFilterType = new UIComboBox();
		comboParticipantFilterType.addItem("");
		// 遍历HashMap
		Map<String, IParticipantFilterType> participantFilterTypeMap = ParticipantFilterTypeFactory.getInstance().getFilterTypes();
		iterator = participantFilterTypeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, IParticipantFilterType> entry = (Map.Entry<String, IParticipantFilterType>) iterator.next();
			String key = entry.getKey();
			IParticipantFilterType value = entry.getValue();
			comboParticipantFilterType.addItem(key);
		}
		getTable().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboParticipantFilterType));
	}

	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj == this.comboParticipantType) {
			Object selectedItemValue  = this.comboParticipantType.getSelectdItemValue();
			if(selectedItemValue == null || StringUtil.isEmpty(selectedItemValue.toString()) || oldSelectedItemValue == selectedItemValue)
				return;
			oldSelectedItemValue = selectedItemValue;
			String seletorClassName = ParticipantTypeFactory.getInstance().getSelectorClassName(selectedItemValue.toString());
			try {
				Class<RefModelWithExtInfo> clazz = (Class<RefModelWithExtInfo>) Class.forName(seletorClassName);
				RefModelWithExtInfo refModel = clazz.newInstance();
				getParticipantRef().setRefModel(refModel);
			} catch (Exception ex) {
				WorkflowLogger.error(ex.getMessage(), ex);
				throw new WorkflowRuntimeException(ex.getMessage());
			}
		}
	}	

	private UIRefPane getParticipantRef() {
		if (participantRef == null) {
			participantRef = new UIRefPane();
			participantRef.setIsCustomDefined(true);
			PFUserRefModel des_transrefModel = new PFUserRefModel("用户");
			participantRef.setRefModel(des_transrefModel);
			participantRef.setReturnCode(false);
			
			
//			participantRef.setReturnCode(true);
		}
		return participantRef;
	}
	
	@Override
	protected void doEdit() {

	}

	@Override
	public Object assemberData() {
		Vector vector = ((AbstractBpmnTableModel) getTable().getModel()).getDataVector();
		List<DefaultParticipantDefinition> list = new ArrayList<DefaultParticipantDefinition>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			DefaultParticipantDefinition participantDefinition = new DefaultParticipantDefinition();

			IParticipantType participantType = ParticipantTypeFactory.getInstance().getType(rowVector.get(0).toString());
			DefaultParticipantType defaultParticipantType = new DefaultParticipantType();
			if(participantType != null)
			{
				defaultParticipantType.setCode(participantType.getCode());
				defaultParticipantType.setName(participantType.getName());
			}
			participantDefinition.setParticipantType(defaultParticipantType);
			
//			TableCellEditor cellEditor = getTable().getCellEditor(start, 1);
//			String participantID = ((UIRefPane)((UIRefCellEditor)cellEditor).getComponent()).getRefPK();
//			String participantCode = ((UIRefPane)((UIRefCellEditor)cellEditor).getComponent()).getRefCode();
//			String participantName = ((UIRefPane)((UIRefCellEditor)cellEditor).getComponent()).getRefName();
			if(rowVector.get(3) != null){
				participantDefinition.setParticipantID(rowVector.get(3).toString());
			}
			if(rowVector.get(4) != null){
				participantDefinition.setCode(rowVector.get(4).toString());
			}
			if(rowVector.get(1) != null){
				participantDefinition.setName(rowVector.get(1).toString());
			}
			
			IParticipantFilterType participantFilterType = ParticipantFilterTypeFactory.getInstance().getFilterType(rowVector.get(2).toString());
			DefaultParticipantFilterType defaultParticipantFilterType = new DefaultParticipantFilterType();
			if(participantFilterType != null)
			{
				defaultParticipantFilterType.setCode(participantFilterType.getCode());
				defaultParticipantFilterType.setName(participantFilterType.getName());
			}
			participantDefinition.setParticipantFilterType(defaultParticipantFilterType);

			list.add(participantDefinition);
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
			DefaultParticipantDefinition participantDefinition = (DefaultParticipantDefinition) obj;
			rowVector.add(participantDefinition.getParticipantType().getCode());
			rowVector.add(participantDefinition.getName());
			rowVector.add(participantDefinition.getParticipantFilterType().getCode());
			rowVector.add(participantDefinition.getID());
			rowVector.add(participantDefinition.getCode());
			((AbstractBpmnTableModel) getTable().getModel()).getDataVector().add(rowVector);
		}

	}
}
