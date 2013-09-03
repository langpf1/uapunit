package uap.workflow.modeler.uecomponent;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import uap.workflow.bpmn2.model.MultiInstanceLoopCharacteristics;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.editors.ExpressionEditor;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;

public class MultiInstancePanel extends UIPanel implements IAssemberPropertyData{
	
	private UILabel sequential  =new UILabel("Sequential");
	
	private UILabel loopCardinality =new UILabel("LoopCardinality");
	
	private UILabel dataCollection =new UILabel("Data Collection");
	
	private  UILabel dataItem =new UILabel("Data Item");
	
	private UILabel completionCondition =new UILabel("Completion Condition");
	
	private UIRadioButton yes =new UIRadioButton("yes");
	
	private UIRadioButton no =new UIRadioButton("no");
	
	private UIPanel radioPanel;
	
	private ExpressionEditor tx_collection =new ExpressionEditor();
	
	private UITextField tx_loop =new UITextField();
	
	private UITextField tx_dataItem =new UITextField();
	
	private ExpressionEditor tx_condition = new ExpressionEditor();
	
	private MultiInstanceLoopCharacteristics  multiInstanceFeature;
	
	public MultiInstancePanel(){
		this(null);
	}

	public MultiInstancePanel(MultiInstanceLoopCharacteristics multiInstance){
		super();
		this.multiInstanceFeature =multiInstance;
		initialize();
	}
	
	private void loadData(){
		if(multiInstanceFeature==null){
			yes.setSelected(false);
			no.setSelected(true);
			tx_collection.setValue(null);
			tx_loop.setText(null);
			tx_dataItem.setText(null);
			tx_condition.setValue(null);
			return;
		}
			
		yes.setSelected(multiInstanceFeature.isSequential());
		no.setSelected(!multiInstanceFeature.isSequential());
		tx_collection.setValue(multiInstanceFeature.getLoopDataInputRef());
		tx_loop.setText(multiInstanceFeature.getLoopCardinality());
		tx_dataItem.setText(multiInstanceFeature.getInputDataItem().getDataItem());
		tx_condition.setValue(multiInstanceFeature.getCompletionCondition().toString());
	}
	
	private void builUI(){

		radioPanel =new UIPanel();
		GroupLayout layout =new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		setLayout(layout);
		GroupLayout.SequentialGroup hGroup= layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().addComponent(sequential).addComponent(loopCardinality).addComponent(dataCollection).addComponent(dataItem).addComponent(completionCondition));
		hGroup.addGroup(layout.createParallelGroup().addComponent(radioPanel).addComponent(tx_loop).addComponent(tx_collection.getCustomEditor()).addComponent(tx_dataItem).addComponent(tx_condition.getCustomEditor()));
		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(50).addComponent(sequential).addGap(50).addComponent(radioPanel));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(50).addComponent(loopCardinality).addGap(50).addComponent(tx_loop));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(34).addComponent(dataCollection).addGap(30).addComponent(tx_collection.getCustomEditor()));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(50).addComponent(dataItem).addGap(50).addComponent(tx_dataItem));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(34).addComponent(completionCondition).addGap(30).addComponent(tx_condition.getCustomEditor()));
		layout.setVerticalGroup(vGroup);
		radioPanel.setPreferredSize(new Dimension(tx_collection.getCustomEditor().getWidth(),tx_collection.getCustomEditor().getHeight()));
		radioPanel.setMaximumSize(new Dimension(tx_collection.getCustomEditor().getWidth(),tx_collection.getCustomEditor().getHeight()));
		radioPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
		ButtonGroup group =new ButtonGroup();
		group.add(yes);
		group.add(no);
		radioPanel.add(yes);
		radioPanel.add(no);
		
		BpmnGraphComponent graphComponent = BpmnGraphComponent.getGraphComponentObject();
		tx_collection.setGraphComponent(graphComponent);
		tx_condition.setGraphComponent(graphComponent);

	}
	
	private void initialize(){
		builUI();
		loadData();
	}
	
	@Override
	public Object assemberData() {
		MultiInstanceLoopCharacteristics result =new MultiInstanceLoopCharacteristics();
		result.setSequential(yes.isSelected());
		result.setCompletionCondition(tx_condition.getAsText().trim());
		result.setLoopDataInputRef(tx_collection.getValue().toString().trim());
		result.setLoopCardinality(tx_loop.getText().trim());
		result.getInputDataItem().setDataItem(tx_dataItem.getText().trim());
		return result;
	}

	@Override
	public void unassemberData(Object intializeData) {
		multiInstanceFeature =(MultiInstanceLoopCharacteristics) intializeData;
		loadData();
		
	}
}
