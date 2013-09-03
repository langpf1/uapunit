package uap.workflow.modeler.bpmn.editor.handler;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uap.workflow.bpmn2.model.IUserObjectClone;
import uap.workflow.bpmn2.model.Lane;
import uap.workflow.modeler.bpmn.graph.BpmnGraph;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.uecomponent.UfGraphCell;

import nc.ui.fd.model.Geometry;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxInsertHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class BpmnInsertHandler extends mxInsertHandler {

	public BpmnInsertHandler(mxGraphComponent graphComponent, String style) {
		super(graphComponent, style);
	}
	
	public void mousePressed(MouseEvent e)
	{
		if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed()
				&& isStartEvent(e)&&checekInsertEnable())
		{
			start(e);
			e.consume();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		
	}
	
	private boolean checekInsertEnable(){
		mxICell selectEntry= ((BpmnGraphComponent)graphComponent).getSelectedEntry();
		//
		if(selectEntry==null||selectEntry.getValue()==null||selectEntry.isEdge())
			return false;
		return true;
	}
	
	private void beforeInsertCell(){
		mxICell cell =((BpmnGraphComponent)graphComponent).getSelectedEntry();
		current =new mxRectangle();
		mxGraph graph = graphComponent.getGraph();
		double scale = graph.getView().getScale();
		mxPoint tr = graph.getView().getTranslate();
		current.setX(first.x/scale - tr.getX());
		current.setY(first.getY() / scale - tr.getY());
		current.setWidth(cell.getGeometry().getWidth() / scale);
		current.setHeight(cell.getGeometry().getHeight() / scale);
		style =cell.getStyle();
	}
	
	private Object getUserObj(){
		mxICell cell =((BpmnGraphComponent)graphComponent).getSelectedEntry();
		return cell.getValue();
	}
	
	
	public void mouseReleased(MouseEvent e)
	{
		if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed())
		{
			
			if(first==null||!checekInsertEnable())
				return;
			beforeInsertCell();			
			Object insertCell = insertCell(current,getUserObj(),e);
			eventSource.fireEvent(new mxEventObject(mxEvent.INSERT, "cell",
					insertCell));
			e.consume();
		}

		reset();
	}
	
	private Object getContainer(Object parent){
		if (parent != null && 
				!((BpmnGraph) graphComponent.getGraph()).isContainer(parent) && 
				!(graphComponent.getGraph()).isSwimlane(parent)){
			return getContainer(((mxCell)parent).getParent());
		}else
			return parent;
	}

	private Object getPool(Object obj){
		if (obj != null && 
				!((BpmnGraph) graphComponent.getGraph()).isSwimPool(obj)){
			return getPool(((mxCell)obj).getParent());
		}else
			return obj;
	}
	
	public Object insertCell(mxRectangle bounds,Object userObj, MouseEvent e)
	{
		Object obj = ((IUserObjectClone) userObj).replicate();
		Object parent = graphComponent.getCellAt(e.getX(), e.getY());
		if(obj!=null&&obj instanceof Lane)
		{
			parent=getPool(parent);
		}
		else
			parent = getContainer(parent);
		Point point = new Point(0, 0);
		mxCellState state = null;
		if (parent != null) {
			state = graphComponent.getGraph().getView().getState(parent);
			point = state.getPoint();
		}
		return graphComponent.getGraph().insertVertex(parent, null, obj,
				bounds.getX() - point.getX(), bounds.getY() - point.getY(),
				bounds.getWidth(), bounds.getHeight(), style);
	}
}
