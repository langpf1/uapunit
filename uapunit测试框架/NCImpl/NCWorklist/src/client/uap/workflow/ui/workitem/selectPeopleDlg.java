package uap.workflow.ui.workitem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;

@SuppressWarnings("serial")
public class selectPeopleDlg extends UIDialog{
	JButton ok;
	JButton cancel;
	private JList left;
	private JList right;
	JButton jb1; // ��>����ť
	JButton jb2; // ��<����ť
	JButton jb3; // ��>>����ť
	JButton jb4; // ��<<����ť
	DefaultListModel dm;
	DefaultListModel dm1;
	public JSplitPane one;
	public JSplitPane two;
	final ArrayList<String> activeUserNames;
	public ArrayList<String> getActiveUserNames() {
		return activeUserNames;
	}
	public selectPeopleDlg (String [] people)
	{
		this.setTitle("ָ��ִ����");
		activeUserNames=new ArrayList<String>();
	//	setBounds(500, 300, 400, 350); // ���ô��ڴ�С��λ��
		// �����߿�
		dm = new DefaultListModel();
		if(people.length>0)
		{
			for(int i=0;i<people.length;i++)
				dm.add(i, people[i]);
		}
		left = new JList(dm); // ������߿�
		// ����ұ߿�
		dm1 = new DefaultListModel();
		right = new JList(dm1); // �����ұ߿�
		// ����м䰴ť
		jb1 = new JButton(">");
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm1.addElement(left.getSelectedValue());// �����б���ѡ�е�Ԫ����������б�
				dm.removeElement(left.getSelectedValue());// �Ƴ����б�����������б��Ԫ��
			}
		});
		jb2 = new JButton("<");
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.addElement(right.getSelectedValue());// �����б���ѡ�е�Ԫ����������б�
				dm1.removeElement(right.getSelectedValue());// �Ƴ����б�����������б��Ԫ��
			}
		});
		jb3 = new JButton(">>");
		jb3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] temp = left.getSelectedValues();
				for (int k = 0; k < temp.length; k++) {
					dm1.addElement(temp[k]);
					dm.removeElement(temp[k]);
				}
			}
		});
		jb4 = new JButton("<<");
		jb4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] temp = right.getSelectedValues();
				for (int k = 0; k < temp.length; k++) {
					dm.addElement(temp[k]);
					dm1.removeElement(temp[k]);
				}
			}
		});
		
		JPanel ButtonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
		ButtonPanel.add(jb1);
		ButtonPanel.add(jb2);
		ButtonPanel.add(jb3);
		ButtonPanel.add(jb4);
		one = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, ButtonPanel);
		one.setResizeWeight(1);
		one.setDividerSize(12);
		one.setBorder(null);
		one.enable(false);
		one.addComponentListener(new ComponentAdapter() {  
		public void componentResized(ComponentEvent e) {  
		one.setDividerLocation(155);  
		 }  
		});  
		two = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, one, right);
		two.setResizeWeight(1);
		two.setDividerSize(12);
		two.setBorder(null);
		two.enable(false);
		two.addComponentListener(new ComponentAdapter() {  
			public void componentResized(ComponentEvent e) {  
			two.setDividerLocation(225);  
			 }  
			});  
		one.setMinimumSize(new Dimension(0, 0));
		two.setMinimumSize(new Dimension(0, 0));
		ok=new JButton("ȷ   ��");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dm1.size()>0)
		        {
		        	for(int i=0;i<dm1.size();i++)
		        		activeUserNames.add(dm1.get(i).toString());
		        }
				close();
				fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
				return;
				}
		});
		cancel=new JButton("ȡ   ��");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
				fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
				return;
				}
		});
		JPanel button=new JPanel(new FlowLayout());
		button.add(ok);
		button.add(cancel);	
		JSplitPane total=new JSplitPane();
		total.setOrientation(JSplitPane.VERTICAL_SPLIT);
		total.setLeftComponent(two);
		total.setRightComponent(button);
		total.setResizeWeight(1);
		total.setDividerSize(6);
		total.setBorder(null);
		total.setContinuousLayout(true);
		//total.setOneTouchExpandable(true);
		total.enable(false);
		total.setMinimumSize(new Dimension(0, 0));
		total.setDividerLocation(250);
		this.add(total);
		//this.setVisible(true);
	}
  private void  OnOk(){
	  this.dispose();
  }
}
