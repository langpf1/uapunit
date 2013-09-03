package uap.workflow.modeler.bpmn.editor.handler;

import uap.workflow.bpmn2.model.IUserObjectClone;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;

import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class BpmnConnectPreview extends mxConnectPreview {

	public BpmnConnectPreview(mxGraphComponent graphComponent) {
		super(graphComponent);
	}
	
	protected Object createCell(mxCellState startState, String style)
	{
		mxGraph graph = graphComponent.getGraph();
		mxICell cell = ((mxICell) graph
				.createEdge(null, null, getClonedSequenceFlowUserObject(),
						(startState != null) ? startState.getCell() : null,
						null, style));
		cell.setStyle(((BpmnGraphComponent)graphComponent).getSelectedEntry().getStyle()+style);
		((mxICell) startState.getCell()).insertEdge(cell, true);

		return cell;
	}
	//插入连接线
	private Object getClonedSequenceFlowUserObject(){
		return ((IUserObjectClone)((BpmnGraphComponent)graphComponent).getSelectedEntry().getValue()).replicate();
	}

}
