package uap.workflow.modeler.editors;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import uap.workflow.modeler.uecomponent.Table4Editor;

import nc.ui.pf.workitem.ApproveLangUtil;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;



public class TableEditor extends DefaultBpmnPropertyEditor{
	
	private UITable  table ;
	
	private UIButton btn_new;
	
	private UIButton btn_editor;
	
	private UIButton btn_remove;
	
	private UIButton btn_up;
	
	private UIButton btn_down;
	
	private UIPanel actionPanel;
	
	private UIScrollPane scrollpane;

	
	
	public TableEditor(String[] columnName ,int[] columnWidth,Container parent){	
		editor =new UIDialog(parent);
		table =new Table4Editor(columnName,columnWidth);
		initialize();
	}
	
	public Object getValue(){
		return null;
	}
	
	public void setValue(Object value) {
		
	}
	
	

	private void initialize(){
		((UIDialog)editor).setResizable(true);
		((UIDialog)editor).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		((UIDialog)editor).setTitle(ApproveLangUtil.getApproveDealStatus());
		
		
		scrollpane =new UIScrollPane(table);
		
		Container container = ((UIDialog)editor).getContentPane();
		container.setLayout(new BorderLayout());
		container.add(scrollpane, BorderLayout.CENTER);
		container.add(getActionPanel(),BorderLayout.EAST);
		
	}
	
	
	

	
	
	private UIPanel getActionPanel(){
		if(actionPanel==null){
			actionPanel =new UIPanel();
			actionPanel.setLayout(new GridLayout(5,1));
			actionPanel.add(getNewBtn());
			actionPanel.add(getEditorBtn());
			actionPanel.add(getRemoveBtn());
			actionPanel.add(getUpBtn());
			actionPanel.add(getDownBtn());
		}
		return actionPanel;
	}
	
	
	
	private UIButton getNewBtn(){
		if(btn_new==null){
			btn_new =new UIButton("New");
		}
		return btn_new;
	}
	
	private UIButton getEditorBtn(){
		if(btn_editor==null){
			btn_editor =new UIButton("Eidtor");
		}
		return btn_editor;
	}
	
	private UIButton getRemoveBtn(){
		if(btn_remove==null){
			btn_remove =new UIButton("Rmove");
		}
		return btn_remove;
	}
	
	private UIButton getUpBtn(){
		if(btn_up==null){
			btn_up =new UIButton("Up");
		}
		return btn_up;
	}
	
	private UIButton getDownBtn(){
		if(btn_down==null){
			btn_down =new UIButton("Down");
		}
		return btn_down;
	}
	
	
	
	
	
}
