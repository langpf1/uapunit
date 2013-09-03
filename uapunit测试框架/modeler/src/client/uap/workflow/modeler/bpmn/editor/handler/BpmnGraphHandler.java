package uap.workflow.modeler.bpmn.editor.handler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.TransferHandler;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.event.BoundaryEvent;
import uap.workflow.bpmn2.model.event.IntermediateCatchEvent;
import uap.workflow.modeler.bpmn.graph.BpmnGraph;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.editors.BoundaryMarker;
import uap.workflow.modeler.uecomponent.UfGraphCell;
import uap.workflow.modeler.utils.BpmnCursorFactory;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.handler.mxMovePreview;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class BpmnGraphHandler extends mxGraphHandler {

	private BoundaryMarker boundaryMarker;

	private boolean boundaryActived = true;
	private mxCell boundaryCell = null;	
	public BpmnGraphHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
		
		boundaryMarker = createBoundaryMarker();
	}

	public void mouseMoved(MouseEvent e) {
		if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed()) {
			Cursor cursor = getCursor(e);

			if (cursor != null) {
				graphComponent.getGraphControl().setCursor(cursor);
				e.consume();
			} else {
				graphComponent.getGraphControl().setCursor(DEFAULT_CURSOR);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
		mxICell cell = ((BpmnGraphComponent)graphComponent).getSelectedEntry();	//不是插入单元，而是移动单元
		if ( cell == null || cell.getValue() == null){
			super.mousePressed(e);
		}
	}
	@Override
	public void start(MouseEvent e) {
		this.setLivePreview(true);
		super.start(e);
		//this.movePreview.start(e, null);
	}
	
	/*private Object getContainer(Object parent){
		if (parent != null && 
				!((BpmnGraph) graphComponent.getGraph()).isContainer(parent) && 
				!(graphComponent.getGraph()).isSwimlane(parent)){
			return getContainer(((mxCell)parent).getParent());
		}else
			return parent;
	}*/
	
	@Override
	public void mouseDragged(MouseEvent e) {
	
		//super.mouseDragged(e);
		///*
		boolean gridEnabledEvent = false;
		
		if (graphComponent.isAutoScroll())
		{
			graphComponent.getGraphControl().scrollRectToVisible(
					new Rectangle(e.getPoint()));
		}

		if (!e.isConsumed())
		{
			gridEnabledEvent = graphComponent.isGridEnabledEvent(e);
			constrainedEvent = graphComponent.isConstrainedEvent(e);

			if (constrainedEvent && first != null)
			{
				int x = e.getX();
				int y = e.getY();

				if (Math.abs(e.getX() - first.x) > Math.abs(e.getY() - first.y))
				{
					y = first.y;
				}
				else
				{
					x = first.x;
				}

				e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(),
						e.getModifiers(), x, y, e.getClickCount(),
						e.isPopupTrigger(), e.getButton());
			}

			if (isVisible() && isMarkerEnabled())
			{
				//marker.process(e);

				mxCellState state = null;
				if (isEnabled())
				{
					mxCell dragedCell = (mxCell)initialCell;
					boundaryCell = (mxCell)graphComponent.getCellAt(e.getX(), e.getY(),true);
				//	boundaryCell = (mxCell) getContainer(boundaryCell);
					
					if (boundaryCell != null && !boundaryCell.equals(dragedCell)){
						mxGraphView view = graphComponent.getGraph().getView();
						state = view.getState(boundaryCell);
						Color color = null;
						boolean valid = (state != null);
						if(dragedCell != null && dragedCell.getParent().equals(boundaryCell)){
							boundaryActived = false;
						}else{
							Rectangle rectboundaryCell = boundaryCell.getGeometry().getRectangle();
							if(dragedCell == null){
								dragedCell = (mxCell)((BpmnGraphComponent)graphComponent).getSelectedEntry();
							}
							Rectangle rectDragedCell = new Rectangle(0,0,36,36);
							if (dragedCell != null) 
								rectDragedCell = dragedCell.getGeometry().getRectangle();
							
							rectDragedCell.setLocation(e.getX()-rectDragedCell.width/2+1, e.getY()-rectDragedCell.height/2+1);
							boundaryActived = !rectboundaryCell.contains(rectDragedCell);
						}
						if (boundaryActived){		//标记边界
							if(((BpmnGraph)graphComponent.getGraph()).isCanBound(boundaryCell) && dragedCell != null && 
									dragedCell.getValue() != null && (
											dragedCell.getValue() instanceof IntermediateCatchEvent || 
											dragedCell.getValue() instanceof BoundaryEvent)){
								marker.unmark();
								color = valid ? boundaryMarker.getValidColor() : boundaryMarker.getInvalidColor();
								boundaryMarker.highlight(state, color, valid);
							}
						}else{						//标记在单元内
							if(((BpmnGraph)graphComponent.getGraph()).isContainer(boundaryCell)){
								boundaryMarker.unmark();
								color = valid ? marker.getValidColor() : marker.getInvalidColor();
								marker.highlight(state, color, valid);
							}
						}
					}
				}
			}

			if (first != null)
			{
				if (movePreview.isActive())
				{
					double dx = e.getX() - first.x;
					double dy = e.getY() - first.y;

					if (graphComponent.isGridEnabledEvent(e))
					{
						mxGraph graph = graphComponent.getGraph();

						dx = graph.snap(dx);
						dy = graph.snap(dy);
					}

					boolean clone = isCloneEnabled()
							&& graphComponent.isCloneEvent(e);
					movePreview.update(e, dx, dy, clone);
					e.consume();
				}
				else if (cellBounds != null)
				{
					double dx = e.getX() - first.x;
					double dy = e.getY() - first.y;

					if (previewBounds != null)
					{
						setPreviewBounds(new Rectangle(getPreviewLocation(e,
								gridEnabledEvent), previewBounds.getSize()));
					}

					if (!isVisible() && graphComponent.isSignificant(dx, dy))
					{
						if (imagePreview && dragImage == null
								&& !graphComponent.isDragEnabled())
						{
							updateDragImage(cells);
						}
						

						setVisible(true);
					}

					e.consume();
				}
			}
		}
//		*/
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {	
		
		//记录绑定信息
		mxCell dragedCell = (mxCell)initialCell;
		if (dragedCell != null){
			boundaryCell = (mxCell)graphComponent.getCellAt(e.getX(), e.getY(),true);
			if (boundaryCell == null){
				((UfGraphCell)dragedCell).setBoundaryCell(null);			//解除绑定信息
			}else{ 
				if (boundaryCell != null && !boundaryCell.equals(dragedCell)){
					if(dragedCell.getParent().equals(boundaryCell)){
						boundaryActived = false;
					}else{
						Rectangle rectboundaryCell = boundaryCell.getGeometry().getRectangle();
						Rectangle rectDragedCell = dragedCell.getGeometry().getRectangle();
						rectDragedCell.setLocation(e.getX()-rectDragedCell.width/2+1, e.getY()-rectDragedCell.height/2+1);
						rectDragedCell.setSize(rectDragedCell.x+rectDragedCell.width/2-1, rectDragedCell.y+rectDragedCell.height/2-1);
						rectboundaryCell.setSize(rectboundaryCell.x+rectboundaryCell.width, rectboundaryCell.y+rectboundaryCell.height);
						boundaryActived = !rectboundaryCell.contains(rectDragedCell);
					}
					if (boundaryActived){
						((UfGraphCell)dragedCell).setBoundaryCell((UfGraphCell)boundaryCell);		//记录绑定信息
//						mxCellState dragedState = graphComponent.getGraph().getView().getState(dragedCell);
//						mxCellState boundaryState = graphComponent.getGraph().getView().getState(boundaryCell);
//						Rectangle dragedRect = dragedState.getRectangle();							//调整dragedcell位置
//						dragedRect.x = ((int)(boundaryState.getX()+ boundaryState.getWidth()-dragedState.getWidth()/2-e.getX()+ dragedRect.getX()));
//						((UfGraphCell)dragedCell).setGeometry(new mxGeometry(dragedRect.x,dragedRect.y,dragedRect.width,dragedRect.height));
					}else{
						((UfGraphCell)dragedCell).setBoundaryCell(null);			//解除绑定信息
					}
				}
			}
		}
		boundaryCell = null;
		
		// 移动边界上的Cell,被绑定到当前单元的单元
		if (first != null) {
			if (initialCell != null) {
				Object parent = (Object) graphComponent.getGraph().getModel()
						.getParent(initialCell);
				int childCount = graphComponent.getGraph().getModel().getChildCount(parent);
				for (int i = 0; i < childCount; i++) {
					UfGraphCell cell = (UfGraphCell) graphComponent.getGraph().getModel().getChildAt(parent, i);
					if (cell.getValue() != null) {
						BaseElement element = (BaseElement) cell.getValue();
						if (element instanceof BoundaryEvent) {
							BoundaryEvent boundary = (BoundaryEvent) element;
							if (boundary.getAttachedToRef().equals(((BaseElement) ((UfGraphCell) initialCell).getValue()).getId())) {
								double dx = e.getX() - first.x;
								double dy = e.getY() - first.y;
								if (graphComponent.isGridEnabledEvent(e)) {
									mxGraph graph = graphComponent.getGraph();
									dx = graph.snap(dx);
									dy = graph.snap(dy);
								}
								graphComponent.getGraph().cellsMoved(new Object[] { cell }, dx, dy, true,true);
							}
						}
					}
				}
			}
		}
		marker.setCurrentColor(marker.getInvalidColor());
		marker.unmark();
		boundaryMarker.setCurrentColor(boundaryMarker.getInvalidColor());
		boundaryMarker.unmark();
		super.mouseReleased(e);
	}
	
	@Override
	protected mxMovePreview createMovePreview() {
		return new BpmnMovePreview(graphComponent);
	}
	
	protected Cursor getCursor(MouseEvent e) {
		Cursor cursor = null;
		mxICell selectEntry = ((BpmnGraphComponent)graphComponent).getSelectedEntry();
		if(selectEntry!=null&&selectEntry.getValue()!=null&&selectEntry.isVertex()){
			return BpmnCursorFactory.getInstance().getCopyDropCursor();
		}else if (isMoveEnabled()) {
			Object cell = graphComponent.getCellAt(e.getX(), e.getY(), false);
			if (cell != null) {
				if (graphComponent.isFoldingEnabled() && graphComponent.hitFoldingIcon(cell, e.getX(), e.getY())) {
					cursor = FOLD_CURSOR;
				} else if (graphComponent.getGraph().isCellMovable(cell)) {
					cursor = MOVE_CURSOR;
				}
			}
		}

		return cursor;
	}

	protected BoundaryMarker createBoundaryMarker()
	{
		BoundaryMarker marker = new BoundaryMarker(graphComponent, Color.YELLOW)
		{
			private static final long serialVersionUID = -8451338653189373347L;

			public boolean isEnabled()
			{
				return graphComponent.getGraph().isDropEnabled();
			}

			public Object getCell(MouseEvent e)
			{
				mxIGraphModel model = graphComponent.getGraph().getModel();
				TransferHandler th = graphComponent.getTransferHandler();
				boolean isLocal = th instanceof mxGraphTransferHandler
						&& ((mxGraphTransferHandler) th).isLocalDrag();

				mxGraph graph = graphComponent.getGraph();
				Object cell = super.getCell(e);
				Object[] cells = (isLocal) ? graph.getSelectionCells()
						: dragCells;
				cell = graph.getDropTarget(cells, e.getPoint(), cell);

				// Checks if parent is dropped into child
				Object parent = cell;

				while (parent != null)
				{
					if (mxUtils.contains(cells, parent))
					{
						return null;
					}
					
					parent = model.getParent(parent);
				}

				boolean clone = graphComponent.isCloneEvent(e) && cloneEnabled;

				if (isLocal && cell != null && cells.length > 0 && !clone
						&& graph.getModel().getParent(cells[0]) == cell)
				{
					cell = null;
				}

				return cell;
			}

		};

		// Swimlane content area will not be transparent drop targets
		marker.setSwimlaneContentEnabled(true);

		return marker;
	}
	
}
