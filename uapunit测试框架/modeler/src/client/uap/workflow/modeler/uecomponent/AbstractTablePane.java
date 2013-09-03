package uap.workflow.modeler.uecomponent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;

public abstract class AbstractTablePane extends UIPanel implements IAssemberPropertyData{
    
	private UITable  table ;
	
	private UIButton btn_new;
	
	private UIButton btn_editor;
	
	private UIButton btn_remove;
	
	private UIButton btn_up;
	
	private UIButton btn_down;
	
	private UIPanel actionPanel;
	
	private UIScrollPane scrollpane;

	
	public AbstractTablePane(NCTableModel model,int[] columnWidth){	
		table =new UITable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setColumnWidth(columnWidth);
		initialize();
	}
	
	
	protected void initCellEditor(){
		
	}
	
	
	private void initialize(){		
		setLayout(new BorderLayout());
		scrollpane =new UIScrollPane(table);	
//		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		add(scrollpane, BorderLayout.CENTER);
		add(getActionPanel(),BorderLayout.EAST);
		initListeners();
		initCellEditor();
	}
	
	private void initListeners(){
		
		
		getNewBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doNew();
			}
		});
		
		getEditorBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doEdit();
			}
		});
		
		getRemoveBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		});
		
		getDownBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doDown();
			}
		});
		
		getUpBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doUp();
			}
		});
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
			btn_remove =new UIButton("Remove");
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
	
	private AbstractBpmnTableModel getTableModel(){
		return (AbstractBpmnTableModel)table.getModel();
	}
	
	public UITable getTable(){
		return table;
	}
	
	//新建
	protected void doNew(){
		((NCTableModel)(table.getModel())).addRow(1);
	}
	//编辑
	protected abstract void doEdit();
//	//返回table中的内容
//    protected List getTableContent(){
//    	return (List)getTableModel().assemberData();
//    };
//	//设置table中的内容
//	protected void setTableContent(List list){
//		getTableModel().unassemberData(list);
//	};
	
	private void doRemove(){
		int selectRow =table.getSelectedRow();
		if(selectRow==-1)
			return;
		((NCTableModel)(table.getModel())).removeRow(selectRow);
	}
	
	private void doUp(){
		int selectRow =table.getSelectedRow();
		if(selectRow==-1)
			return;
		((NCTableModel)(table.getModel())).moveRow(selectRow,selectRow,selectRow==0?0:selectRow-1);
	}
	
	private void doDown(){
		int selectRow =table.getSelectedRow();
		if(selectRow==-1)
			return;
		((NCTableModel)(table.getModel())).moveRow(selectRow,selectRow,selectRow==table.getRowCount()-1?table.getRowCount()-1:selectRow+1);
	}
	
	protected boolean doAfterOKButtonClicked(){
		table.clearSelection();
		table.getSelectionModel().setLeadSelectionIndex(-1);
		stopEditing();
		return true;
	}
	
	protected void doAfterCancelButtonClicked(){
		table.clearSelection();
		table.getSelectionModel().setLeadSelectionIndex(-1);
		cancelEditing();
	}
	
	private void stopEditing(){
		int columnCount = getTable().getColumnCount();
		int rowCount = this.getTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				TableCellEditor tce = this.getTable().getCellEditor();
				if (tce != null && (tce instanceof DefaultCellEditor)) {
					DefaultCellEditor cellEditor = (DefaultCellEditor)tce ;
					cellEditor.stopCellEditing();
				}
			}
		}
	}
	
	private void cancelEditing(){
		int columnCount = getTable().getColumnCount();
		int rowCount = this.getTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				TableCellEditor tce = this.getTable().getCellEditor();
				if (tce != null && (tce instanceof DefaultCellEditor)) {
					DefaultCellEditor cellEditor = (DefaultCellEditor) tce;
					cellEditor.cancelCellEditing();
				}
			}
		}
	}
	
	
		
	
}
