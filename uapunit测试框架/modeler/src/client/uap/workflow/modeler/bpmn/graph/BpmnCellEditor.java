package uap.workflow.modeler.bpmn.graph;

import java.util.EventObject;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.Process;


import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.view.mxCellState;

public class BpmnCellEditor extends mxCellEditor {
	
	public BpmnCellEditor(mxGraphComponent graphComponent) {
		super(graphComponent);

	}
	
	//更新userObject的名称 
	private Object getCellWithNewName(Object cell){
		BaseElement element =(BaseElement) (((mxCell)cell).getValue());
		if(element instanceof FlowElement){
			((FlowElement)element).setName(getCurrentValue());
		}else if(element instanceof Process){
			((Process)element).setName(getCurrentValue());
		}
		return element;
	}
	
	
	public void stopEditing(boolean cancel)
	{
		if (editingCell != null)
		{
			scrollPane.transferFocusUpCycle();
			Object cell = editingCell;
			editingCell = null;

			if (!cancel)
			{
				EventObject trig = trigger;
				trigger = null;
				
				graphComponent.labelChanged(cell, getCellWithNewName(cell), trig);
			}
			else
			{
				mxCellState state = graphComponent.getGraph().getView()
						.getState(cell);
				graphComponent.redraw(state);
			}

			if (scrollPane.getParent() != null)
			{
				scrollPane.setVisible(false);
				scrollPane.getParent().remove(scrollPane);
			}

			graphComponent.requestFocusInWindow();
		}
	}
}
