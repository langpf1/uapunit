package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import uap.workflow.bpmn2.model.BaseElement;

import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;

public  class ExtendEditorDlg extends nc.ui.pub.beans.UIDialog implements java.awt.event.ActionListener{
	private static final long serialVersionUID = -8832520673752193116L;
	protected PropertyEditorSupport propertyEditor;
	private UIButton ivjUIButtonOK = null;
	private TextField text;
	private List<IPushOkandCancelListener> listeners = new ArrayList<IPushOkandCancelListener>();
	public ExtendEditorDlg(Container parent,PropertyEditorSupport propertyEditor,uap.workflow.bpmn2.model.Process process,BaseElement element) {
		super(parent);
		text=new TextField();
		ivjUIButtonOK=new UIButton("确定");
		ivjUIButtonOK.addActionListener(this);
		this.setLayout(new FlowLayout());
		this.getContentPane().add(text);
		this.getContentPane().add(ivjUIButtonOK);
		setSize(300,300);
		setResizable(true);
		this.propertyEditor =propertyEditor;
	}
	@Override
	public void actionPerformed(ActionEvent e) {

			onButtonOK();
	}
	public void SetPropertys(Object obj) {
		text.getText();
	}
	public Object getPropertys() {
		return text.getText();
	}
	public void onButtonOK() {
		try{
			doAfterOKButtonClicked();
			this.closeOK();
			firePushOkListener();
		}catch(Exception e){
			MessageBox.showMessageDialog("提示", e.getMessage());
		}
	}
	protected void doAfterOKButtonClicked() {
		propertyEditor.setValue(getPropertys());
		propertyEditor.firePropertyChange();
	}

	public void firePushOkListener() {
		for (IPushOkandCancelListener listener : listeners) {
			listener.pushOkStopEditing();
		}
	}
}
