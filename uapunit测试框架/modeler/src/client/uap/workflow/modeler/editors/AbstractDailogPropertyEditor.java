package uap.workflow.modeler.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTable;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;

public abstract class AbstractDailogPropertyEditor extends DefaultBpmnPropertyEditor implements ICustomRendererEdit{
	
	protected UIDialog dlg ;
	private UIPanel panel;
	private UILabel label;
	private UIButton button;

	
	public AbstractDailogPropertyEditor(){
		panel =new UIPanel(new BorderLayout());
		label =new UILabel("");
		button =new UIButton("..."){

			private static final long serialVersionUID = -7954354734780179931L;

			public java.awt.Dimension getPreferredSize() {
				return new Dimension(button.getHeight(),button.getHeight());
			};
		};
		panel.add(label,BorderLayout.CENTER);
		panel.add(button,BorderLayout.EAST);
		editor =panel;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performedButtonClicked();
			}
		});
			
	}
	
	public void setValue(Object value) {
		super.setValue(value);
		if(value instanceof List)
			label.setText("[...]");
		else if(value instanceof String){
			if (value != null)
			label.setText(value.toString());
		}
	}
	
	public UIDialog getOKCancelDlg(){
		return dlg;
	}
	/**
	 * 子类重写
	 * */
	protected abstract UIDialog initializeDlg(Container parent);
	/**
	 * 子类重写
	 * */
	protected void performedButtonClicked(){
		dlg =initializeDlg(panel);
		doButtonClick();
	}
	protected abstract void doButtonClick();
	
	@Override
	public DefaultCellRenderer getRender()
	{
		return   new DefaultCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if(value instanceof List)
					setValue("[...]");
				else if(value instanceof String)
					setValue(value);
				else{
					setValue("");
				}
				return this;
			}
		};
			
	}
}
