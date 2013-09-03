package uap.workflow.modeler.bpmn.graph;

import java.awt.Point;
import java.util.Map;

import uap.workflow.bpmn2.model.Activity;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Group;
import uap.workflow.bpmn2.model.Lane;
import uap.workflow.bpmn2.model.Pool;
import uap.workflow.bpmn2.model.SubProcess;
import uap.workflow.bpmn2.model.event.EndEvent;
import uap.workflow.bpmn2.model.event.StartEvent;
import uap.workflow.modeler.uecomponent.UfGraphCell;
import nc.vo.pub.graph.element.AbstractGraphObject;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class BpmnGraph extends mxGraph {

	protected Object edgeTemplate;
	
	public BpmnGraph() {
		super();
		initializeDefaultStyle();
		//new BpmnParser2Diagram(memeryModel,this).parser2Diagram();
		//addCell(new mxCell(UserObjectFactory.getTask(BpmnTaskTypeEnum.UserTask, "3333"),new mxGeometry(100, 100, 300, 280), "label;image=/themeroot/blue/themeres/control/activiti/type.user.png;fontFamily=宋体;fontSize=12;imageVerticalAlign=top;verticalAlign=middle;verticalLabelPosition=bottom;rounded=1"));
	}
	
	private void initializeDefaultStyle(){
		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setAllowDanglingEdges(false);
		setCellsEditable(true);
		setDisconnectOnMove(false);
	}	

	public void setEdgeTemplate(Object template) {
		edgeTemplate = template;
	}
	
	public String getToolTipForCell(Object cell) {
		
		String tip = "";
		if (cell instanceof UfGraphCell) {
			Object obj = ((UfGraphCell) cell).getValue();
			if (obj instanceof AbstractGraphObject) {
				tip = ((AbstractGraphObject) obj).getDisplayName();
			}
		}
		return tip;
	}
	
	
	public Object[] moveCells(Object[] cells, double dx, double dy,
			boolean clone, Object target, Point location)
	{
		if (cells != null && (dx != 0 || dy != 0 || clone || target != null))
		{
			model.beginUpdate();
			try
			{
				if (clone)
				{
					cells = cloneCells(cells, isCloneInvalidEdges());

					if (target == null)
					{
						target = getDefaultParent();
					}
				}

				cellsMoved(cells, dx, dy, !clone && isDisconnectOnMove()
						&& isAllowDanglingEdges(), target == null);
				//原始的parent
				Object originalParent =((mxCell)cells[0]).getParent();			//仅仅移动单元，不会增加，后续只需要调整parent
				if (target != null)
				{
					Integer index = model.getChildCount(target);	
					cellsAdded(cells, target, index, null, null, true);
				}

				fireEvent(new mxEventObject(mxEvent.MOVE_CELLS, "cells", cells,
						"dx", dx, "dy", dy, "clone", clone, "target", target, "originalParent",originalParent,
						"location", location));
			}
			finally
			{
				model.endUpdate();
			}
		}

		return cells;
	}
	

	public String getLabel(Object cell) {
		String result = "";

		if (cell != null) {
			mxCellState state = view.getState(cell);
			Map<String, Object> style = (state != null) ? state.getStyle() : getCellStyle(cell);

			if (labelsVisible && !mxUtils.isTrue(style, mxConstants.STYLE_NOLABEL, false)) {
				result = convertValueToString(cell);
			}
			state.setLabel(result);
		}

		return result;
	}

	public Object[] setCellStyle(String style, Object[] cells) {
		if (cells == null) {
			cells = getSelectionCells();
		}

		if (cells != null) {
			model.beginUpdate();
			try {
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof mxICell && ((mxICell) cells[i]).isEdge())
						model.setStyle(cells[i], style);
				}
			} finally {
				model.endUpdate();
			}
		}

		return cells;
	}
	
	@Override
	public Object createEdge(Object parent, String id, Object value, Object source, Object target, String style) {
		mxCell edge = new UfGraphCell(value, new mxGeometry(), style);
		edge.setId(id);
		edge.setEdge(true);
		edge.getGeometry().setRelative(true);
		return edge;
	}
	@Override
	public Object createVertex(Object parent, String id, Object value,
			double x, double y, double width, double height, String style,
			boolean relative)
	{
		mxGeometry geometry = new mxGeometry(x, y, width, height);
		geometry.setRelative(relative);
		mxCell vertex = new UfGraphCell(value, geometry, style);
		vertex.setId(id);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}
	
	@Override
	public boolean isValidDropTarget(Object cell, Object[] cells) {
		if (cell != null){//活动的边也是拖拽的目标					
			Object objCell = ((mxCell)cell).getValue();
			if (objCell instanceof SubProcess){
				if (cells != null && cells.length > 0)
					return true; 
			}
			
		}
		
		return cell != null
		&& ((isSplitEnabled() && isSplitTarget(cell, cells)) || (!model
				.isEdge(cell) && (isSwimlane(cell) || isSwimPool(cell) || (model
				.getChildCount(cell) > 0 && !isCellCollapsed(cell)))));
		//return super.isValidDropTarget(cell, cells);
	}
	
	//需要重写
	public boolean isSplitTarget(Object target, Object[] cells){
		return super.isSplitTarget(target, cells);
	}
	//需要重写
	public boolean isValidSource(Object cell)
	{
		boolean isValidSource = (cell == null && allowDanglingEdges)|| (cell != null&& (!model.isEdge(cell) || isConnectableEdges()) && isCellConnectable(cell));
		if(isValidSource){
			isValidSource = !(((mxCell)cell).getValue() instanceof EndEvent)&&!(((mxCell)cell).getValue() instanceof Group);
		}
		return isValidSource;
	}
	//需要重写
	public boolean isValidTarget(Object cell)
	{
		boolean isValidTarget = (cell == null && allowDanglingEdges)|| (cell != null&& (!model.isEdge(cell) || isConnectableEdges()) && isCellConnectable(cell));
		if(isValidTarget){
			isValidTarget = !(((mxCell)cell).getValue() instanceof StartEvent)&&!(((mxCell)cell).getValue() instanceof Group);
		}
		return isValidTarget;
	}
	
	public void cellLabelChanged(Object cell, Object value, boolean autoSize)
	{
		model.beginUpdate();
		try
		{
			BaseElement oldValue=  ((BaseElement) ((mxCell)cell).getValue());
			getModel().setValue(cell, value);

			if (autoSize)
			{
				cellSizeUpdated(cell, false);
			}
			
			fireEvent(new mxEventObject(mxEvent.LABEL_CHANGED,
					"cell", cell, "value", value, "oldvalue",oldValue));
		}
		finally
		{
			model.endUpdate();
		}
	}
	
	public boolean isCanBound(Object cell){
		boolean canBound = false;
		if(!canBound && cell != null && ((mxCell)cell).getValue() != null ){
			canBound =((BaseElement)((mxCell)cell).getValue()).isCanBound();
		}
		return canBound;
	}

	public boolean isContainer(Object cell){
		boolean isContainer = false;
		if(!isContainer && cell!=null && ((mxCell)cell).getValue()!=null ){
			isContainer =((BaseElement)((mxCell)cell).getValue()).isContainer();
		}
		return isContainer;
	}
	
	public boolean isSwimPool(Object cell){
		return (cell != null && ((mxCell)cell).getValue() instanceof Pool);
	}
	/**
	 * 1.cell的style是swimlane 返回true
	 * 2.cell的((BaseElement)userObj).isContainer(),返回true
	 * 3.其他情况，返回false
	 * */
	public boolean isSwimlane(Object cell) {
		return (cell != null && ((mxCell)cell).getValue() instanceof Lane);
/*		boolean isContainer = false;
		if (cell != null) {
			if (model.getParent(cell) != model.getRoot()) {
				mxCellState state = view.getState(cell);
				Map<String, Object> style = (state != null) ? state.getStyle() : getCellStyle(cell);
				if (style != null && !model.isEdge(cell)) {
					isContainer = mxUtils.getString(style, mxConstants.STYLE_SHAPE, "").equals(mxConstants.SHAPE_SWIMLANE);
				}
			}
		}

		return isContainer;
*/
	}
	
