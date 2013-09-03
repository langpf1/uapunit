package uap.workflow.modeler.uecomponent;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import uap.workflow.modeler.utils.BpmnModelerConstants;

import nc.ui.pub.beans.UIPanel;

//有name-value两列 。行列对齐的布局面板
public class Name_ValuePanel extends UIPanel {
	
	public Name_ValuePanel(Component[] e){
		super();
		
		GroupLayout layout =new GroupLayout(this);
		
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		
		hGroup.addGroup(layout.createParallelGroup().addComponent(e[0]).addComponent(e[2]).addComponent(e[4]));
		hGroup.addGroup(layout.createParallelGroup().addComponent(e[1]).addComponent(e[3]).addComponent(e[5]));
		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(e[0]).addGap(50).addComponent(e[1]));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(e[2]).addGap(50).addComponent(e[3]));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(e[4]).addGap(50).addComponent(e[5]));
	    layout.setVerticalGroup(vGroup);
	}
	
	
}
