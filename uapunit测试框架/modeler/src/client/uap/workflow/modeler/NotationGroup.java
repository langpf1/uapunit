package uap.workflow.modeler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import uap.workflow.modeler.uecomponent.ApproveCellLib;
import uap.workflow.modeler.uecomponent.BpmnCellLib;



public class NotationGroup {
	
	private NotationGroupChange listener = null; 

	private JToggleButton previousButton = null;
	
	public void addListener(NotationGroupChange listener){
		this.listener = listener;
	}
	
	private JToggleButton setButtonGroup(final JPanel panel, int yPos, String buttonText, String name){
		final JToggleButton btn = new JToggleButton(buttonText);
		btn.setName(name);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = yPos;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		panel.add(btn,gridBagConstraints);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToggleButtons(e);
			}
		});
		panel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				btn.setPreferredSize(new Dimension(panel.getWidth(), 23));
			}

		});
		return btn;
	}
	
	public void insertButtons(String appTitle,JPanel panelContainer, BorderLayout layout) {
		
		int yPos = 0;
		
		JPanel buttonsPanel = new JPanel();
		GridBagLayout buttonLayout = new GridBagLayout();
		buttonsPanel.setLayout(buttonLayout);
		
		panelContainer.add(buttonsPanel, BorderLayout.NORTH);
		JToggleButton btn = null;
		if(appTitle.equals("ActivitiModeler"))
		{
		    btn = setButtonGroup(buttonsPanel,yPos++,"Activity",BpmnCellLib.NOTATION_ACTIVITY);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Gateway",BpmnCellLib.NOTATION_GATEWAY);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Start Event",BpmnCellLib.NOTATION_STARTEVENT);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Intermediation Throwing",BpmnCellLib.NOTATION_INTERMEDIATIONTHROWING);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Intermediation Catching",BpmnCellLib.NOTATION_INTERMEDIATIONCATCHING);
		    btn = setButtonGroup(buttonsPanel,yPos++,"End Event",BpmnCellLib.NOTATION_ENDEVENT);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Artifacts and swimlane",BpmnCellLib.NOTATION_ARTIFACTS);
		    btn = setButtonGroup(buttonsPanel,yPos++,"Connection", BpmnCellLib.NOTATION_CONNECTION);
		    btn = setButtonGroup(buttonsPanel,yPos++, "All",BpmnCellLib.NOTATION_ALL);
		}
		else if(appTitle.equals("ApproveModeler"))
		{
			btn = setButtonGroup(buttonsPanel,yPos++,"Activity",ApproveCellLib.NOTATION_ACTIVITY);
			btn = setButtonGroup(buttonsPanel,yPos++,"Gateway",ApproveCellLib.NOTATION_GATEWAY);
			btn = setButtonGroup(buttonsPanel,yPos++,"Start Event",ApproveCellLib.NOTATION_STARTEVENT);
			btn = setButtonGroup(buttonsPanel,yPos++,"End Event",ApproveCellLib.NOTATION_ENDEVENT);
			btn = setButtonGroup(buttonsPanel,yPos++,"Artifacts and swimlane",ApproveCellLib.NOTATION_ARTIFACTS);
			btn = setButtonGroup(buttonsPanel,yPos++,"Connection", ApproveCellLib.NOTATION_CONNECTION);
			btn = setButtonGroup(buttonsPanel,yPos++, "All",ApproveCellLib.NOTATION_ALL);
		}
		btn.doClick();
	}
	
	
	private void ToggleButtons(ActionEvent e){
		JToggleButton btn = (JToggleButton)e.getSource();
		if (this.previousButton != null){
			if (previousButton == btn)
				return;
			this.previousButton.doClick();
			if (this.listener != null)
				this.listener.groupSelectedChange(previousButton.getName(), btn.getName());
		}
		previousButton = btn;
	}
}