//	public Object getDefaultParent()
//	{
//		Object parent =super.getDefaultParent();
//		//置为MainprocessID
//		((mxCell)parent).setId(memeryModel.getMainProcess().getId());
//		return parent;
//	}
	
	
	
	public Object[] moveCellsWithConnect(Object[] cells, double dx, double dy, boolean clone, Object target, Point location) {
		if (cells != null && (dx != 0 || dy != 0 || clone || target != null)) {
			model.beginUpdate();
			try {
				if (clone) {
					if (target == null) {
						target = getDefaultParent();
					}
				}

				if (target == null) {
					target = getDefaultParent();
				}

				cellsMoved(cells, dx, dy, !clone && isDisconnectOnMove() && isAllowDanglingEdges(), target == null);

				if (target != null) {
					Integer index = model.getChildCount(target);
					cellsAddedWithConnect(cells, target, index, true);
				}
				// XXX clone为true时在调用invokeMove方法时才会往getElementMap()中put元素
				clone = true;
				fireEvent(new mxEventObject(mxEvent.MOVE_CELLS, "cells", cells, "dx", dx, "dy", dy, "clone", clone, "target", target, "location", location));
			} finally {
				model.endUpdate();
			}
		}

		return cells;
	}

	public void cellsAddedWithConnect(Object[] cells, Object parent, Integer index, boolean absolute) {
		if (cells != null && parent != null && index != null) {
			model.beginUpdate();
			try {
				mxCellState parentState = (absolute) ? view.getState(parent) : null;
				mxPoint o1 = (parentState != null) ? parentState.getOrigin() : null;
				mxPoint zero = new mxPoint(0, 0);

				for (int i = 0; i < cells.length; i++) {
					Object previous = model.getParent(cells[i]);

					// Keeps the cell at its absolute location
					if (o1 != null && cells[i] != parent && parent != previous) {
						mxCellState oldState = view.getState(previous);
						mxPoint o2 = (oldState != null) ? oldState.getOrigin() : zero;
						mxGeometry geo = model.getGeometry(cells[i]);

						if (geo != null) {
							double dx = o2.getX() - o1.getX();
							double dy = o2.getY() - o1.getY();

							geo = (mxGeometry) geo.clone();
							geo.translate(dx, dy);

							if (!geo.isRelative() && model.isVertex(cells[i]) && !isAllowNegativeCoordinates()) {
								geo.setX(Math.max(0, geo.getX()));
								geo.setY(Math.max(0, geo.getY()));
							}

							model.setGeometry(cells[i], geo);
						}
					}

					// Decrements all following indices
					// if cell is already in parent
					if (parent == previous) {
						index--;
					}

					model.add(parent, cells[i], index + i);

					// Extends the parent
					if (isExtendParentsOnAdd() && isExtendParent(cells[i])) {
						extendParent(cells[i]);
					}

					// Constrains the child
					constrainChild(cells[i]);

					// Sets the source terminal
					if (((UfGraphCell) cells[i]).getSource() != null) {
						cellConnected(cells[i], ((UfGraphCell) cells[i]).getSource(), true, null);
					}
					// Sets the target terminal
					if (((UfGraphCell) cells[i]).getTarget() != null) {
						cellConnected(cells[i], ((UfGraphCell) cells[i]).getTarget(), false, null);
					}
				}

				fireEvent(new mxEventObject(mxEvent.CELLS_ADDED, "cells", cells, "parent", parent, "index", index, "source", null, "target", null, "absolute", absolute));

			} finally {
				model.endUpdate();
			}
		}
	}
}
