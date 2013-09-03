package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;

import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.ValidationException;

import org.drools.lang.DRLParser.string_list_return;

import uap.workflow.engine.form.FormProperty;
import uap.workflow.engine.form.FormPropertyImpl;
import uap.workflow.engine.itf.IFormTransferService;

public class CommonFormDialog extends UIDialog {

	private static final long serialVersionUID = -6499090231891013839L;
	
	private static int COMPONENT_HEIGHT = 24;
	private static int COMPONENT_WIDTH = 100;

	private UIPanel pnlHeader;
	private UILabel labelTitle;
	private UIPanel pnlBody;
	private UIPanel pnlFooter;
	private UIButton btnOK;
	private UIButton btnCancel;
	
	private String  pkTaskId;
	private Object bodyContent;
	private String pkProcDefId;
	
	
	private FormPropertyTableModel tableModel = null;
	
	@SuppressWarnings("deprecation")
	public CommonFormDialog(Container parent, String pkTaskId, String pkProcDefId) {
		this.setTitle("Form Editor");
		this.pkTaskId = pkTaskId;
		this.pkProcDefId = pkProcDefId;
		this.setPreferredSize(new Dimension(640,480));
		this.setResizable(true);
		getContentComponentInfo();
		initialize();
		installListener();
	}

	private void getContentComponentInfo(){
		IFormTransferService formService = NCLocator.getInstance().lookup(IFormTransferService.class);
		if(pkTaskId == null){
			bodyContent = formService.getStartFormProperties(pkProcDefId);
		}else{
			bodyContent = formService.getTaskFormProperties(pkTaskId);
		}
	}
	
	private void initialize(){
		setLayout(new BorderLayout());
		
		pnlHeader = new UIPanel();
		pnlHeader.setLayout(new BorderLayout());
		labelTitle = new UILabel("Message");
		labelTitle.setPreferredSize(new Dimension(COMPONENT_WIDTH,0));
		pnlHeader.add(labelTitle,BorderLayout.CENTER);
		this.add(pnlHeader, BorderLayout.NORTH);

		pnlFooter = new UIPanel();
		btnOK = new UIButton("OK");
		btnCancel = new UIButton("Cancel");
		btnOK.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		btnCancel.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlFooter.add(btnOK);
		pnlFooter.add(btnCancel);
		this.add(pnlFooter, BorderLayout.SOUTH);
		pnlFooter.setVisible(true);
		pnlFooter.validate();
		pnlFooter.setPreferredSize(new Dimension(200,COMPONENT_HEIGHT+4));

	
//		UIScrollPane scrollPane = new UIScrollPane();
//		scrollPane.setLayout(new ScrollPaneLayout());
//		scrollPane.setViewportView(pnlBody);
//		scrollPane.setVisible(true);
//		scrollPane.validate();
		pnlBody = new UIPanel();
		pnlBody.setLayout(new BorderLayout());
		//pnlBody.setPreferredSize(new Dimension(200,100));
		if (bodyContent instanceof List){
			FillField((List)bodyContent);
		}else if (bodyContent instanceof String){
			FillTemplate(bodyContent);
		}
		pnlBody.setVisible(true);
		pnlBody.validate();
		this.add(pnlBody, BorderLayout.CENTER);
		//scrollPane.add(pnlBody);
//		this.add(scrollPane,BorderLayout.CENTER);

	}
	
	private void submitFormData(){
		
		List<FormProperty> propertyList = tableModel.getFormProperties();
		Map<String, String> properties = new HashMap<String, String>();
		for(FormProperty property : propertyList){
			properties.put(property.getId(), property.getValue());
		}
		
//		String value = null;
//		Map<String, String> properties = new HashMap<String, String>();
//		Component[] components = pnlBody.getComponents();
//		for(Component component : components){
//			value = null;
//			if(component instanceof UITextField){
//				value = ((UITextField)component).getText();
//			}else if(component instanceof UITextField){
//				value = ((UITextField)component).getText();
//			}else if(component instanceof UITextField){
//				value = ((UIComboBox)component).getSelectedItem().toString();
//			}else if(component instanceof UICheckBox){
//				value = ((UICheckBox)component).getText();
//			}
//			if (value != null){
//				properties.put(component.getName(), value);
//			}
//		}
		
		IFormTransferService formService = NCLocator.getInstance().lookup(IFormTransferService.class);
		if (pkTaskId == null){		//Start form
			formService.submitStartFormData(pkProcDefId, properties);
		}else{						//Task form
			formService.submitTaskFormData(pkTaskId, properties);
		}
	}
	
