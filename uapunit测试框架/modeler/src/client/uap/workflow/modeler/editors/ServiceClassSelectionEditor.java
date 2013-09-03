package uap.workflow.modeler.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

import javax.swing.JTable;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;

import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.FormSettingDlg;
import uap.workflow.modeler.uecomponent.ServiceClassSelectionDlg;
import uap.workflow.modeler.utils.ServiceImplementTypeEnum;


public class ServiceClassSelectionEditor extends DefaultBpmnPropertyEditor implements ICustomRendererEdit{
	protected UIDialog dlg ;
	private UIPanel panel;
	protected TextField label;
	private UIButton button;

	protected BpmnPropertyEditingDlg initializeDlg(Container parent){
		return new ServiceClassSelectionDlg(parent, ServiceClassSelectionEditor.this);
	}
	
	public ServiceClassSelectionEditor(){
		panel =new UIPanel(new BorderLayout());
		label =new TextField("");
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
		label.addKeyListener(new   KeyListener()   {
            public   void   keyReleased(KeyEvent e)  {};
            public   void   keyTyped(KeyEvent  e)  {};
            public   void   keyPressed(KeyEvent   e)   {
                if   (e.getKeyCode()   ==   KeyEvent.VK_ENTER)   {
                	setValue(label.getText());
                }
                
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
	protected void doButtonClick(){
		((ServiceClassSelectionDlg)dlg).SetPropertys(((ServiceClassSelectionDlg)dlg).getServiceClass());
		//dlg.setVisible(true);
		dlg.showModal();
	}

	
	@Override
	public DefaultCellRenderer getRender()
	{

		return   new DefaultCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				return this;
			}
		};
			
	}
	protected void performedButtonClicked(){
		dlg =initializeDlg(panel);
		doButtonClick();
		if (dlg!=null)
			label.setText(((ServiceClassSelectionDlg)dlg).getServiceClass());
			setValue(label.getText());
	}
	
}