	private void installListener(){
		btnOK.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPerformedOK(e);
			}
		});
		
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionPerformedCancel(e);
			}
		});
	}
	
	private void actionPerformedOK(ActionEvent e){
		submitFormData();
		this.closeOK();		
	}

	private void actionPerformedCancel(ActionEvent e){ 				
		this.closeCancel();	
	}

	private void FillField(List<FormPropertyImpl> properties){
		tableModel = new FormPropertyTableModel();
		tableModel.setFormProperties(properties);
		
		UITablePane tablePane = new UITablePane();
		tablePane.setPreferredSize(new Dimension(200, 200));
		tablePane.getTable().setModel(tableModel);
		
		pnlBody.add(tablePane, BorderLayout.CENTER);
//		
//		
//		Component component = null;
//		UILabel label = null;
//		UITextField textField = null;
//		UICheckBox checkBox = null;
//		UIComboBox comboBox = null; 
//		for(FormProperty property : properties){
//			label = new UILabel(property.getName());
//			label.setName("label" + property.getId());
//			label.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
//			pnlBody.add(label);
//			
//			component = null;
//			if(property.getType().getName()=="text"){
//				component = textField = new UITextField();
//				textField.setValue(property.getValue());
//			}else if(property.getType().getName()=="date"){
//				component = textField = new UITextField();
//			}else if(property.getType().getName()=="enum"){
//				component = comboBox = new UIComboBox();
//				comboBox.addItem("test");
//			}else if(property.getType().getName()=="boolean"){
//				component = checkBox = new UICheckBox();
//				checkBox.setSelected(Boolean.parseBoolean(property.getValue()));
//			}else{
//				component = textField = new UITextField();
//				textField.setValue(property.getValue());
//			}
//			if (component != null){
//				component.setPreferredSize(new Dimension(20,50));
//				component.setName(property.getName());
//				component.setVisible(true);
//				pnlBody.add(component);
//			}
//		}
	}
	
	private void FillTemplate(Object templateString){
		//解析内容，将不支持
		UITextArea textArea = new UITextArea();
		textArea.setText(templateString.toString());
		pnlBody.add(textArea);
	}	
	
	
	class FormPropertyTableModel extends AbstractTableModel {
		
		List<FormProperty> propertiesList = new ArrayList<FormProperty>();
		
		private String[] COLUMN_NAMES = new String[] {
				"name",
				"value"
		};

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}
		
		@Override
		public String getColumnName(int arg0) {
			return COLUMN_NAMES[arg0];
		}

		@Override
		public int getRowCount() {
			return propertiesList.size();
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return false;
			} else {
				FormProperty p = propertiesList.get(row);
				return p.isWritable();
			}
		}

		@Override
		public Object getValueAt(int row, int column) {
			FormProperty p = propertiesList.get(row);
			
			switch (column) {
			case 0:
				return p.getName();
			case 1:
				return p.getValue();
			default:
				return null;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			if (column != 1) {
				return;
			}
			
			FormProperty p = propertiesList.get(row);
			((FormPropertyImpl) p).setValue((String) value);
		}
		
		public void setFormProperties(List<FormPropertyImpl> properties) {
			propertiesList.clear();
			propertiesList.addAll(properties);
		}
		
		public List<FormProperty> getFormProperties() {
			return propertiesList;
		}
		
		public void validate() throws ValidationException {
			for (FormProperty p : propertiesList) {
				if (p.isRequired() && StringUtil.isEmptyWithTrim(p.getValue())) {
					throw new ValidationException(p.getName() + " is null");
				}
			}
		}
		
	}
}
